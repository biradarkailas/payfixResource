package com.payfix.packages.repository;

import com.payfix.packages.domain.UserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>, JpaSpecificationExecutor<UserDetails> {}
