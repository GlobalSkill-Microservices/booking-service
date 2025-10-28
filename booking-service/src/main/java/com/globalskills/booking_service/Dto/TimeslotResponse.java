package com.globalskills.booking_service.Dto;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Enum.SlotStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeslotResponse {

    Long id;

    SlotStatus slotStatus;

    LocalDate slotDate;

    LocalTime startTime;

    LocalTime endTime;

    AccountDto mentorId;

    Long roomId;

    String linkUrlRoom;
}
