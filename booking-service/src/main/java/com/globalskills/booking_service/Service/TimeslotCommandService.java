package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Dto.TimeslotRequest;
import com.globalskills.booking_service.Dto.TimeslotResponse;
import com.globalskills.booking_service.Entity.Calendar;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Exception.TimeslotException;
import com.globalskills.booking_service.Repository.TimeslotRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimeslotCommandService {

    @Autowired
    CalendarQueryService calendarQueryService;

    @Autowired
    TimeslotQueryService timeslotQueryService;

    @Autowired
    TimeslotRepo timeslotRepo;

    @Autowired
    ModelMapper modelMapper;

    public TimeslotResponse createByTeacher(Long accountId, TimeslotRequest request){
        Timeslot timeslot = modelMapper.map(request,Timeslot.class);
        Calendar calendar = calendarQueryService.findByAccountId(accountId);
        //set timeslot status
        timeslot.setCalendar(calendar);
        timeslot.setAccountId(accountId);
        timeslotRepo.save(timeslot);
        return modelMapper.map(timeslot, TimeslotResponse.class);
    }

    public TimeslotResponse updateByTeacher(Long id, TimeslotRequest request){
        Timeslot oldTimeslot = timeslotQueryService.findById(id);
        oldTimeslot.setSlotDate(request.getSlotDate());
        oldTimeslot.setStartTime(request.getStartTime());
        oldTimeslot.setEndTime(request.getEndTime());
        timeslotRepo.save(oldTimeslot);
        return modelMapper.map(oldTimeslot, TimeslotResponse.class);
    }

    @Transactional
    public void delete(Long id){
        Timeslot timeslot = timeslotQueryService.findById(id);
        if(timeslot.getBooking()!=null){
            throw new TimeslotException("Cant delete, already booked", HttpStatus.BAD_REQUEST);
        }
        timeslotRepo.delete(timeslot);
    }
}
