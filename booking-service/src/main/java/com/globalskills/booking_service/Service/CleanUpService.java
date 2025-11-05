package com.globalskills.booking_service.Service;

import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Entity.Timeslot;
import com.globalskills.booking_service.Enum.BookingStatus;
import com.globalskills.booking_service.Enum.SlotStatus;
import com.globalskills.booking_service.Repository.BookingRepo;
import com.globalskills.booking_service.Repository.TimeslotRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CleanUpService {

    private static final Logger log = LoggerFactory.getLogger(CleanUpService.class);
    private static final int EXPIRATION_MINUTES = 15;

    @Autowired
    private BookingRepo bookingRepository;

    @Autowired
    private TimeslotRepo timeslotRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredBookings() {
        log.info("ExpiredBookingTask: Starting scan...");

        Instant expiryTime = Instant.now().minus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        List<Booking> expiredBookings;
        try {
            expiredBookings = bookingRepository.findByBookingStatusAndCreatedAtBefore(
                    BookingStatus.PENDING,
                    expiryTime
            );
        } catch (Exception e) {
            log.error("ExpiredBookingTask: Failed to query or lock expired bookings.", e);
            return;
        }

        if (expiredBookings.isEmpty()) {
            log.info("ExpiredBookingTask: No expired bookings found. Scan complete.");
            return;
        }

        log.warn("ExpiredBookingTask: Found {} expired bookings. Proceeding to release slots...", expiredBookings.size());

        for (Booking booking : expiredBookings) {

            Timeslot timeslot = booking.getTimeslot();

            if (timeslot != null) {
                timeslot.setSlotStatus(SlotStatus.AVAILABLE);
                timeslotRepository.save(timeslot);

                booking.setBookingStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);

                log.warn("ExpiredBookingTask: Released slotId: {} and cancelled bookingId: {}",
                        timeslot.getId(), booking.getId());
            } else {
                log.error("ExpiredBookingTask: BookingId: {} has no associated timeslot. Status set to CANCELLED.",
                        booking.getId());
            }
        }

        log.info("ExpiredBookingTask: Finished cleanup task. Total items processed: {}", expiredBookings.size());
    }

}
