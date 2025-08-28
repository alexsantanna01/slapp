package com.slapp.service;

import com.slapp.repository.projections.StudioDetailProjection;
import com.slapp.repository.projections.StudioListProjection;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.StudioFilterDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    Page<StudioListProjection> getStudioRoomPagination(Pageable pageable, StudioFilterDTO filters);

    Optional<StudioDetailProjection> getStudioDetail(Long id);

    public List<StudioListProjection> findStudiosKeyset(
        String name,
        String city,
        String roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        LocalDateTime availabilityStartDateTime,
        LocalDateTime availabilityEndDateTime,
        Long lastId,
        int pageSize
    );
}
