package com.slapp.service.impl;

import com.slapp.domain.Room;
import com.slapp.domain.Studio;
import com.slapp.repository.RoomRepository;
import com.slapp.repository.StudioRepository;
import com.slapp.service.RoomService;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.mapper.RoomMapper;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.Room}.
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;

    private final StudioRepository studioRepository;

    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository, StudioRepository studioRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.studioRepository = studioRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public RoomDTO save(RoomDTO roomDTO) {
        LOG.debug("Request to save Room : {}", roomDTO);
        try {
            // Validar se o studio foi informado
            if (roomDTO.getStudio() == null || roomDTO.getStudio().getId() == null) {
                throw new RuntimeException("Studio ID is required");
            }

            Room room = roomMapper.toEntity(roomDTO);

            // Garantir que o Studio seja uma entidade gerenciada
            if (room.getStudio() != null && room.getStudio().getId() != null) {
                final Long studioId = room.getStudio().getId();
                Studio studio = studioRepository
                    .findById(studioId)
                    .orElseThrow(() -> new RuntimeException("Studio not found with id: " + studioId));
                room.setStudio(studio);
            } else {
                // Fallback: usar o ID do DTO se o mapper não funcionou
                final Long studioId = roomDTO.getStudio().getId();
                Studio studio = studioRepository
                    .findById(studioId)
                    .orElseThrow(() -> new RuntimeException("Studio not found with id: " + studioId));
                room.setStudio(studio);
            }

            room = roomRepository.save(room);
            return roomMapper.toDto(room);
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
    public RoomDTO update(RoomDTO roomDTO) {
        LOG.debug("Request to update Room : {}", roomDTO);
        try {
            Room room = roomMapper.toEntity(roomDTO);

            // Se o studio está vindo com apenas o ID, buscar do banco
            if (room.getStudio() != null && room.getStudio().getId() != null) {
                final Long studioId = room.getStudio().getId();
                Studio studio = studioRepository
                    .findById(studioId)
                    .orElseThrow(() -> new RuntimeException("Studio not found with id: " + studioId));
                room.setStudio(studio);
            }

            room = roomRepository.save(room);
            return roomMapper.toDto(room);
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
    public Optional<RoomDTO> partialUpdate(RoomDTO roomDTO) {
        LOG.debug("Request to partially update Room : {}", roomDTO);

        return roomRepository
            .findById(roomDTO.getId())
            .map(existingRoom -> {
                roomMapper.partialUpdate(existingRoom, roomDTO);

                // Se o studio está vindo com apenas o ID, buscar do banco
                if (existingRoom.getStudio() != null && existingRoom.getStudio().getId() != null) {
                    final Long studioId = existingRoom.getStudio().getId();
                    Studio studio = studioRepository
                        .findById(studioId)
                        .orElseThrow(() -> new RuntimeException("Studio not found with id: " + studioId));
                    existingRoom.setStudio(studio);
                }

                return existingRoom;
            })
            .map(roomRepository::save)
            .map(roomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDTO> findOne(Long id) {
        LOG.debug("Request to get Room : {}", id);
        return roomRepository.findById(id).map(roomMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Room : {}", id);
        roomRepository.deleteById(id);
    }
}
