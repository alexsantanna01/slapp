package com.slapp.service;

import com.slapp.domain.*; // for static metamodels
import com.slapp.domain.Room;
import com.slapp.repository.RoomRepository;
import com.slapp.service.criteria.RoomCriteria;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.mapper.RoomMapper;
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
 * Service for executing complex queries for {@link Room} entities in the database.
 * The main input is a {@link RoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RoomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoomQueryService extends QueryService<Room> {

    private static final Logger LOG = LoggerFactory.getLogger(RoomQueryService.class);

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public RoomQueryService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    /**
     * Return a {@link Page} of {@link RoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> findByCriteria(RoomCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Room> specification = createSpecification(criteria);
        return roomRepository.findAll(specification, page).map(roomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoomCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Room> specification = createSpecification(criteria);
        return roomRepository.count(specification);
    }

    /**
     * Function to convert {@link RoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Room> createSpecification(RoomCriteria criteria) {
        Specification<Room> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Room_.id),
                buildStringSpecification(criteria.getName(), Room_.name),
                buildRangeSpecification(criteria.getHourlyRate(), Room_.hourlyRate),
                buildRangeSpecification(criteria.getCapacity(), Room_.capacity),
                buildSpecification(criteria.getSoundproofed(), Room_.soundproofed),
                buildSpecification(criteria.getAirConditioning(), Room_.airConditioning),
                buildSpecification(criteria.getRoomType(), Room_.roomType),
                buildSpecification(criteria.getActive(), Room_.active),
                buildRangeSpecification(criteria.getCreatedAt(), Room_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Room_.updatedAt),
                buildSpecification(criteria.getStudioId(), root -> root.join(Room_.studio, JoinType.LEFT).get(Studio_.id))
            );
        }
        return specification;
    }
}
