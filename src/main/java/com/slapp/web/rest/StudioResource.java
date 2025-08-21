package com.slapp.web.rest;

import com.slapp.repository.StudioRepository;
import com.slapp.repository.projections.StudioDetailProjection;
import com.slapp.repository.projections.StudioListProjection;
import com.slapp.service.StudioQueryService;
import com.slapp.service.StudioService;
import com.slapp.service.criteria.StudioCriteria;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.StudioFilterDTO;
import com.slapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.slapp.domain.Studio}.
 */
@RestController
@RequestMapping("/api/studios")
public class StudioResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudioResource.class);

    private static final String ENTITY_NAME = "studio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudioService studioService;

    private final StudioRepository studioRepository;

    private final StudioQueryService studioQueryService;

    public StudioResource(StudioService studioService, StudioRepository studioRepository, StudioQueryService studioQueryService) {
        this.studioService = studioService;
        this.studioRepository = studioRepository;
        this.studioQueryService = studioQueryService;
    }

    /**
     * {@code POST  /studios} : Create a new studio.
     *
     * @param studioDTO the studioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studioDTO, or with status {@code 400 (Bad Request)} if the studio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudioDTO> createStudio(@Valid @RequestBody StudioDTO studioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Studio : {}", studioDTO);
        if (studioDTO.getId() != null) {
            throw new BadRequestAlertException("A new studio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studioDTO = studioService.save(studioDTO);
        return ResponseEntity.created(new URI("/api/studios/" + studioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studioDTO.getId().toString()))
            .body(studioDTO);
    }

    /**
     * {@code PUT  /studios/:id} : Updates an existing studio.
     *
     * @param id the id of the studioDTO to save.
     * @param studioDTO the studioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studioDTO,
     * or with status {@code 400 (Bad Request)} if the studioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudioDTO> updateStudio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudioDTO studioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Studio : {}, {}", id, studioDTO);
        if (studioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studioDTO = studioService.update(studioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studioDTO.getId().toString()))
            .body(studioDTO);
    }

    /**
     * {@code PATCH  /studios/:id} : Partial updates given fields of an existing studio, field will ignore if it is null
     *
     * @param id the id of the studioDTO to save.
     * @param studioDTO the studioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studioDTO,
     * or with status {@code 400 (Bad Request)} if the studioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudioDTO> partialUpdateStudio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudioDTO studioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Studio partially : {}, {}", id, studioDTO);
        if (studioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudioDTO> result = studioService.partialUpdate(studioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /studios} : get all the studios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudioDTO>> getAllStudios(
        StudioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Studios by criteria: {}", criteria);

        Page<StudioDTO> page = studioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /studios/count} : count all the studios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStudios(StudioCriteria criteria) {
        LOG.debug("REST request to count Studios by criteria: {}", criteria);
        return ResponseEntity.ok().body(studioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /studios/:id} : get the "id" studio.
     *
     * @param id the id of the studioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studioDTO, or with status {@code 404 (Not Found)}.
     */
    // @GetMapping("/{id}")
    // public ResponseEntity<StudioDTO> getStudio(@PathVariable("id") Long id) {
    //     LOG.debug("REST request to get Studio : {}", id);
    //     Optional<StudioDTO> studioDTO = studioService.findOne(id);
    //     return ResponseUtil.wrapOrNotFound(studioDTO);
    // }

    /**
     * GET /api/studios/{id}/detail
     * Retorna os detalhes completos do studio com rooms e imagens
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudioDetailProjection> getStudioDetail(@PathVariable Long id) {
        Optional<StudioDetailProjection> studioDetail = studioService.getStudioDetail(id);

        return studioDetail.map(studio -> ResponseEntity.ok().body(studio)).orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /studios/:id} : delete the "id" studio.
     *
     * @param id the id of the studioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Studio : {}", id);
        studioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  } : get all the studios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studios in body.
     */
    @GetMapping("/pagination")
    public ResponseEntity<List<StudioListProjection>> getAllStudiosPagination(
        Pageable pageable,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String roomType,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        StudioFilterDTO filters = StudioFilterDTO.builder()
            .name(name)
            .city(city)
            .roomType(roomType)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .build();

        final Page<StudioListProjection> page = studioService.getStudioRoomPagination(pageable, filters);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
