package com.slapp.service;

import com.slapp.service.dto.CancellationPolicyDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.slapp.domain.CancellationPolicy}.
 */
public interface CancellationPolicyService {
    /**
     * Save a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to save.
     * @return the persisted entity.
     */
    CancellationPolicyDTO save(CancellationPolicyDTO cancellationPolicyDTO);

    /**
     * Updates a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to update.
     * @return the persisted entity.
     */
    CancellationPolicyDTO update(CancellationPolicyDTO cancellationPolicyDTO);

    /**
     * Partially updates a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CancellationPolicyDTO> partialUpdate(CancellationPolicyDTO cancellationPolicyDTO);

    /**
     * Get all the cancellationPolicies.
     *
     * @return the list of entities.
     */
    List<CancellationPolicyDTO> findAll();

    /**
     * Get the "id" cancellationPolicy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CancellationPolicyDTO> findOne(Long id);

    /**
     * Delete the "id" cancellationPolicy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
