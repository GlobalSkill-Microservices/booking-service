package com.globalskills.booking_service.Repository;

import com.globalskills.booking_service.Entity.Timeslot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeslotRepo extends JpaRepository<Timeslot,Long> {
    Page<Timeslot> findAllByAccountId(PageRequest pageRequest, Long accountId);
}
