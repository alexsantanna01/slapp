package com.slapp.repository;

import com.slapp.domain.Reservation;
import com.slapp.domain.enumeration.ReservationStatus;
import java.math.BigDecimal;
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

    /**
     * Conta o total de reservas confirmadas de um proprietário no mês atual
     */
    @Query(
        "SELECT COUNT(r) FROM Reservation r " +
        "JOIN r.room rm " +
        "JOIN rm.studio s " +
        "WHERE s.owner.id = :ownerId " +
        "AND r.status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED') " +
        "AND r.startDateTime >= :monthStart " +
        "AND r.startDateTime < :monthEnd"
    )
    Long countReservationsByOwnerAndCurrentMonth(
        @Param("ownerId") Long ownerId,
        @Param("monthStart") Instant monthStart,
        @Param("monthEnd") Instant monthEnd
    );

    /**
     * Soma a receita total de um proprietário no mês atual
     */
    @Query(
        "SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r " +
        "JOIN r.room rm " +
        "JOIN rm.studio s " +
        "WHERE s.owner.id = :ownerId " +
        "AND r.status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED') " +
        "AND r.startDateTime >= :monthStart " +
        "AND r.startDateTime < :monthEnd"
    )
    BigDecimal sumRevenueByOwnerAndCurrentMonth(
        @Param("ownerId") Long ownerId,
        @Param("monthStart") Instant monthStart,
        @Param("monthEnd") Instant monthEnd
    );

    /**
     * Calcula a taxa de ocupação dos estúdios de um proprietário no mês atual
     * Retorna o total de horas reservadas
     */
    @Query(
        "SELECT COALESCE(SUM(EXTRACT(EPOCH FROM r.endDateTime) - EXTRACT(EPOCH FROM r.startDateTime)), 0) / 3600.0 " +
        "FROM Reservation r " +
        "JOIN r.room rm " +
        "JOIN rm.studio s " +
        "WHERE s.owner.id = :ownerId " +
        "AND r.status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED') " +
        "AND r.startDateTime >= :monthStart " +
        "AND r.startDateTime < :monthEnd"
    )
    Double sumReservedHoursByOwnerAndCurrentMonth(
        @Param("ownerId") Long ownerId,
        @Param("monthStart") Instant monthStart,
        @Param("monthEnd") Instant monthEnd
    );
}
