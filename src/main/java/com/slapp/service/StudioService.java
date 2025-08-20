package com.slapp.service;

import com.slapp.service.dto.StudioDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.slapp.domain.Studio}.
 */
public interface StudioService {
    /**
     * Save a studio.
     *
     * @param studioDTO the entity to save.
     * @return the persisted entity.
     */
    StudioDTO save(StudioDTO studioDTO);

    /**
     * Updates a studio.
     *
     * @param studioDTO the entity to update.
     * @return the persisted entity.
     */
    StudioDTO update(StudioDTO studioDTO);

    /**
     * Partially updates a studio.
     *
     * @param studioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudioDTO> partialUpdate(StudioDTO studioDTO);

    /**
     * Get the "id" studio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudioDTO> findOne(Long id);

    /**
     * Delete the "id" studio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
