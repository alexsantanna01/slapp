package com.slapp.repository;

import com.slapp.domain.Studio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Studio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudioRepository extends JpaRepository<Studio, Long>, JpaSpecificationExecutor<Studio> {}
