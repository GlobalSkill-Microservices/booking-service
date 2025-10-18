package com.globalskills.booking_service.Exception;

import com.globalskills.booking_service.Common.BaseException;
import org.springframework.http.HttpStatus;

public class CalendarException extends BaseException {
    public CalendarException(String message, HttpStatus status) {
        super(message,status);
    }
}
