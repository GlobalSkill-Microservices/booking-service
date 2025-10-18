package com.globalskills.booking_service.Entity;

import com.globalskills.booking_service.Enum.SlotStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate slotDate;
    LocalTime startTime;
    LocalTime endTime;
    Long accountId;
    @Enumerated(EnumType.STRING)
    SlotStatus slotStatus;

    Long roomId;
    String linkUrlRoom;

    @OneToOne(mappedBy = "timeslot", fetch = FetchType.LAZY)
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    Calendar calendar;
}
