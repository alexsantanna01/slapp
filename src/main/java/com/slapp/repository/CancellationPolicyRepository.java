package com.slapp.repository;

import com.slapp.domain.CancellationPolicy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CancellationPolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {}
