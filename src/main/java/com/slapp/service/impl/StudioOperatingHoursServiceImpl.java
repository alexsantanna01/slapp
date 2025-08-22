package com.slapp.service.impl;

import com.slapp.domain.Studio;
import com.slapp.domain.StudioOperatingHours;
import com.slapp.repository.StudioOperatingHoursRepository;
import com.slapp.service.StudioOperatingHoursService;
import com.slapp.service.dto.StudioOperatingHoursDTO;
import com.slapp.service.mapper.StudioOperatingHoursMapper;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.StudioOperatingHours}.
 */
@Service
@Transactional
public class StudioOperatingHoursServiceImpl implements StudioOperatingHoursService {

    private static final Logger log = LoggerFactory.getLogger(StudioOperatingHoursServiceImpl.class);

    private final StudioOperatingHoursRepository studioOperatingHoursRepository;

    private final StudioOperatingHoursMapper studioOperatingHoursMapper;

    public StudioOperatingHoursServiceImpl(
        StudioOperatingHoursRepository studioOperatingHoursRepository,
        StudioOperatingHoursMapper studioOperatingHoursMapper
    ) {
        this.studioOperatingHoursRepository = studioOperatingHoursRepository;
        this.studioOperatingHoursMapper = studioOperatingHoursMapper;
    }

    @Override
    public StudioOperatingHoursDTO save(StudioOperatingHoursDTO studioOperatingHoursDTO) {
        log.debug("Request to save StudioOperatingHours : {}", studioOperatingHoursDTO);
        StudioOperatingHours studioOperatingHours = studioOperatingHoursMapper.toEntity(studioOperatingHoursDTO);
        // Set studio manually
        if (studioOperatingHoursDTO.getStudioId() != null) {
            Studio studio = new Studio();
            studio.setId(studioOperatingHoursDTO.getStudioId());
            studioOperatingHours.setStudio(studio);
        }
        studioOperatingHours = studioOperatingHoursRepository.save(studioOperatingHours);
        return studioOperatingHoursMapper.toDto(studioOperatingHours);
    }

    @Override
    public StudioOperatingHoursDTO update(StudioOperatingHoursDTO studioOperatingHoursDTO) {
        log.debug("Request to update StudioOperatingHours : {}", studioOperatingHoursDTO);
        StudioOperatingHours studioOperatingHours = studioOperatingHoursMapper.toEntity(studioOperatingHoursDTO);
        // Set studio manually
        if (studioOperatingHoursDTO.getStudioId() != null) {
            Studio studio = new Studio();
            studio.setId(studioOperatingHoursDTO.getStudioId());
            studioOperatingHours.setStudio(studio);
        }
        studioOperatingHours = studioOperatingHoursRepository.save(studioOperatingHours);
        return studioOperatingHoursMapper.toDto(studioOperatingHours);
    }

    @Override
    public Optional<StudioOperatingHoursDTO> partialUpdate(StudioOperatingHoursDTO studioOperatingHoursDTO) {
        log.debug("Request to partially update StudioOperatingHours : {}", studioOperatingHoursDTO);

        return studioOperatingHoursRepository
            .findById(studioOperatingHoursDTO.getId())
            .map(existingStudioOperatingHours -> {
                studioOperatingHoursMapper.partialUpdate(existingStudioOperatingHours, studioOperatingHoursDTO);

                return existingStudioOperatingHours;
            })
            .map(studioOperatingHoursRepository::save)
            .map(studioOperatingHoursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudioOperatingHoursDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudioOperatingHours");
        return studioOperatingHoursRepository.findAll(pageable).map(studioOperatingHoursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudioOperatingHoursDTO> findOne(Long id) {
        log.debug("Request to get StudioOperatingHours : {}", id);
        return studioOperatingHoursRepository.findById(id).map(studioOperatingHoursMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudioOperatingHours : {}", id);
        studioOperatingHoursRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudioOperatingHoursDTO> findByStudioId(Long studioId) {
        log.debug("Request to get StudioOperatingHours for studio : {}", studioId);
        return studioOperatingHoursRepository
            .findByStudioIdOrderByDayOfWeek(studioId)
            .stream()
            .map(studioOperatingHoursMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudioOperatingHoursDTO> findByStudioIdAndDayOfWeek(Long studioId, DayOfWeek dayOfWeek) {
        log.debug("Request to get StudioOperatingHours for studio {} on {}", studioId, dayOfWeek);
        return studioOperatingHoursRepository
            .findByStudioIdAndDayOfWeek(studioId, dayOfWeek)
            .stream()
            .map(studioOperatingHoursMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public List<StudioOperatingHoursDTO> updateStudioOperatingHours(Long studioId, List<StudioOperatingHoursDTO> operatingHours) {
        log.debug("Request to update operating hours for studio : {}", studioId);

        // Delete existing operating hours for this studio
        studioOperatingHoursRepository.deleteByStudioId(studioId);

        // Save new operating hours
        return operatingHours
            .stream()
            .map(dto -> {
                dto.setStudioId(studioId);
                dto.setId(null); // Ensure new entities are created
                return save(dto);
            })
            .toList();
    }
}
