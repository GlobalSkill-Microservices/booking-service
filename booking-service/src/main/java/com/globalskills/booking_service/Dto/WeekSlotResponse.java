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
public class WeekSlotResponse implements Comparable<WeekSlotResponse>{
    Integer week;
    Integer month;
    Integer year;
    List<TimeslotResponse> timeslotResponses;

    @Override
    public int compareTo(WeekSlotResponse other) {
        int yearCompare = this.year.compareTo(other.year);
        if (yearCompare != 0) return yearCompare;

        int monthCompare = this.month.compareTo(other.month);
        if (monthCompare != 0) return monthCompare;

        return this.week.compareTo(other.week);
    }

}
