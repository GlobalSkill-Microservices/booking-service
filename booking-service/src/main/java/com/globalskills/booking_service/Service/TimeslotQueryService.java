package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Exception.TimeslotException;
import com.globalskills.booking_service.Repository.TimeslotRepo;
import com.globalskills.booking_service.Service.CLient.AccountClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TimeslotQueryService {

    @Autowired
    TimeslotRepo timeslotRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountClientService accountClientService;

    public Timeslot findById(Long id){
        return timeslotRepo.findById(id).orElseThrow(()-> new TimeslotException("Cant found timeslot", HttpStatus.NOT_FOUND));
    }

    public TimeslotResponse getTimeslotById(Long id){
        Timeslot timeslot = findById(id);
        AccountDto mentor = accountClientService.fetchAccount(timeslot.getAccountId());
        TimeslotResponse response = modelMapper.map(timeslot, TimeslotResponse.class);
        response.setMentorId(mentor);
        return response;
    }

    public PageResponse<TimeslotResponse> getAllByAccountId(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Long accountId
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Timeslot> timeslotPage = timeslotRepo.findAllByAccountId(pageRequest, accountId);

        if (timeslotPage.isEmpty()) {
            return new PageResponse<>(
                    Collections.emptyList(),
                    page,
                    size,
                    0,
                    0,
                    true
            );
        }

        List<TimeslotResponse> responses = mapTimeslotToResponse(timeslotPage.getContent());

        return new PageResponse<>(
                responses,
                page,
                size,
                timeslotPage.getTotalElements(),
                timeslotPage.getTotalPages(),
                timeslotPage.isLast()
        );
    }

    private List<TimeslotResponse> mapTimeslotToResponse(List<Timeslot> timeslots) {
        List<Long> accountIds = timeslots.stream()
                .map(Timeslot::getAccountId)
                .distinct()
                .toList();

        List<AccountDto> accountDtos = accountClientService.fetchListAccount(accountIds);
        Map<Long, AccountDto> accountMap = accountDtos.stream()
                .collect(Collectors.toMap(AccountDto::getId, Function.identity()));

        return timeslots.stream()
                .map(timeslot -> {
                    TimeslotResponse response = modelMapper.map(timeslot, TimeslotResponse.class);
                    response.setMentorId(accountMap.get(timeslot.getAccountId()));
                    return response;
                })
                .toList();
    }
}
