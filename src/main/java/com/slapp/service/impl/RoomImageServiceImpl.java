package com.slapp.service.impl;

import com.slapp.domain.Room;
import com.slapp.domain.RoomImage;
import com.slapp.repository.RoomImageRepository;
import com.slapp.repository.RoomRepository;
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

    private final RoomRepository roomRepository;

    private final RoomImageMapper roomImageMapper;

    public RoomImageServiceImpl(RoomImageRepository roomImageRepository, RoomRepository roomRepository, RoomImageMapper roomImageMapper) {
        this.roomImageRepository = roomImageRepository;
        this.roomRepository = roomRepository;
        this.roomImageMapper = roomImageMapper;
    }

    @Override
    public RoomImageDTO save(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to save RoomImage : {}", roomImageDTO);
        try {
            // Validar se o room foi informado
            if (roomImageDTO.getRoom() == null || roomImageDTO.getRoom().getId() == null) {
                throw new RuntimeException("Room ID is required");
            }

            RoomImage roomImage = roomImageMapper.toEntity(roomImageDTO);

            // Garantir que o Room seja uma entidade gerenciada
            if (roomImage.getRoom() != null && roomImage.getRoom().getId() != null) {
                final Long roomId = roomImage.getRoom().getId();
                Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
                roomImage.setRoom(room);
            } else {
                // Fallback: usar o ID do DTO se o mapper não funcionou
                final Long roomId = roomImageDTO.getRoom().getId();
                Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
                roomImage.setRoom(room);
            }

            roomImage = roomImageRepository.save(roomImage);
            return roomImageMapper.toDto(roomImage);
        } catch (Exception e) {
            LOG.error(
                "Exception in save() with cause = '{}' and exception = '{}'",
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e
            );
            throw e;
        }
    }

    @Override
    public RoomImageDTO update(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to update RoomImage : {}", roomImageDTO);
        try {
            RoomImage roomImage = roomImageMapper.toEntity(roomImageDTO);

            // Se o room está vindo com apenas o ID, buscar do banco
            if (roomImage.getRoom() != null && roomImage.getRoom().getId() != null) {
                final Long roomId = roomImage.getRoom().getId();
                Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
                roomImage.setRoom(room);
            }

            roomImage = roomImageRepository.save(roomImage);
            return roomImageMapper.toDto(roomImage);
        } catch (Exception e) {
            LOG.error(
                "Exception in update() with cause = '{}' and exception = '{}'",
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e
            );
            throw e;
        }
    }

    @Override
    public Optional<RoomImageDTO> partialUpdate(RoomImageDTO roomImageDTO) {
        LOG.debug("Request to partially update RoomImage : {}", roomImageDTO);

        return roomImageRepository
            .findById(roomImageDTO.getId())
            .map(existingRoomImage -> {
                roomImageMapper.partialUpdate(existingRoomImage, roomImageDTO);

                // Se o room está vindo com apenas o ID, buscar do banco
                if (existingRoomImage.getRoom() != null && existingRoomImage.getRoom().getId() != null) {
                    final Long roomId = existingRoomImage.getRoom().getId();
                    Room room = roomRepository
                        .findById(roomId)
                        .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
                    existingRoomImage.setRoom(room);
                }

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

    @Override
    public List<RoomImageDTO> findByRoomId(Long roomId) {
        LOG.debug("Request to get all RoomImages for Room : {}", roomId);
        return roomImageRepository
            .findByRoomIdOrderByDisplayOrderAsc(roomId)
            .stream()
            .map(roomImageMapper::toDto)
            .collect(Collectors.toList());
    }
}
