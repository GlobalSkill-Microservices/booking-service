package com.globalskills.booking_service.Exception;

import com.globalskills.booking_service.Common.BaseException;
import org.springframework.http.HttpStatus;

public class TimeslotException extends BaseException {
    public TimeslotException(String message, HttpStatus status) {
        super(message,status);
    }
}
