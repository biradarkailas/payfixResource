package com.payfix.packages.repository;

import com.payfix.packages.domain.CompanyDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long> {}
