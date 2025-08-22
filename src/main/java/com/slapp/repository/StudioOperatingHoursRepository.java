package com.slapp.repository;

import com.slapp.domain.StudioOperatingHours;
import java.time.DayOfWeek;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudioOperatingHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudioOperatingHoursRepository extends JpaRepository<StudioOperatingHours, Long> {
    List<StudioOperatingHours> findByStudioIdOrderByDayOfWeek(Long studioId);

    List<StudioOperatingHours> findByStudioIdAndDayOfWeek(Long studioId, DayOfWeek dayOfWeek);

    void deleteByStudioId(Long studioId);
}
