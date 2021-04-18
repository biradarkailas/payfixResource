package com.payfix.packages.repository;

import com.payfix.packages.domain.PricePerDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PricePerDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricePerDayRepository extends JpaRepository<PricePerDay, Long> {}
