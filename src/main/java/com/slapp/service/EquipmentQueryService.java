package com.slapp.service;

import com.slapp.domain.*; // for static metamodels
import com.slapp.domain.Equipment;
import com.slapp.repository.EquipmentRepository;
import com.slapp.service.criteria.EquipmentCriteria;
import com.slapp.service.dto.EquipmentDTO;
import com.slapp.service.mapper.EquipmentMapper;
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
 * Service for executing complex queries for {@link Equipment} entities in the database.
 * The main input is a {@link EquipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EquipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentQueryService extends QueryService<Equipment> {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentQueryService.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentQueryService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    /**
     * Return a {@link Page} of {@link EquipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findByCriteria(EquipmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.findAll(specification, page).map(equipmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Equipment> createSpecification(EquipmentCriteria criteria) {
        Specification<Equipment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Equipment_.id),
                buildStringSpecification(criteria.getName(), Equipment_.name),
                buildStringSpecification(criteria.getBrand(), Equipment_.brand),
                buildStringSpecification(criteria.getModel(), Equipment_.model),
                buildSpecification(criteria.getAvailable(), Equipment_.available),
                buildSpecification(criteria.getEquipmentType(), Equipment_.equipmentType),
                buildRangeSpecification(criteria.getCreatedAt(), Equipment_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Equipment_.updatedAt),
                buildSpecification(criteria.getRoomId(), root -> root.join(Equipment_.room, JoinType.LEFT).get(Room_.id))
            );
        }
        return specification;
    }
}
