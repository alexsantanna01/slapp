package com.slapp.web.rest;

import com.slapp.repository.StudioOperatingHoursRepository;
import com.slapp.service.StudioOperatingHoursService;
import com.slapp.service.dto.StudioOperatingHoursDTO;
import com.slapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.slapp.domain.StudioOperatingHours}.
 */
@RestController
@RequestMapping("/api/studio-operating-hours")
public class StudioOperatingHoursResource {

    private static final Logger log = LoggerFactory.getLogger(StudioOperatingHoursResource.class);

    private static final String ENTITY_NAME = "studioOperatingHours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudioOperatingHoursService studioOperatingHoursService;

    private final StudioOperatingHoursRepository studioOperatingHoursRepository;

    public StudioOperatingHoursResource(
        StudioOperatingHoursService studioOperatingHoursService,
        StudioOperatingHoursRepository studioOperatingHoursRepository
    ) {
        this.studioOperatingHoursService = studioOperatingHoursService;
        this.studioOperatingHoursRepository = studioOperatingHoursRepository;
    }

    /**
     * {@code POST  /studio-operating-hours} : Create a new studioOperatingHours.
     *
     * @param studioOperatingHoursDTO the studioOperatingHoursDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studioOperatingHoursDTO, or with status {@code 400 (Bad Request)} if the studioOperatingHours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudioOperatingHoursDTO> createStudioOperatingHours(
        @Valid @RequestBody StudioOperatingHoursDTO studioOperatingHoursDTO
    ) throws URISyntaxException {
        log.debug("REST request to save StudioOperatingHours : {}", studioOperatingHoursDTO);
        if (studioOperatingHoursDTO.getId() != null) {
            throw new BadRequestAlertException("A new studioOperatingHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studioOperatingHoursDTO = studioOperatingHoursService.save(studioOperatingHoursDTO);
        return ResponseEntity.created(new URI("/api/studio-operating-hours/" + studioOperatingHoursDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studioOperatingHoursDTO.getId().toString()))
            .body(studioOperatingHoursDTO);
    }

    /**
     * {@code PUT  /studio-operating-hours/:id} : Updates an existing studioOperatingHours.
     *
     * @param id the id of the studioOperatingHoursDTO to save.
     * @param studioOperatingHoursDTO the studioOperatingHoursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studioOperatingHoursDTO,
     * or with status {@code 400 (Bad Request)} if the studioOperatingHoursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studioOperatingHoursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudioOperatingHoursDTO> updateStudioOperatingHours(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudioOperatingHoursDTO studioOperatingHoursDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudioOperatingHours : {}, {}", id, studioOperatingHoursDTO);
        if (studioOperatingHoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studioOperatingHoursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studioOperatingHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studioOperatingHoursDTO = studioOperatingHoursService.update(studioOperatingHoursDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studioOperatingHoursDTO.getId().toString()))
            .body(studioOperatingHoursDTO);
    }

    /**
     * {@code PATCH  /studio-operating-hours/:id} : Partial updates given fields of an existing studioOperatingHours, field will ignore if it is null
     *
     * @param id the id of the studioOperatingHoursDTO to save.
     * @param studioOperatingHoursDTO the studioOperatingHoursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studioOperatingHoursDTO,
     * or with status {@code 404 (Not Found)} if the studioOperatingHoursDTO is not found,
     * or with status {@code 400 (Bad Request)} if the studioOperatingHoursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studioOperatingHoursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudioOperatingHoursDTO> partialUpdateStudioOperatingHours(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudioOperatingHoursDTO studioOperatingHoursDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudioOperatingHours partially : {}, {}", id, studioOperatingHoursDTO);
        if (studioOperatingHoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studioOperatingHoursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studioOperatingHoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudioOperatingHoursDTO> result = studioOperatingHoursService.partialUpdate(studioOperatingHoursDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studioOperatingHoursDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /studio-operating-hours} : get all the studioOperatingHours.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studioOperatingHours in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudioOperatingHoursDTO>> getAllStudioOperatingHours(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of StudioOperatingHours");
        Page<StudioOperatingHoursDTO> page = studioOperatingHoursService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /studio-operating-hours/:id} : get the "id" studioOperatingHours.
     *
     * @param id the id of the studioOperatingHoursDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studioOperatingHoursDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudioOperatingHoursDTO> getStudioOperatingHours(@PathVariable("id") Long id) {
        log.debug("REST request to get StudioOperatingHours : {}", id);
        Optional<StudioOperatingHoursDTO> studioOperatingHoursDTO = studioOperatingHoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studioOperatingHoursDTO);
    }

    /**
     * {@code DELETE  /studio-operating-hours/:id} : delete the "id" studioOperatingHours.
     *
     * @param id the id of the studioOperatingHoursDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudioOperatingHours(@PathVariable("id") Long id) {
        log.debug("REST request to delete StudioOperatingHours : {}", id);
        studioOperatingHoursService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /studio-operating-hours/studio/:studioId} : get operating hours for a studio.
     *
     * @param studioId the id of the studio.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of operating hours.
     */
    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<StudioOperatingHoursDTO>> getStudioOperatingHoursByStudio(@PathVariable("studioId") Long studioId) {
        log.debug("REST request to get StudioOperatingHours for studio : {}", studioId);
        List<StudioOperatingHoursDTO> operatingHours = studioOperatingHoursService.findByStudioId(studioId);
        return ResponseEntity.ok().body(operatingHours);
    }

    /**
     * {@code GET  /studio-operating-hours/studio/:studioId/day/:dayOfWeek} : get operating hours for a studio on a specific day.
     *
     * @param studioId the id of the studio.
     * @param dayOfWeek the day of the week.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operating hours for that day.
     */
    @GetMapping("/studio/{studioId}/day/{dayOfWeek}")
    public ResponseEntity<List<StudioOperatingHoursDTO>> getStudioOperatingHoursByStudioAndDay(
        @PathVariable("studioId") Long studioId,
        @PathVariable("dayOfWeek") DayOfWeek dayOfWeek
    ) {
        log.debug("REST request to get StudioOperatingHours for studio {} on {}", studioId, dayOfWeek);
        List<StudioOperatingHoursDTO> operatingHours = studioOperatingHoursService.findByStudioIdAndDayOfWeek(studioId, dayOfWeek);
        return ResponseEntity.ok().body(operatingHours);
    }

    /**
     * {@code PUT  /studio-operating-hours/studio/:studioId} : Update operating hours for a studio.
     *
     * @param studioId the id of the studio.
     * @param operatingHours the list of operating hours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operating hours.
     */
    @PutMapping("/studio/{studioId}")
    public ResponseEntity<List<StudioOperatingHoursDTO>> updateStudioOperatingHours(
        @PathVariable("studioId") Long studioId,
        @Valid @RequestBody List<StudioOperatingHoursDTO> operatingHours
    ) {
        log.debug("REST request to update operating hours for studio : {}", studioId);
        List<StudioOperatingHoursDTO> result = studioOperatingHoursService.updateStudioOperatingHours(studioId, operatingHours);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Operating hours updated for studio " + studioId, studioId.toString()))
            .body(result);
    }
}
