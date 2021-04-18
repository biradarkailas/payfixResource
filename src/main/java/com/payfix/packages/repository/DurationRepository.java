package com.payfix.packages.repository;

import com.payfix.packages.domain.Duration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Duration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DurationRepository extends JpaRepository<Duration, Long> {}
