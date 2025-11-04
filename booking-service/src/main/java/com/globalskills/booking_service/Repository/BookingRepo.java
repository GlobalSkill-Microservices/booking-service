package com.globalskills.booking_service.Repository;

import com.globalskills.booking_service.Common.AccountDto;
import com.globalskills.booking_service.Common.TopMentorProjection;
import com.globalskills.booking_service.Entity.Booking;
import com.globalskills.booking_service.Enum.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Long> {
    Page<Booking> findAllByAccountIdAndBookingStatus(PageRequest pageRequest, Long accountId, BookingStatus bookingStatus);

    Booking findByMentorIdAndTimeslotIdAndBookingStatus(Long accountId,Long timeslotId,BookingStatus bookingStatus);

    Booking findByAccountIdAndTimeslotId(Long accountId, Long timeslotId);

    @Query(
            value = """
            SELECT 
                b.mentor_id AS mentorId, 
                COUNT(*) AS confirmedCount
            FROM bookings b
            WHERE b.booking_status = 'CONFIRMED'
            GROUP BY b.mentor_id
            ORDER BY confirmedCount DESC
            LIMIT :limit
            """,
            nativeQuery = true
    )
    List<TopMentorProjection> findTopMentors(@Param("limit") int limit);
}
