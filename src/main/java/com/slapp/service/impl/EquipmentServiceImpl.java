package com.slapp.service.impl;

import com.slapp.domain.Equipment;
import com.slapp.repository.EquipmentRepository;
import com.slapp.service.EquipmentService;
import com.slapp.service.dto.EquipmentDTO;
import com.slapp.service.mapper.EquipmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.Equipment}.
 */
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    @Override
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        LOG.debug("Request to save Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    @Override
    public EquipmentDTO update(EquipmentDTO equipmentDTO) {
        LOG.debug("Request to update Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    @Override
    public Optional<EquipmentDTO> partialUpdate(EquipmentDTO equipmentDTO) {
        LOG.debug("Request to partially update Equipment : {}", equipmentDTO);

        return equipmentRepository
            .findById(equipmentDTO.getId())
            .map(existingEquipment -> {
                equipmentMapper.partialUpdate(existingEquipment, equipmentDTO);

                return existingEquipment;
            })
            .map(equipmentRepository::save)
            .map(equipmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipmentDTO> findOne(Long id) {
        LOG.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id).map(equipmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
    }
}
