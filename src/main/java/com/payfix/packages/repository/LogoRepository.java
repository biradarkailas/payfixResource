package com.payfix.packages.repository;

import com.payfix.packages.domain.Logo;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Logo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogoRepository extends JpaRepository<Logo, Long>, JpaSpecificationExecutor<Logo> {
    @Query("select logo from Logo logo where logo.user.login = ?#{principal.username}")
    List<Logo> findByUserIsCurrentUser();
}
