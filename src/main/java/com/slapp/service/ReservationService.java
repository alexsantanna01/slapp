package com.slapp.service;

import com.slapp.service.dto.ReservationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.slapp.domain.Reservation}.
 */
public interface ReservationService {
    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationDTO save(ReservationDTO reservationDTO);

    /**
     * Updates a reservation.
     *
     * @param reservationDTO the entity to update.
     * @return the persisted entity.
     */
    ReservationDTO update(ReservationDTO reservationDTO);

    /**
     * Partially updates a reservation.
     *
     * @param reservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO);

    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationDTO> findOne(Long id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get reservations for a room on a specific date.
     *
     * @param roomId the room id.
     * @param date the date string (yyyy-mm-dd).
     * @return the list of reservations.
     */
    List<ReservationDTO> findReservationsByRoomAndDate(Long roomId, String date);

    /**
     * Approve a pending reservation.
     *
     * @param id the reservation id.
     * @return the updated reservation.
     */
    Optional<ReservationDTO> approveReservation(Long id);

    /**
     * Reject a pending reservation.
     *
     * @param id the reservation id.
     * @param reason the reason for rejection.
     * @return the updated reservation.
     */
    Optional<ReservationDTO> rejectReservation(Long id, String reason);

    /**
     * Get pending reservations for a studio.
     *
     * @param studioId the studio id.
     * @return the list of pending reservations.
     */
    List<ReservationDTO> findPendingReservationsByStudio(Long studioId);

    /**
     * Auto-confirm reservations that have been pending for more than 30 minutes.
     *
     * @return the number of reservations auto-confirmed.
     */
    int autoConfirmExpiredPendingReservations();

    /**
     * Get all reservations for a room (for debugging).
     *
     * @param roomId the room id.
     * @return the list of all reservations.
     */
    List<ReservationDTO> findAllReservationsByRoom(Long roomId);
}
