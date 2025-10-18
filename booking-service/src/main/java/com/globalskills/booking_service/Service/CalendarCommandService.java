package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Entity.Calendar;
import com.globalskills.booking_service.Repository.CalendarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarCommandService {

    @Autowired
    CalendarRepo calendarRepo;

    public void create(Long accountId){
        Calendar calendar = new Calendar();
        calendar.setOwnerId(accountId);
        calendarRepo.save(calendar);
    }

}
