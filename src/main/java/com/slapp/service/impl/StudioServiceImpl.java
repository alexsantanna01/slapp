package com.slapp.service.impl;

import com.slapp.domain.Studio;
import com.slapp.repository.StudioRepository;
import com.slapp.repository.projections.RoomImageProjection;
import com.slapp.repository.projections.RoomProjection;
import com.slapp.repository.projections.StudioBasicProjection;
import com.slapp.repository.projections.StudioDetailProjection;
import com.slapp.repository.projections.StudioListProjection;
import com.slapp.service.StudioService;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.StudioDetailDTO;
import com.slapp.service.dto.StudioFilterDTO;
import com.slapp.service.mapper.StudioMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<StudioListProjection> getStudioRoomPagination(Pageable pageable, StudioFilterDTO filters) {
        LOG.debug(
            "getStudioRoomPagination called with filters: start={}, end={}",
            filters.getAvailabilityStartDateTime(),
            filters.getAvailabilityEndDateTime()
        );

        // Frontend já envia em UTC, não precisa converter
        Instant startInstant = filters.getAvailabilityStartDateTime() != null
            ? filters.getAvailabilityStartDateTime().atZone(ZoneId.of("UTC")).toInstant()
            : null;
        Instant endInstant = filters.getAvailabilityEndDateTime() != null
            ? filters.getAvailabilityEndDateTime().atZone(ZoneId.of("UTC")).toInstant()
            : null;

        LOG.debug("Converted to Instants: start={}, end={}", startInstant, endInstant);

        return studioRepository.getStudioRoomPagination(
            filters.getName(),
            filters.getCity(),
            filters.getRoomType(),
            filters.getMinPrice(),
            filters.getMaxPrice(),
            startInstant,
            endInstant,
            pageable
        );
    }

    public List<StudioListProjection> findStudiosKeyset(
        String name,
        String city,
        String roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        LocalDateTime availabilityStartDateTime,
        LocalDateTime availabilityEndDateTime,
        Long lastId,
        int pageSize
    ) {
        // Frontend já envia em UTC, não precisa converter
        Instant startInstant = availabilityStartDateTime != null ? availabilityStartDateTime.atZone(ZoneId.of("UTC")).toInstant() : null;
        Instant endInstant = availabilityEndDateTime != null ? availabilityEndDateTime.atZone(ZoneId.of("UTC")).toInstant() : null;

        return studioRepository.getStudiosKeyset(name, city, roomType, minPrice, maxPrice, startInstant, endInstant, lastId, pageSize);
    }

    /**
     * Busca detalhes completos do Studio com Rooms e suas imagens
     * Implementação otimizada para evitar problema N+1
     */
    public Optional<StudioDetailProjection> getStudioDetail(Long id) {
        // 1. Busca os dados básicos do studio
        Optional<StudioBasicProjection> studioOpt = studioRepository.findStudioBasicDetailById(id);

        if (studioOpt.isEmpty()) {
            return Optional.empty();
        }

        StudioBasicProjection studioData = studioOpt.get();

        // 2. Busca as rooms do studio
        List<RoomProjection> rooms = studioRepository.findRoomsByStudioId(id);

        if (rooms.isEmpty()) {
            // Se não há rooms, retorna só os dados do studio
            return Optional.of(createStudioDTO(studioData, rooms));
        }

        // 3. Busca todas as imagens das rooms de uma vez
        List<Long> roomIds = rooms.stream().map(RoomProjection::getId).collect(Collectors.toList());

        List<RoomImageProjection> images = studioRepository.findImagesByRoomIds(roomIds);

        // 4. Agrupa as imagens por room
        Map<Long, List<RoomImageProjection>> imagesByRoom = images.stream().collect(Collectors.groupingBy(RoomImageProjection::getRoomId));

        // 5. Associa as imagens às rooms
        return Optional.of(createStudioDTO(studioData, rooms, imagesByRoom));
    }

    /**
     * Cria um DTO completo do Studio com todas as relações
     */
    private StudioDetailProjection createStudioDTO(StudioBasicProjection studioData, List<RoomProjection> rooms) {
        return createStudioDTO(studioData, rooms, Map.of());
    }

    /**
     * Cria um DTO completo do Studio com todas as relações
     */
    private StudioDetailProjection createStudioDTO(
        StudioBasicProjection studioData,
        List<RoomProjection> rooms,
        Map<Long, List<RoomImageProjection>> imagesByRoom
    ) {
        // Cria o DTO do studio
        StudioDetailDTO studioDTO = new StudioDetailDTO();

        // Mapeia dados básicos do studio
        studioDTO.setId(studioData.getId());
        studioDTO.setName(studioData.getName());
        studioDTO.setDescription(studioData.getDescription());
        studioDTO.setAddress(studioData.getAddress());
        studioDTO.setCity(studioData.getCity());
        studioDTO.setState(studioData.getState());
        studioDTO.setZipCode(studioData.getZipCode());
        studioDTO.setLatitude(studioData.getLatitude());
        studioDTO.setLongitude(studioData.getLongitude());
        studioDTO.setPhone(studioData.getPhone());
        studioDTO.setEmail(studioData.getEmail());
        studioDTO.setWebsite(studioData.getWebsite());
        studioDTO.setImage(studioData.getImage());
        studioDTO.setActive(studioData.getActive());
        studioDTO.setCreatedAt(studioData.getCreatedAt());
        studioDTO.setUpdatedAt(studioData.getUpdatedAt());

        // Mapeia owner se existir
        if (studioData.getOwnerId() != null) {
            StudioDetailDTO.OwnerDTO ownerDTO = new StudioDetailDTO.OwnerDTO();
            ownerDTO.setId(studioData.getOwnerId());
            studioDTO.setOwner(ownerDTO);
        }

        // Mapeia rooms e suas imagens
        List<StudioDetailDTO.RoomDTO> roomDTOs = rooms
            .stream()
            .map(room -> createRoomDTO(room, imagesByRoom.getOrDefault(room.getId(), List.of())))
            .collect(Collectors.toList());

        studioDTO.setRooms(roomDTOs);

        return studioDTO;
    }

    /**
     * Cria um DTO da Room com suas imagens
     */
    private StudioDetailDTO.RoomDTO createRoomDTO(RoomProjection roomData, List<RoomImageProjection> images) {
        StudioDetailDTO.RoomDTO roomDTO = new StudioDetailDTO.RoomDTO();

        // Mapeia dados da room
        roomDTO.setId(roomData.getId());
        roomDTO.setName(roomData.getName());
        roomDTO.setDescription(roomData.getDescription());
        roomDTO.setHourlyRate(roomData.getHourlyRate());
        roomDTO.setCapacity(roomData.getCapacity());
        roomDTO.setSoundproofed(roomData.getSoundproofed());
        roomDTO.setAirConditioning(roomData.getAirConditioning());
        roomDTO.setRoomType(roomData.getRoomType());
        roomDTO.setActive(roomData.getActive());
        roomDTO.setCreatedAt(roomData.getCreatedAt());
        roomDTO.setUpdatedAt(roomData.getUpdatedAt());

        // Mapeia imagens da room
        List<StudioDetailDTO.RoomImageDTO> imageDTOs = images.stream().map(this::createRoomImageDTO).collect(Collectors.toList());

        roomDTO.setRoomImages(imageDTOs);

        return roomDTO;
    }

    /**
     * Cria um DTO da RoomImage
     */
    private StudioDetailDTO.RoomImageDTO createRoomImageDTO(RoomImageProjection imageData) {
        StudioDetailDTO.RoomImageDTO imageDTO = new StudioDetailDTO.RoomImageDTO();

        imageDTO.setId(imageData.getId());
        imageDTO.setUrl(imageData.getUrl());
        imageDTO.setAltText(imageData.getAltText());
        imageDTO.setDisplayOrder(imageData.getDisplayOrder());
        imageDTO.setActive(imageData.getActive());
        imageDTO.setRoomId(imageData.getRoomId());

        return imageDTO;
    }
}
