package com.payfix.packages.repository;

import com.payfix.packages.domain.WithdrawStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WithdrawStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawStatusRepository extends JpaRepository<WithdrawStatus, Long> {}
