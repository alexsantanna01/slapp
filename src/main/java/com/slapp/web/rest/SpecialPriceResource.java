package com.slapp.web.rest;

import com.slapp.repository.SpecialPriceRepository;
import com.slapp.service.SpecialPriceService;
import com.slapp.service.dto.SpecialPriceDTO;
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
 * REST controller for managing {@link com.slapp.domain.SpecialPrice}.
 */
@RestController
@RequestMapping("/api/special-prices")
public class SpecialPriceResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialPriceResource.class);

    private static final String ENTITY_NAME = "specialPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialPriceService specialPriceService;

    private final SpecialPriceRepository specialPriceRepository;

    public SpecialPriceResource(SpecialPriceService specialPriceService, SpecialPriceRepository specialPriceRepository) {
        this.specialPriceService = specialPriceService;
        this.specialPriceRepository = specialPriceRepository;
    }

    /**
     * {@code POST  /special-prices} : Create a new specialPrice.
     *
     * @param specialPriceDTO the specialPriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialPriceDTO, or with status {@code 400 (Bad Request)} if the specialPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialPriceDTO> createSpecialPrice(@Valid @RequestBody SpecialPriceDTO specialPriceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SpecialPrice : {}", specialPriceDTO);
        if (specialPriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        specialPriceDTO = specialPriceService.save(specialPriceDTO);
        return ResponseEntity.created(new URI("/api/special-prices/" + specialPriceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, specialPriceDTO.getId().toString()))
            .body(specialPriceDTO);
    }

    /**
     * {@code PUT  /special-prices/:id} : Updates an existing specialPrice.
     *
     * @param id the id of the specialPriceDTO to save.
     * @param specialPriceDTO the specialPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialPriceDTO,
     * or with status {@code 400 (Bad Request)} if the specialPriceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialPriceDTO> updateSpecialPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialPriceDTO specialPriceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SpecialPrice : {}, {}", id, specialPriceDTO);
        if (specialPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        specialPriceDTO = specialPriceService.update(specialPriceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialPriceDTO.getId().toString()))
            .body(specialPriceDTO);
    }

    /**
     * {@code PATCH  /special-prices/:id} : Partial updates given fields of an existing specialPrice, field will ignore if it is null
     *
     * @param id the id of the specialPriceDTO to save.
     * @param specialPriceDTO the specialPriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialPriceDTO,
     * or with status {@code 400 (Bad Request)} if the specialPriceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialPriceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialPriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialPriceDTO> partialUpdateSpecialPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialPriceDTO specialPriceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SpecialPrice partially : {}, {}", id, specialPriceDTO);
        if (specialPriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialPriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialPriceDTO> result = specialPriceService.partialUpdate(specialPriceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialPriceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /special-prices} : get all the specialPrices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialPrices in body.
     */
    @GetMapping("")
    public List<SpecialPriceDTO> getAllSpecialPrices() {
        LOG.debug("REST request to get all SpecialPrices");
        return specialPriceService.findAll();
    }

    /**
     * {@code GET  /special-prices/:id} : get the "id" specialPrice.
     *
     * @param id the id of the specialPriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialPriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialPriceDTO> getSpecialPrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SpecialPrice : {}", id);
        Optional<SpecialPriceDTO> specialPriceDTO = specialPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialPriceDTO);
    }

    /**
     * {@code DELETE  /special-prices/:id} : delete the "id" specialPrice.
     *
     * @param id the id of the specialPriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialPrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SpecialPrice : {}", id);
        specialPriceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
