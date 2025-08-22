package com.slapp.service;

import com.slapp.service.dto.StudioOperatingHoursDTO;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.slapp.domain.StudioOperatingHours}.
 */
public interface StudioOperatingHoursService {
    /**
     * Save a studioOperatingHours.
     *
     * @param studioOperatingHoursDTO the entity to save.
     * @return the persisted entity.
     */
    StudioOperatingHoursDTO save(StudioOperatingHoursDTO studioOperatingHoursDTO);

    /**
     * Updates a studioOperatingHours.
     *
     * @param studioOperatingHoursDTO the entity to update.
     * @return the persisted entity.
     */
    StudioOperatingHoursDTO update(StudioOperatingHoursDTO studioOperatingHoursDTO);

    /**
     * Partially updates a studioOperatingHours.
     *
     * @param studioOperatingHoursDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudioOperatingHoursDTO> partialUpdate(StudioOperatingHoursDTO studioOperatingHoursDTO);

    /**
     * Get all the studioOperatingHours.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudioOperatingHoursDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studioOperatingHours.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudioOperatingHoursDTO> findOne(Long id);

    /**
     * Delete the "id" studioOperatingHours.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get operating hours for a studio.
     *
     * @param studioId the studio id.
     * @return the list of operating hours.
     */
    List<StudioOperatingHoursDTO> findByStudioId(Long studioId);

    /**
     * Get operating hours for a studio on a specific day.
     *
     * @param studioId the studio id.
     * @param dayOfWeek the day of the week.
     * @return the list of operating hours for that day.
     */
    List<StudioOperatingHoursDTO> findByStudioIdAndDayOfWeek(Long studioId, DayOfWeek dayOfWeek);

    /**
     * Update operating hours for a studio (replaces all existing hours).
     *
     * @param studioId the studio id.
     * @param operatingHours the new operating hours.
     * @return the list of updated operating hours.
     */
    List<StudioOperatingHoursDTO> updateStudioOperatingHours(Long studioId, List<StudioOperatingHoursDTO> operatingHours);
}
