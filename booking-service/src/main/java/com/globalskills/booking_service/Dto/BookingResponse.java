package com.globalskills.booking_service.Dto;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Enum.BookingStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    Long id;

    AccountDto mentorId;

    AccountDto userId;

    BookingStatus bookingStatus;

    TimeslotResponse timeslotResponse;
}
