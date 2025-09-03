package com.slapp.repository;

import com.slapp.domain.RoomImage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findByRoomIdOrderByDisplayOrderAsc(Long roomId);
}
