package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Common.PageResponse;
import com.globalskills.booking_service.Dto.BookingResponse;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Enum.BookingStatus;
import com.globalskills.booking_service.Exception.BookingException;
import com.globalskills.booking_service.Repository.BookingRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingQueryService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    TimeslotQueryService timeslotQueryService;

    public Booking findById(Long id){
        return bookingRepo.findById(id).orElseThrow(()-> new BookingException("Cant found booking", HttpStatus.NOT_FOUND));
    }

    public BookingResponse getBookingById(Long id){
        Booking booking = findById(id);
        Timeslot timeslot = timeslotQueryService.findById(booking.getTimeslot().getId());
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);
        TimeslotResponse timeslotResponse = modelMapper.map(timeslot, TimeslotResponse.class);
        bookingResponse.setTimeslotResponse(timeslotResponse);
        return bookingResponse;
    }

    public PageResponse<BookingResponse> getAllBCurrentUser(
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
            return null;
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

}
