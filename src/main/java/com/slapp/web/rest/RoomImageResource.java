package com.slapp.web.rest;

import com.slapp.repository.RoomImageRepository;
import com.slapp.service.RoomImageService;
import com.slapp.service.dto.RoomImageDTO;
import com.slapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.slapp.domain.RoomImage}.
 */
@RestController
@RequestMapping("/api/room-images")
public class RoomImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoomImageResource.class);

    private static final String ENTITY_NAME = "roomImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomImageService roomImageService;

    private final RoomImageRepository roomImageRepository;

    public RoomImageResource(RoomImageService roomImageService, RoomImageRepository roomImageRepository) {
        this.roomImageService = roomImageService;
        this.roomImageRepository = roomImageRepository;
    }

    /**
     * {@code POST  /room-images} : Create a new roomImage.
     *
     * @param roomImageDTO the roomImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomImageDTO, or with status {@code 400 (Bad Request)} if the roomImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoomImageDTO> createRoomImage(@Valid @RequestBody RoomImageDTO roomImageDTO) throws URISyntaxException {
        LOG.debug("REST request to save RoomImage : {}", roomImageDTO);
        if (roomImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roomImageDTO = roomImageService.save(roomImageDTO);
        return ResponseEntity.created(new URI("/api/room-images/" + roomImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roomImageDTO.getId().toString()))
            .body(roomImageDTO);
    }

    /**
     * {@code PUT  /room-images/:id} : Updates an existing roomImage.
     *
     * @param id the id of the roomImageDTO to save.
     * @param roomImageDTO the roomImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomImageDTO,
     * or with status {@code 400 (Bad Request)} if the roomImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomImageDTO> updateRoomImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoomImageDTO roomImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RoomImage : {}, {}", id, roomImageDTO);
        if (roomImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roomImageDTO = roomImageService.update(roomImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomImageDTO.getId().toString()))
            .body(roomImageDTO);
    }

    /**
     * {@code PATCH  /room-images/:id} : Partial updates given fields of an existing roomImage, field will ignore if it is null
     *
     * @param id the id of the roomImageDTO to save.
     * @param roomImageDTO the roomImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomImageDTO,
     * or with status {@code 400 (Bad Request)} if the roomImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roomImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomImageDTO> partialUpdateRoomImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoomImageDTO roomImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RoomImage partially : {}, {}", id, roomImageDTO);
        if (roomImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomImageDTO> result = roomImageService.partialUpdate(roomImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /room-images} : get all the roomImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomImages in body.
     */
    @GetMapping("")
    public List<RoomImageDTO> getAllRoomImages() {
        LOG.debug("REST request to get all RoomImages");
        return roomImageService.findAll();
    }

    /**
     * {@code GET  /room-images/:id} : get the "id" roomImage.
     *
     * @param id the id of the roomImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomImageDTO> getRoomImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RoomImage : {}", id);
        Optional<RoomImageDTO> roomImageDTO = roomImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomImageDTO);
    }

    /**
     * {@code DELETE  /room-images/:id} : delete the "id" roomImage.
     *
     * @param id the id of the roomImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RoomImage : {}", id);
        roomImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET /room-images} : get all the roomImages by roomId.
     *
     * @param roomId the id of the room to retrieve roomImages from.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomImages in body.
     */
    @GetMapping(params = "roomId.equals")
    public List<RoomImageDTO> getEntitiesByRoomId(@RequestParam("roomId.equals") Long roomId) {
        LOG.debug("REST request to get all RoomImages by RoomId : {}", roomId);
        return roomImageService.findByRoomId(roomId);
    }
}
