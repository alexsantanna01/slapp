package com.slapp.repository;

import com.slapp.domain.Availability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Availability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {}
