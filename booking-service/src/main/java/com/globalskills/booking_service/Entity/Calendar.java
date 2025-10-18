package com.globalskills.booking_service.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long ownerId;

    @OneToMany(mappedBy = "calendar", fetch = FetchType.LAZY ,cascade = CascadeType.ALL, orphanRemoval = true)
    List<Timeslot> timeslots;

}
