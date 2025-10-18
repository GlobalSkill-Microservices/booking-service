package com.globalskills.booking_service.Repository;

import com.globalskills.booking_service.Entity.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepo extends JpaRepository<Calendar,Long> {
    Optional<Calendar> findByOwnerId(Long accountId);
    Page<Calendar> findByOwnerId(Pageable pageable,Long accountId);
}
