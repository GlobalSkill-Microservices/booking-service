package com.globalskills.booking_service.Entity;

import com.globalskills.booking_service.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long mentorId;

    Long accountId;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    @OneToOne
    @JoinColumn(name = "timeslot_id", unique = true, nullable = false)
    Timeslot timeslot;

}
