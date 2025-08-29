package com.slapp.repository;

import com.slapp.domain.Studio;
import com.slapp.domain.Studio;
import com.slapp.repository.projections.RoomImageProjection;
import com.slapp.repository.projections.RoomProjection;
import com.slapp.repository.projections.StudioBasicProjection;
import com.slapp.repository.projections.StudioDetailProjection;
import com.slapp.repository.projections.StudioListProjection;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Studio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudioRepository extends JpaRepository<Studio, Long>, JpaSpecificationExecutor<Studio> {
    // Query sem filtros de disponibilidade
    @Query(
        value = """
        SELECT DISTINCT
            s.id,
            s.name,
            s.description,
            s.address,
            s.city,
            s.state,
            s.image,
            (SELECT MIN(r2.hourly_rate)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as minPrice,
            (SELECT MAX(r2.hourly_rate)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as maxPrice,
            (SELECT COUNT(*)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as roomCount
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
        AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
        AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
        AND (:roomType IS NULL OR :roomType = '' OR
                (:roomType = 'RECORDING' AND r.room_type = 'RECORDING') OR
                (:roomType = 'REHEARSAL' AND r.room_type = 'REHEARSAL') OR
                (:roomType = 'LIVE' AND r.room_type = 'LIVE') OR
                (:roomType = 'MIXING' AND r.room_type = 'MIXING') OR
                (:roomType = 'BOTH' AND r.room_type IN ('RECORDING', 'REHEARSAL', 'LIVE', 'MIXING')))
        AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
        AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
        """,
        countQuery = """
        SELECT COUNT(DISTINCT s.id)
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
        AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
        AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
        AND (:roomType IS NULL OR :roomType = '' OR
            (r.room_type = :roomType))
        AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
        AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
        """,
        nativeQuery = true
    )
    Page<StudioListProjection> getStudioRoomPaginationBasic(
        @Param("name") String name,
        @Param("city") String city,
        @Param("roomType") String roomType,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable
    );

    // Query com filtros de disponibilidade
    @Query(
        value = """
        SELECT DISTINCT
            s.id,
            s.name,
            s.description,
            s.address,
            s.city,
            s.state,
            s.image,
            (SELECT MIN(r2.hourly_rate)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as minPrice,
            (SELECT MAX(r2.hourly_rate)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as maxPrice,
            (SELECT COUNT(*)
            FROM room r2
            WHERE r2.studio_id = s.id AND r2.active = true) as roomCount
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
        AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
        AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
        AND (:roomType IS NULL OR :roomType = '' OR
                (:roomType = 'RECORDING' AND r.room_type = 'RECORDING') OR
                (:roomType = 'REHEARSAL' AND r.room_type = 'REHEARSAL') OR
                (:roomType = 'LIVE' AND r.room_type = 'LIVE') OR
                (:roomType = 'MIXING' AND r.room_type = 'MIXING') OR
                (:roomType = 'BOTH' AND r.room_type IN ('RECORDING', 'REHEARSAL', 'LIVE', 'MIXING')))
        AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
        AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
        AND NOT EXISTS (
            SELECT 1 FROM reservation res
            WHERE res.room_id = r.id
            AND res.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
            AND (
                (res.start_date_time <= :availabilityStartDateTime AND res.end_date_time > :availabilityStartDateTime)
                OR (res.start_date_time < :availabilityEndDateTime AND res.end_date_time >= :availabilityEndDateTime)
                OR (res.start_date_time >= :availabilityStartDateTime AND res.end_date_time <= :availabilityEndDateTime)
            )
        )
        """,
        countQuery = """
        SELECT COUNT(DISTINCT s.id)
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
        AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
        AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
        AND (:roomType IS NULL OR :roomType = '' OR
            (r.room_type = :roomType))
        AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
        AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
        AND NOT EXISTS (
            SELECT 1 FROM reservation res
            WHERE res.room_id = r.id
            AND res.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
            AND (
                (res.start_date_time <= :availabilityStartDateTime AND res.end_date_time > :availabilityStartDateTime)
                OR (res.start_date_time < :availabilityEndDateTime AND res.end_date_time >= :availabilityEndDateTime)
                OR (res.start_date_time >= :availabilityStartDateTime AND res.end_date_time <= :availabilityEndDateTime)
            )
        )
        """,
        nativeQuery = true
    )
    Page<StudioListProjection> getStudioRoomPaginationWithAvailability(
        @Param("name") String name,
        @Param("city") String city,
        @Param("roomType") String roomType,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("availabilityStartDateTime") Instant availabilityStartDateTime,
        @Param("availabilityEndDateTime") Instant availabilityEndDateTime,
        Pageable pageable
    );

    // Método de conveniência que escolhe a query apropriada
    default Page<StudioListProjection> getStudioRoomPagination(
        String name,
        String city,
        String roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Instant availabilityStartDateTime,
        Instant availabilityEndDateTime,
        Pageable pageable
    ) {
        org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(StudioRepository.class);
        LOG.debug(
            "StudioRepository.getStudioRoomPagination: availabilityStart={}, availabilityEnd={}",
            availabilityStartDateTime,
            availabilityEndDateTime
        );

        if (availabilityStartDateTime != null && availabilityEndDateTime != null) {
            LOG.debug("Using getStudioRoomPaginationWithAvailability");
            return getStudioRoomPaginationWithAvailability(
                name,
                city,
                roomType,
                minPrice,
                maxPrice,
                availabilityStartDateTime,
                availabilityEndDateTime,
                pageable
            );
        } else {
            LOG.debug("Using getStudioRoomPaginationBasic");
            return getStudioRoomPaginationBasic(name, city, roomType, minPrice, maxPrice, pageable);
        }
    }

    // Keyset sem filtros de disponibilidade
    @Query(
        value = """
        SELECT DISTINCT
            s.id,
            s.name,
            s.description,
            s.address,
            s.city,
            s.state,
            s.image,
            (SELECT MIN(r2.hourly_rate)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as minPrice,
            (SELECT MAX(r2.hourly_rate)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as maxPrice,
            (SELECT COUNT(*)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as roomCount
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
          AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
          AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
          AND (:roomType IS NULL OR :roomType = '' OR
                (:roomType = 'RECORDING' AND r.room_type = 'RECORDING') OR
                (:roomType = 'REHEARSAL' AND r.room_type = 'REHEARSAL') OR
                (:roomType = 'LIVE' AND r.room_type = 'LIVE') OR
                (:roomType = 'MIXING' AND r.room_type = 'MIXING') OR
                (:roomType = 'BOTH' AND r.room_type IN ('RECORDING', 'REHEARSAL', 'LIVE', 'MIXING')))
          AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
          AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
          AND (:lastId IS NULL OR s.id > :lastId)
        ORDER BY s.id
        LIMIT :pageSize
        """,
        nativeQuery = true
    )
    List<StudioListProjection> getStudiosKeysetBasic(
        @Param("name") String name,
        @Param("city") String city,
        @Param("roomType") String roomType,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("lastId") Long lastId,
        @Param("pageSize") int pageSize
    );

    // Keyset com filtros de disponibilidade
    @Query(
        value = """
        SELECT DISTINCT
            s.id,
            s.name,
            s.description,
            s.address,
            s.city,
            s.state,
            s.image,
            (SELECT MIN(r2.hourly_rate)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as minPrice,
            (SELECT MAX(r2.hourly_rate)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as maxPrice,
            (SELECT COUNT(*)
             FROM room r2
             WHERE r2.studio_id = s.id AND r2.active = true) as roomCount
        FROM studio s
        INNER JOIN room r ON r.studio_id = s.id AND r.active = true
        WHERE s.active = true
          AND (:name IS NULL OR :name = '' OR UPPER(s.name) LIKE UPPER(CONCAT('%', :name, '%')))
          AND (:city IS NULL OR :city = '' OR UPPER(s.city) LIKE UPPER(CONCAT('%', :city, '%')))
          AND (:roomType IS NULL OR :roomType = '' OR
                (:roomType = 'RECORDING' AND r.room_type = 'RECORDING') OR
                (:roomType = 'REHEARSAL' AND r.room_type = 'REHEARSAL') OR
                (:roomType = 'LIVE' AND r.room_type = 'LIVE') OR
                (:roomType = 'MIXING' AND r.room_type = 'MIXING') OR
                (:roomType = 'BOTH' AND r.room_type IN ('RECORDING', 'REHEARSAL', 'LIVE', 'MIXING')))
          AND (:minPrice IS NULL OR r.hourly_rate >= :minPrice)
          AND (:maxPrice IS NULL OR r.hourly_rate <= :maxPrice)
          AND NOT EXISTS (
            SELECT 1 FROM reservation res
            WHERE res.room_id = r.id
            AND res.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
            AND (
                (res.start_date_time <= :availabilityStartDateTime AND res.end_date_time > :availabilityStartDateTime)
                OR (res.start_date_time < :availabilityEndDateTime AND res.end_date_time >= :availabilityEndDateTime)
                OR (res.start_date_time >= :availabilityStartDateTime AND res.end_date_time <= :availabilityEndDateTime)
            )
        )
          AND (:lastId IS NULL OR s.id > :lastId)
        ORDER BY s.id
        LIMIT :pageSize
        """,
        nativeQuery = true
    )
    List<StudioListProjection> getStudiosKeysetWithAvailability(
        @Param("name") String name,
        @Param("city") String city,
        @Param("roomType") String roomType,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("availabilityStartDateTime") Instant availabilityStartDateTime,
        @Param("availabilityEndDateTime") Instant availabilityEndDateTime,
        @Param("lastId") Long lastId,
        @Param("pageSize") int pageSize
    );

    // Método de conveniência para keyset
    default List<StudioListProjection> getStudiosKeyset(
        String name,
        String city,
        String roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Instant availabilityStartDateTime,
        Instant availabilityEndDateTime,
        Long lastId,
        int pageSize
    ) {
        if (availabilityStartDateTime != null && availabilityEndDateTime != null) {
            return getStudiosKeysetWithAvailability(
                name,
                city,
                roomType,
                minPrice,
                maxPrice,
                availabilityStartDateTime,
                availabilityEndDateTime,
                lastId,
                pageSize
            );
        } else {
            return getStudiosKeysetBasic(name, city, roomType, minPrice, maxPrice, lastId, pageSize);
        }
    }

    /**
     * Busca os detalhes completos do Studio com Rooms e suas imagens
     *
     * @param id ID do Studio
     * @return StudioDetailProjection com todos os dados relacionados
     */
    // @Query("SELECT s AS studio, " +
    //         "r AS room, " +
    //         "ri AS roomImages " +
    //         "FROM Studio s " +
    //         "LEFT JOIN s.room r " +
    //         "LEFT JOIN r.roomImages ri " +
    //         "WHERE s.id = :id AND s.active = true")
    // Optional<StudioDetailProjection> findStudioDetailById(@Param("id") Long id);

    /**
     * Busca os dados básicos do Studio
     */
    @Query(
        "SELECT s.id as id, " +
        "s.name as name, " +
        "s.description as description, " +
        "s.address as address, " +
        "s.city as city, " +
        "s.state as state, " +
        "s.zipCode as zipCode, " +
        "s.latitude as latitude, " +
        "s.longitude as longitude, " +
        "s.phone as phone, " +
        "s.email as email, " +
        "s.website as website, " +
        "s.image as image, " +
        "s.active as active, " +
        "s.createdAt as createdAt, " +
        "s.updatedAt as updatedAt, " +
        "s.owner.id as ownerId " +
        "FROM Studio s " +
        "WHERE s.id = :id AND s.active = true"
    )
    Optional<StudioBasicProjection> findStudioBasicDetailById(@Param("id") Long id);

    /**
     * Busca as rooms do Studio
     */
    @Query(
        "SELECT r.id as id, " +
        "r.name as name, " +
        "r.description as description, " +
        "r.hourlyRate as hourlyRate, " +
        "r.capacity as capacity, " +
        "r.soundproofed as soundproofed, " +
        "r.airConditioning as airConditioning, " +
        "r.roomType as roomType, " +
        "r.active as active, " +
        "r.createdAt as createdAt, " +
        "r.updatedAt as updatedAt " +
        "FROM Room r " +
        "WHERE r.studio.id = :studioId AND r.active = true " +
        "ORDER BY r.name"
    )
    List<RoomProjection> findRoomsByStudioId(@Param("studioId") Long studioId);

    /**
     * Busca as imagens das rooms
     */
    @Query(
        "SELECT ri.id as id, " +
        "ri.url as url, " +
        "ri.altText as altText, " +
        "ri.displayOrder as displayOrder, " +
        "ri.active as active, " +
        "ri.room.id as roomId " +
        "FROM RoomImage ri " +
        "WHERE ri.room.id IN :roomIds AND ri.active = true " +
        "ORDER BY ri.room.id, ri.displayOrder ASC"
    )
    List<RoomImageProjection> findImagesByRoomIds(@Param("roomIds") List<Long> roomIds);

    /**
     * Busca studios ativos por cidade
     */
    @Query("SELECT s FROM Studio s WHERE s.city = :city AND s.active = true ORDER BY s.name")
    List<Studio> findActiveByCityOrderByName(@Param("city") String city);

    /**
     * Busca studios ativos
     */
    @Query("SELECT s FROM Studio s WHERE s.active = true ORDER BY s.name")
    List<Studio> findAllActiveOrderByName();

    /**
     * Query de debug para verificar reservas conflitantes
     */
    @Query(
        value = """
        SELECT
            r.studio_id,
            res.room_id,
            res.start_date_time,
            res.end_date_time,
            res.status
        FROM reservation res
        INNER JOIN room r ON r.id = res.room_id
        WHERE res.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
        AND (
            (res.start_date_time <= :availabilityStartDateTime AND res.end_date_time > :availabilityStartDateTime)
            OR (res.start_date_time < :availabilityEndDateTime AND res.end_date_time >= :availabilityEndDateTime)
            OR (res.start_date_time >= :availabilityStartDateTime AND res.end_date_time <= :availabilityEndDateTime)
        )
        """,
        nativeQuery = true
    )
    List<Object[]> findConflictingReservations(
        @Param("availabilityStartDateTime") Instant availabilityStartDateTime,
        @Param("availabilityEndDateTime") Instant availabilityEndDateTime
    );
}
