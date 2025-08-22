package com.slapp.web.rest;

import com.slapp.repository.ReservationRepository;
import com.slapp.service.ReservationQueryService;
import com.slapp.service.ReservationService;
import com.slapp.service.criteria.ReservationCriteria;
import com.slapp.service.dto.ReservationDTO;
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
 * REST controller for managing {@link com.slapp.domain.Reservation}.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationResource.class);

    private static final String ENTITY_NAME = "reservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationService reservationService;

    private final ReservationRepository reservationRepository;

    private final ReservationQueryService reservationQueryService;

    public ReservationResource(
        ReservationService reservationService,
        ReservationRepository reservationRepository,
        ReservationQueryService reservationQueryService
    ) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationQueryService = reservationQueryService;
    }

    /**
     * {@code POST  /reservations} : Create a new reservation.
     *
     * @param reservationDTO the reservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservationDTO, or with status {@code 400 (Bad Request)} if the reservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Reservation : {}", reservationDTO);
        if (reservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new reservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reservationDTO = reservationService.save(reservationDTO);
        return ResponseEntity.created(new URI("/api/reservations/" + reservationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reservationDTO.getId().toString()))
            .body(reservationDTO);
    }

    /**
     * {@code PUT  /reservations/:id} : Updates an existing reservation.
     *
     * @param id the id of the reservationDTO to save.
     * @param reservationDTO the reservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationDTO,
     * or with status {@code 400 (Bad Request)} if the reservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservationDTO reservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reservation : {}, {}", id, reservationDTO);
        if (reservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reservationDTO = reservationService.update(reservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservationDTO.getId().toString()))
            .body(reservationDTO);
    }

    /**
     * {@code PATCH  /reservations/:id} : Partial updates given fields of an existing reservation, field will ignore if it is null
     *
     * @param id the id of the reservationDTO to save.
     * @param reservationDTO the reservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationDTO,
     * or with status {@code 400 (Bad Request)} if the reservationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reservationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservationDTO> partialUpdateReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservationDTO reservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reservation partially : {}, {}", id, reservationDTO);
        if (reservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservationDTO> result = reservationService.partialUpdate(reservationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reservations} : get all the reservations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReservationDTO>> getAllReservations(
        ReservationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Reservations by criteria: {}", criteria);

        Page<ReservationDTO> page = reservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reservations/count} : count all the reservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReservations(ReservationCriteria criteria) {
        LOG.debug("REST request to count Reservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(reservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reservations/:id} : get the "id" reservation.
     *
     * @param id the id of the reservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reservation : {}", id);
        Optional<ReservationDTO> reservationDTO = reservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservationDTO);
    }

    /**
     * {@code DELETE  /reservations/:id} : delete the "id" reservation.
     *
     * @param id the id of the reservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reservation : {}", id);
        reservationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /reservations/room/{roomId}/availability} : get availability for a room on a specific date.
     *
     * @param roomId the id of the room to check availability.
     * @param date the date to check (format: yyyy-mm-dd).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the availability data.
     */
    @GetMapping("/room/{roomId}/availability")
    public ResponseEntity<List<ReservationDTO>> getRoomAvailability(
        @PathVariable("roomId") Long roomId,
        @RequestParam("date") String date
    ) {
        LOG.debug("REST request to get room availability for room: {} on date: {}", roomId, date);
        List<ReservationDTO> reservations = reservationService.findReservationsByRoomAndDate(roomId, date);
        return ResponseEntity.ok().body(reservations);
    }

    /**
     * {@code POST  /reservations/{id}/approve} : approve a pending reservation.
     *
     * @param id the id of the reservation to approve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationDTO.
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<ReservationDTO> approveReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to approve Reservation : {}", id);
        Optional<ReservationDTO> result = reservationService.approveReservation(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * {@code POST  /reservations/{id}/reject} : reject a pending reservation.
     *
     * @param id the id of the reservation to reject.
     * @param reason the reason for rejection.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationDTO.
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<ReservationDTO> rejectReservation(
        @PathVariable("id") Long id,
        @RequestParam(value = "reason", required = false) String reason
    ) {
        LOG.debug("REST request to reject Reservation : {} with reason: {}", id, reason);
        Optional<ReservationDTO> result = reservationService.rejectReservation(id, reason);
        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * {@code GET  /reservations/studio/{studioId}/pending} : get pending reservations for a studio.
     *
     * @param studioId the id of the studio.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of pending reservations.
     */
    @GetMapping("/studio/{studioId}/pending")
    public ResponseEntity<List<ReservationDTO>> getPendingReservationsForStudio(@PathVariable("studioId") Long studioId) {
        LOG.debug("REST request to get pending reservations for studio: {}", studioId);
        List<ReservationDTO> pendingReservations = reservationService.findPendingReservationsByStudio(studioId);
        return ResponseEntity.ok().body(pendingReservations);
    }

    /**
     * {@code GET  /reservations/room/{roomId}/all} : get all reservations for a room.
     *
     * @param roomId the id of the room.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of all reservations.
     */
    @GetMapping("/room/{roomId}/all")
    public ResponseEntity<List<ReservationDTO>> getAllReservationsForRoom(@PathVariable("roomId") Long roomId) {
        LOG.debug("REST request to get all reservations for room: {}", roomId);
        List<ReservationDTO> allReservations = reservationService.findAllReservationsByRoom(roomId);
        return ResponseEntity.ok().body(allReservations);
    }
}
