package com.slapp.service.impl;

import com.slapp.domain.Studio;
import com.slapp.repository.StudioRepository;
import com.slapp.service.StudioService;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.mapper.StudioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.Studio}.
 */
@Service
@Transactional
public class StudioServiceImpl implements StudioService {

    private static final Logger LOG = LoggerFactory.getLogger(StudioServiceImpl.class);

    private final StudioRepository studioRepository;

    private final StudioMapper studioMapper;

    public StudioServiceImpl(StudioRepository studioRepository, StudioMapper studioMapper) {
        this.studioRepository = studioRepository;
        this.studioMapper = studioMapper;
    }

    @Override
    public StudioDTO save(StudioDTO studioDTO) {
        LOG.debug("Request to save Studio : {}", studioDTO);
        Studio studio = studioMapper.toEntity(studioDTO);
        studio = studioRepository.save(studio);
        return studioMapper.toDto(studio);
    }

    @Override
    public StudioDTO update(StudioDTO studioDTO) {
        LOG.debug("Request to update Studio : {}", studioDTO);
        Studio studio = studioMapper.toEntity(studioDTO);
        studio = studioRepository.save(studio);
        return studioMapper.toDto(studio);
    }

    @Override
    public Optional<StudioDTO> partialUpdate(StudioDTO studioDTO) {
        LOG.debug("Request to partially update Studio : {}", studioDTO);

        return studioRepository
            .findById(studioDTO.getId())
            .map(existingStudio -> {
                studioMapper.partialUpdate(existingStudio, studioDTO);

                return existingStudio;
            })
            .map(studioRepository::save)
            .map(studioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudioDTO> findOne(Long id) {
        LOG.debug("Request to get Studio : {}", id);
        return studioRepository.findById(id).map(studioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Studio : {}", id);
        studioRepository.deleteById(id);
    }
}
