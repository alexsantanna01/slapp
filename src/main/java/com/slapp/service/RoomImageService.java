package com.slapp.service;

import com.slapp.service.dto.RoomImageDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.slapp.domain.RoomImage}.
 */
public interface RoomImageService {
    /**
     * Save a roomImage.
     *
     * @param roomImageDTO the entity to save.
     * @return the persisted entity.
     */
    RoomImageDTO save(RoomImageDTO roomImageDTO);

    /**
     * Updates a roomImage.
     *
     * @param roomImageDTO the entity to update.
     * @return the persisted entity.
     */
    RoomImageDTO update(RoomImageDTO roomImageDTO);

    /**
     * Partially updates a roomImage.
     *
     * @param roomImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomImageDTO> partialUpdate(RoomImageDTO roomImageDTO);

    /**
     * Get all the roomImages.
     *
     * @return the list of entities.
     */
    List<RoomImageDTO> findAll();

    /**
     * Get the "id" roomImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomImageDTO> findOne(Long id);

    /**
     * Delete the "id" roomImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
