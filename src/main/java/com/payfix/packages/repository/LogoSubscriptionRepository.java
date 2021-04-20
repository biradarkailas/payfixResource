package com.payfix.packages.repository;

import com.payfix.packages.domain.LogoSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LogoSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogoSubscriptionRepository extends JpaRepository<LogoSubscription, Long>, JpaSpecificationExecutor<LogoSubscription> {
    @Query("select logoSubscription from LogoSubscription logoSubscription where logoSubscription.user.login = ?#{principal.username}")
    List<LogoSubscription> findByUserIsCurrentUser();
}
