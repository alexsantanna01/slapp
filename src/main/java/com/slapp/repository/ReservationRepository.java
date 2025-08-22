package com.slapp.repository;

import com.slapp.domain.Reservation;
import com.slapp.domain.enumeration.ReservationStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    @Query(
        "SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
        "AND r.status IN (:activeStatuses) " +
        "AND ((r.startDateTime <= :endDateTime AND r.endDateTime >= :startDateTime))"
    )
    List<Reservation> findConflictingReservations(
        @Param("roomId") Long roomId,
        @Param("startDateTime") Instant startDateTime,
        @Param("endDateTime") Instant endDateTime,
        @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );

    @Query(
        "SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
        "AND r.status IN (:activeStatuses) " +
        "AND r.startDateTime < :endOfDay " +
        "AND r.endDateTime > :startOfDay"
    )
    List<Reservation> findReservationsByRoomAndDate(
        @Param("roomId") Long roomId,
        @Param("startOfDay") Instant startOfDay,
        @Param("endOfDay") Instant endOfDay,
        @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );

    @Query(
        "SELECT r FROM Reservation r " +
        "JOIN FETCH r.room rm " +
        "JOIN FETCH rm.studio s " +
        "JOIN FETCH r.customer c " +
        "LEFT JOIN FETCH c.user u " +
        "WHERE s.id = :studioId AND r.status = 'PENDING' " +
        "ORDER BY r.createdAt ASC"
    )
    List<Reservation> findPendingReservationsByStudio(@Param("studioId") Long studioId);

    @Query("SELECT r FROM Reservation r " + "WHERE r.status = 'PENDING' AND r.createdAt <= :cutoffTime " + "ORDER BY r.createdAt ASC")
    List<Reservation> findExpiredPendingReservations(@Param("cutoffTime") Instant cutoffTime);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId ORDER BY r.startDateTime ASC")
    List<Reservation> findAllByRoomId(@Param("roomId") Long roomId);
}
