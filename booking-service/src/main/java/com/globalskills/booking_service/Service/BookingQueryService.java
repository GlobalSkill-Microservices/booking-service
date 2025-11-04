package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Common.TopMentorProjection;
import com.globalskills.booking_service.Common.TopMentorResponse;
import com.globalskills.booking_service.Dto.BookingResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Enum.BookingStatus;
import com.globalskills.booking_service.Exception.BookingException;
import com.globalskills.booking_service.Repository.BookingRepo;
import com.globalskills.booking_service.Service.CLient.AccountClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingQueryService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    TimeslotQueryService timeslotQueryService;

    @Autowired
    AccountClientService accountClientService;

    public Booking findById(Long id){
        return bookingRepo.findById(id).orElseThrow(()-> new BookingException("Cant found booking", HttpStatus.NOT_FOUND));
    }

    public BookingResponse getBookingById(Long id) {

        Booking booking = findById(id);

        Timeslot timeslot = timeslotQueryService.findById(booking.getTimeslot().getId());

        List<Long> accountIds = List.of(booking.getMentorId(), booking.getAccountId());

        Map<Long, AccountDto> accountMap = accountClientService.getAccountMapByIds(accountIds);

        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);

        bookingResponse.setMentorId(accountMap.get(booking.getMentorId()));

        bookingResponse.setUserId(accountMap.get(booking.getAccountId()));

        TimeslotResponse timeslotResponse = modelMapper.map(timeslot, TimeslotResponse.class);

        timeslotResponse.setMentorId(accountMap.get(booking.getMentorId()));

        bookingResponse.setTimeslotResponse(timeslotResponse);

        return bookingResponse;
    }

    public PageResponse<BookingResponse> getAllByCurrentUser(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Long accountId,
            BookingStatus bookingStatus
    ){
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Booking> bookingPage = bookingRepo.findAllByAccountIdAndBookingStatus(pageRequest,accountId,bookingStatus);
        if(bookingPage.isEmpty()){
            return new PageResponse<>(
                    Collections.emptyList(),
                    page,
                    size,
                    0,
                    0,
                    true
            );
        }
        List<BookingResponse> responses = bookingPage
                .stream()
                .map(booking -> getBookingById(booking.getId()))
                .toList();
        return new PageResponse<>(
                responses,
                page,
                size,
                bookingPage.getTotalElements(),
                bookingPage.getTotalPages(),
                bookingPage.isLast()
        );
    }

    public BookingResponse getByCurrentTeacher(
            Long accountId,
            Long timeslotId,
            BookingStatus bookingStatus
    ){
        Booking booking = bookingRepo.findByMentorIdAndTimeslotIdAndBookingStatus(accountId,timeslotId,bookingStatus);

        return getBookingById(booking.getId());
    }


    public List<TopMentorResponse> getTopMentors(int limit) {

        List<TopMentorProjection> mentors = bookingRepo.findTopMentors(limit);

        List<Long> accountIds = mentors.stream()
                .map(TopMentorProjection::getMentorId)
                .distinct()
                .toList();

        List<AccountDto> accountDtos = accountClientService.fetchListAccount(accountIds);

        Map<Long, AccountDto> accountMap = accountDtos.stream()
                .collect(Collectors.toMap(AccountDto::getId, dto -> dto));

        List<TopMentorResponse> response = mentors.stream()
                .map(m -> {
                    AccountDto dto = accountMap.get(m.getMentorId());
                    if (dto == null) return null;
                    return new TopMentorResponse(
                            dto.getId(),
                            dto.getUsername(),
                            dto.getFullName(),
                            dto.getAvatarUrl(),
                            m.getConfirmedCount()
                    );
                })
                .filter(Objects::nonNull)
                .toList();

        return response;
    }


}
