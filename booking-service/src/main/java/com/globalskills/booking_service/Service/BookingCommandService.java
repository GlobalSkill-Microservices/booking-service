package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Dto.BookingRequest;
import com.globalskills.booking_service.Dto.BookingResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Enum.SlotStatus;
import com.globalskills.booking_service.Exception.TimeslotException;
import com.globalskills.booking_service.Repository.BookingRepo;
import com.globalskills.booking_service.Repository.TimeslotRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public BookingResponse create(BookingRequest request,Long accountId){
        Timeslot timeslot = timeslotQueryService.findById(request.getTimeslotId());
        if(timeslot.getSlotStatus() != null){
            throw new TimeslotException("Cant booking this slot, already: + status", HttpStatus.BAD_REQUEST);
        }
        Booking booking = new Booking();
        booking.setMentorId(request.getMentorId());
        booking.setAccountId(accountId);
        //set booking status
        booking.setTimeslot(timeslot);
        bookingRepo.save(booking);
        //set timeslot status
        timeslotRepo.save(timeslot);
        TimeslotResponse timeslotResponse = modelMapper.map(timeslot, TimeslotResponse.class);
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);
        bookingResponse.setTimeslotResponse(timeslotResponse);
        return bookingResponse;
    }

    public void update(BookingRequest request,Long accountId){
    }

    @Transactional
    public void delete(Long id){
        Booking booking = bookingQueryService.findById(id);
        //set booking status

    }

}
