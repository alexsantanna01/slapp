package com.slapp.repository;

import com.slapp.domain.SpecialPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialPriceRepository extends JpaRepository<SpecialPrice, Long> {}
