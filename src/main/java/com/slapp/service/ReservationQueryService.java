package com.slapp.service;

import com.slapp.domain.*; // for static metamodels
import com.slapp.domain.Reservation;
import com.slapp.repository.ReservationRepository;
import com.slapp.service.criteria.ReservationCriteria;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.mapper.ReservationMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Reservation} entities in the database.
 * The main input is a {@link ReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReservationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservationQueryService extends QueryService<Reservation> {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationQueryService.class);

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationQueryService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Return a {@link Page} of {@link ReservationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservationDTO> findByCriteria(ReservationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification, page).map(reservationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReservationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.count(specification);
    }

    /**
     * Function to convert {@link ReservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reservation> createSpecification(ReservationCriteria criteria) {
        Specification<Reservation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Reservation_.id),
                buildRangeSpecification(criteria.getStartDateTime(), Reservation_.startDateTime),
                buildRangeSpecification(criteria.getEndDateTime(), Reservation_.endDateTime),
                buildRangeSpecification(criteria.getTotalPrice(), Reservation_.totalPrice),
                buildSpecification(criteria.getStatus(), Reservation_.status),
                buildRangeSpecification(criteria.getCreatedAt(), Reservation_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Reservation_.updatedAt),
                buildRangeSpecification(criteria.getCancelledAt(), Reservation_.cancelledAt),
                buildStringSpecification(criteria.getCancelReason(), Reservation_.cancelReason),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Reservation_.customer, JoinType.LEFT).get(UserProfile_.id)),
                buildSpecification(criteria.getRoomId(), root -> root.join(Reservation_.room, JoinType.LEFT).get(Room_.id))
            );
        }
        return specification;
    }
}
