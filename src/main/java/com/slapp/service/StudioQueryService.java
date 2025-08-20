package com.slapp.service;

import com.slapp.domain.*; // for static metamodels
import com.slapp.domain.Studio;
import com.slapp.repository.StudioRepository;
import com.slapp.service.criteria.StudioCriteria;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.mapper.StudioMapper;
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
 * Service for executing complex queries for {@link Studio} entities in the database.
 * The main input is a {@link StudioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StudioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudioQueryService extends QueryService<Studio> {

    private static final Logger LOG = LoggerFactory.getLogger(StudioQueryService.class);

    private final StudioRepository studioRepository;

    private final StudioMapper studioMapper;

    public StudioQueryService(StudioRepository studioRepository, StudioMapper studioMapper) {
        this.studioRepository = studioRepository;
        this.studioMapper = studioMapper;
    }

    /**
     * Return a {@link Page} of {@link StudioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudioDTO> findByCriteria(StudioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Studio> specification = createSpecification(criteria);
        return studioRepository.findAll(specification, page).map(studioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Studio> specification = createSpecification(criteria);
        return studioRepository.count(specification);
    }

    /**
     * Function to convert {@link StudioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Studio> createSpecification(StudioCriteria criteria) {
        Specification<Studio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Studio_.id),
                buildStringSpecification(criteria.getName(), Studio_.name),
                buildStringSpecification(criteria.getAddress(), Studio_.address),
                buildStringSpecification(criteria.getCity(), Studio_.city),
                buildStringSpecification(criteria.getState(), Studio_.state),
                buildStringSpecification(criteria.getZipCode(), Studio_.zipCode),
                buildRangeSpecification(criteria.getLatitude(), Studio_.latitude),
                buildRangeSpecification(criteria.getLongitude(), Studio_.longitude),
                buildStringSpecification(criteria.getPhone(), Studio_.phone),
                buildStringSpecification(criteria.getEmail(), Studio_.email),
                buildStringSpecification(criteria.getWebsite(), Studio_.website),
                buildStringSpecification(criteria.getImage(), Studio_.image),
                buildSpecification(criteria.getActive(), Studio_.active),
                buildRangeSpecification(criteria.getCreatedAt(), Studio_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Studio_.updatedAt),
                buildSpecification(criteria.getOwnerId(), root -> root.join(Studio_.owner, JoinType.LEFT).get(UserProfile_.id)),
                buildSpecification(criteria.getCancellationPolicyId(), root ->
                    root.join(Studio_.cancellationPolicy, JoinType.LEFT).get(CancellationPolicy_.id)
                )
            );
        }
        return specification;
    }
}
