package com.slapp.service.impl;

import com.slapp.domain.RoomImage;
import com.slapp.repository.RoomImageRepository;
import com.slapp.service.RoomImageService;
import com.slapp.service.dto.RoomImageDTO;
import com.slapp.service.mapper.RoomImageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.RoomImage}.
 */
@Service
@Transactional
public class RoomImageServiceImpl implements RoomImageService {

    private static final Logger LOG = LoggerFactory.getLogger(RoomImageServiceImpl.class);

    private final RoomImageRepository roomImageRepository;

    private final RoomImageMapper roomImageMapper;

    public RoomImageServiceImpl(RoomImageRepository roomImageRepository, RoomImageMapper roomImageMapper) {
        this.roomImageRepository = roomImageRepository;
        this.roomImageMapper = roomImageMapper;
    }

    @Override
    public RoomImageDTO save(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to save RoomImage : {}", roomImageDTO);
        RoomImage roomImage = roomImageMapper.toEntity(roomImageDTO);
        roomImage = roomImageRepository.save(roomImage);
        return roomImageMapper.toDto(roomImage);
    }

    @Override
    public RoomImageDTO update(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to update RoomImage : {}", roomImageDTO);
        RoomImage roomImage = roomImageMapper.toEntity(roomImageDTO);
        roomImage = roomImageRepository.save(roomImage);
        return roomImageMapper.toDto(roomImage);
    }

    @Override
    public Optional<RoomImageDTO> partialUpdate(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to partially update RoomImage : {}", roomImageDTO);

        return roomImageRepository
            .findById(roomImageDTO.getId())
            .map(existingRoomImage -> {
                roomImageMapper.partialUpdate(existingRoomImage, roomImageDTO);

                return existingRoomImage;
            })
            .map(roomImageRepository::save)
            .map(roomImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomImageDTO> findAll() {
        LOG.debug("Request to get all RoomImages");
        return roomImageRepository.findAll().stream().map(roomImageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomImageDTO> findOne(Long id) {
        LOG.debug("Request to get RoomImage : {}", id);
        return roomImageRepository.findById(id).map(roomImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete RoomImage : {}", id);
        roomImageRepository.deleteById(id);
    }
}
