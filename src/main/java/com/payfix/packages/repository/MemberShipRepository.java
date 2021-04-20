package com.payfix.packages.repository;

import com.payfix.packages.domain.MemberShip;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MemberShip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {}
