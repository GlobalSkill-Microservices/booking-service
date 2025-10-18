package com.globalskills.booking_service.Dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalendarResponse {

    Long id;

    Long ownerId;

    List<WeekSlotResponse> weekSlotResponses;

}
