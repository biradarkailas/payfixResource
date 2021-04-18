package com.payfix.packages.repository;

import com.payfix.packages.domain.DurationUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DurationUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DurationUnitRepository extends JpaRepository<DurationUnit, Long>, JpaSpecificationExecutor<DurationUnit> {}
