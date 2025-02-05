// src/main/java/com/absoluto/demo/repository/ScheduleRepository.java
package com.absoluto.demo.repository;

import com.absoluto.demo.centre.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // Poți adăuga metode custom dacă este nevoie, de exemplu:
    // List<Schedule> findByCentreId(Long centreId);
}
