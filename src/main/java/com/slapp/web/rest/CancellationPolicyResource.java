package com.slapp.web.rest;

import com.slapp.repository.CancellationPolicyRepository;
import com.slapp.service.CancellationPolicyService;
import com.slapp.service.dto.CancellationPolicyDTO;
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
 * REST controller for managing {@link com.slapp.domain.CancellationPolicy}.
 */
@RestController
@RequestMapping("/api/cancellation-policies")
public class CancellationPolicyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CancellationPolicyResource.class);

    private static final String ENTITY_NAME = "cancellationPolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CancellationPolicyService cancellationPolicyService;

    private final CancellationPolicyRepository cancellationPolicyRepository;

    public CancellationPolicyResource(
        CancellationPolicyService cancellationPolicyService,
        CancellationPolicyRepository cancellationPolicyRepository
    ) {
        this.cancellationPolicyService = cancellationPolicyService;
        this.cancellationPolicyRepository = cancellationPolicyRepository;
    }

    /**
     * {@code POST  /cancellation-policies} : Create a new cancellationPolicy.
     *
     * @param cancellationPolicyDTO the cancellationPolicyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cancellationPolicyDTO, or with status {@code 400 (Bad Request)} if the cancellationPolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CancellationPolicyDTO> createCancellationPolicy(@Valid @RequestBody CancellationPolicyDTO cancellationPolicyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CancellationPolicy : {}", cancellationPolicyDTO);
        if (cancellationPolicyDTO.getId() != null) {
            throw new BadRequestAlertException("A new cancellationPolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cancellationPolicyDTO = cancellationPolicyService.save(cancellationPolicyDTO);
        return ResponseEntity.created(new URI("/api/cancellation-policies/" + cancellationPolicyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cancellationPolicyDTO.getId().toString()))
            .body(cancellationPolicyDTO);
    }

    /**
     * {@code PUT  /cancellation-policies/:id} : Updates an existing cancellationPolicy.
     *
     * @param id the id of the cancellationPolicyDTO to save.
     * @param cancellationPolicyDTO the cancellationPolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cancellationPolicyDTO,
     * or with status {@code 400 (Bad Request)} if the cancellationPolicyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cancellationPolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CancellationPolicyDTO> updateCancellationPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CancellationPolicyDTO cancellationPolicyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CancellationPolicy : {}, {}", id, cancellationPolicyDTO);
        if (cancellationPolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cancellationPolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cancellationPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cancellationPolicyDTO = cancellationPolicyService.update(cancellationPolicyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cancellationPolicyDTO.getId().toString()))
            .body(cancellationPolicyDTO);
    }

    /**
     * {@code PATCH  /cancellation-policies/:id} : Partial updates given fields of an existing cancellationPolicy, field will ignore if it is null
     *
     * @param id the id of the cancellationPolicyDTO to save.
     * @param cancellationPolicyDTO the cancellationPolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cancellationPolicyDTO,
     * or with status {@code 400 (Bad Request)} if the cancellationPolicyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cancellationPolicyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cancellationPolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CancellationPolicyDTO> partialUpdateCancellationPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CancellationPolicyDTO cancellationPolicyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CancellationPolicy partially : {}, {}", id, cancellationPolicyDTO);
        if (cancellationPolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cancellationPolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cancellationPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CancellationPolicyDTO> result = cancellationPolicyService.partialUpdate(cancellationPolicyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cancellationPolicyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cancellation-policies} : get all the cancellationPolicies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cancellationPolicies in body.
     */
    @GetMapping("")
    public List<CancellationPolicyDTO> getAllCancellationPolicies() {
        LOG.debug("REST request to get all CancellationPolicies");
        return cancellationPolicyService.findAll();
    }

    /**
     * {@code GET  /cancellation-policies/:id} : get the "id" cancellationPolicy.
     *
     * @param id the id of the cancellationPolicyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cancellationPolicyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CancellationPolicyDTO> getCancellationPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CancellationPolicy : {}", id);
        Optional<CancellationPolicyDTO> cancellationPolicyDTO = cancellationPolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cancellationPolicyDTO);
    }

    /**
     * {@code DELETE  /cancellation-policies/:id} : delete the "id" cancellationPolicy.
     *
     * @param id the id of the cancellationPolicyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCancellationPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CancellationPolicy : {}", id);
        cancellationPolicyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
