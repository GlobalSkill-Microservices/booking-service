package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Dto.BookingRequest;
import com.globalskills.booking_service.Dto.BookingResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Enum.BookingStatus;
import com.globalskills.booking_service.Enum.SlotStatus;
import com.globalskills.booking_service.Exception.TimeslotException;
import com.globalskills.booking_service.Repository.BookingRepo;
import com.globalskills.booking_service.Repository.TimeslotRepo;
import com.globalskills.booking_service.Service.CLient.AccountClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BookingCommandService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingQueryService bookingQueryService;

    @Autowired
    TimeslotQueryService timeslotQueryService;

    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    TimeslotRepo timeslotRepo;

    @Autowired
    AccountClientService accountClientService;

    public BookingResponse create(BookingRequest request,Long accountId){
        Timeslot timeslot = timeslotQueryService.findById(request.getTimeslotId());
        if(timeslot.getSlotStatus() != null){
            throw new TimeslotException("Cant booking this slot, already: + status", HttpStatus.BAD_REQUEST);
        }
        Booking booking = new Booking();
        booking.setMentorId(request.getMentorId());
        booking.setAccountId(accountId);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTimeslot(timeslot);
        bookingRepo.save(booking);
        timeslot.setSlotStatus(SlotStatus.BOOKED);
        timeslotRepo.save(timeslot);
        TimeslotResponse timeslotResponse = modelMapper.map(timeslot, TimeslotResponse.class);
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);

        List<Long> accountIds = List.of(booking.getMentorId(), booking.getAccountId());
        Map<Long, AccountDto> accountMap = accountClientService.getAccountMapByIds(accountIds);

        bookingResponse.setMentorId(accountMap.get(booking.getMentorId()));
        bookingResponse.setUserId(accountMap.get(booking.getAccountId()));
        bookingResponse.setTimeslotResponse(timeslotResponse);

        return bookingResponse;
    }

    public void update(BookingRequest request,Long accountId){
    }

    @Transactional
    public void delete(Long id){
        Booking booking = bookingQueryService.findById(id);
    }

}
