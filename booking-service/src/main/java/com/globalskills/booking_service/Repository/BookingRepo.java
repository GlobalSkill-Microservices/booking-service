package com.globalskills.booking_service.Repository;

import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Enum.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Long> {
    Page<Booking> findAllByAccountIdAndBookingStatus(PageRequest pageRequest, Long accountId, BookingStatus bookingStatus);
}
