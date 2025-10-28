package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.CalendarResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Dto.WeekSlotResponse;
import com.globalskills.booking_service.Entity.Calendar;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Exception.CalendarException;
import com.globalskills.booking_service.Repository.CalendarRepo;
import com.globalskills.booking_service.Service.CLient.AccountClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalendarQueryService {

    @Autowired
    TimeslotQueryService timeslotQueryService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CalendarRepo calendarRepo;

    @Autowired
    AccountClientService accountClientService;


    public Calendar findById(Long id){
        return calendarRepo.findById(id).orElseThrow(()->new CalendarException("Cant found", HttpStatus.BAD_REQUEST));
    }

    public Calendar findByAccountId(Long accountId){
        return calendarRepo.findByOwnerId(accountId).orElseThrow(()-> new CalendarException("Cant found calendar with owner id: " + accountId,HttpStatus.NOT_FOUND));
    }

    public PageResponse<CalendarResponse> getByCurrentAccount(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Long accountId){

        Calendar calendar = findByAccountId(accountId);

        AccountDto ownerId = accountClientService.fetchAccount(accountId);

        PageResponse<TimeslotResponse> timeslotPage = timeslotQueryService.getAllByAccountId(page, size, sortBy, sortDir, accountId);

        Map<String, List<TimeslotResponse>> grouped = timeslotPage.getContent()
                .stream()
                .collect(Collectors.groupingBy(timeslotResponse -> {
                    LocalDate date = timeslotResponse.getSlotDate();
                    WeekFields weekFields = WeekFields.of(Locale.getDefault());
                    int week = date.get(weekFields.weekOfYear());
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    return year + "-" + month + "-" + week;
                }));

        List<WeekSlotResponse> weekSlotResponses = grouped.entrySet().stream()
                .map(entry ->{
                    String[] parts = entry.getKey().split("-");
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int week = Integer.parseInt(parts[2]);

                    WeekSlotResponse weekSlotResponse = new WeekSlotResponse();
                    weekSlotResponse.setYear(year);
                    weekSlotResponse.setMonth(month);
                    weekSlotResponse.setWeek(week);
                    weekSlotResponse.setTimeslotResponses(entry.getValue()
                            .stream()
                            .sorted(Comparator.comparing(TimeslotResponse::getSlotDate)
                                    .thenComparing(TimeslotResponse::getStartTime))
                            .toList());
                    return weekSlotResponse;

                })
                .sorted()
                .toList();

        CalendarResponse calendarResponse = modelMapper.map(calendar, CalendarResponse.class);
        calendarResponse.setWeekSlotResponses(weekSlotResponses);
        calendarResponse.setOwnerId(ownerId);

        return new PageResponse<>(
                List.of(calendarResponse),
                page,
                size,
                timeslotPage.getTotalElements(),
                timeslotPage.getTotalPages(),
                timeslotPage.isLast()
        );

    }




}
