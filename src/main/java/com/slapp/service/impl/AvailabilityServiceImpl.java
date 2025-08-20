package com.slapp.service.impl;

import com.slapp.domain.Availability;
import com.slapp.repository.AvailabilityRepository;
import com.slapp.service.AvailabilityService;
import com.slapp.service.dto.AvailabilityDTO;
import com.slapp.service.mapper.AvailabilityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.Availability}.
 */
@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    private final AvailabilityRepository availabilityRepository;

    private final AvailabilityMapper availabilityMapper;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository, AvailabilityMapper availabilityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.availabilityMapper = availabilityMapper;
    }

    @Override
    public AvailabilityDTO save(AvailabilityDTO availabilityDTO) {
        LOG.debug("Request to save Availability : {}", availabilityDTO);
        Availability availability = availabilityMapper.toEntity(availabilityDTO);
        availability = availabilityRepository.save(availability);
        return availabilityMapper.toDto(availability);
    }

    @Override
    public AvailabilityDTO update(AvailabilityDTO availabilityDTO) {
        LOG.debug("Request to update Availability : {}", availabilityDTO);
        Availability availability = availabilityMapper.toEntity(availabilityDTO);
        availability = availabilityRepository.save(availability);
        return availabilityMapper.toDto(availability);
    }

    @Override
    public Optional<AvailabilityDTO> partialUpdate(AvailabilityDTO availabilityDTO) {
        LOG.debug("Request to partially update Availability : {}", availabilityDTO);

        return availabilityRepository
            .findById(availabilityDTO.getId())
            .map(existingAvailability -> {
                availabilityMapper.partialUpdate(existingAvailability, availabilityDTO);

                return existingAvailability;
            })
            .map(availabilityRepository::save)
            .map(availabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityDTO> findAll() {
        LOG.debug("Request to get all Availabilities");
        return availabilityRepository.findAll().stream().map(availabilityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AvailabilityDTO> findOne(Long id) {
        LOG.debug("Request to get Availability : {}", id);
        return availabilityRepository.findById(id).map(availabilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Availability : {}", id);
        availabilityRepository.deleteById(id);
    }
}
