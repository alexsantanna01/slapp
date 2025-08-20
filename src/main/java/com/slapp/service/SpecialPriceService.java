package com.slapp.service;

import com.slapp.service.dto.SpecialPriceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.slapp.domain.SpecialPrice}.
 */
public interface SpecialPriceService {
    /**
     * Save a specialPrice.
     *
     * @param specialPriceDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialPriceDTO save(SpecialPriceDTO specialPriceDTO);

    /**
     * Updates a specialPrice.
     *
     * @param specialPriceDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialPriceDTO update(SpecialPriceDTO specialPriceDTO);

    /**
     * Partially updates a specialPrice.
     *
     * @param specialPriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialPriceDTO> partialUpdate(SpecialPriceDTO specialPriceDTO);

    /**
     * Get all the specialPrices.
     *
     * @return the list of entities.
     */
    List<SpecialPriceDTO> findAll();

    /**
     * Get the "id" specialPrice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialPriceDTO> findOne(Long id);

    /**
     * Delete the "id" specialPrice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
