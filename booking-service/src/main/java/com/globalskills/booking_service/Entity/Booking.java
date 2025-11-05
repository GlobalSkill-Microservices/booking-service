package com.globalskills.booking_service.Entity;

import com.globalskills.booking_service.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

    @CreationTimestamp
    @Column(nullable = true, updatable = false)
    Instant createdAt;

}
