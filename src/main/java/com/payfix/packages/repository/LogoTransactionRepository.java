package com.payfix.packages.repository;

import com.payfix.packages.domain.LogoTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LogoTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogoTransactionRepository extends JpaRepository<LogoTransaction, Long>, JpaSpecificationExecutor<LogoTransaction> {
    @Query("select logoTransaction from LogoTransaction logoTransaction where logoTransaction.user.login = ?#{principal.username}")
    List<LogoTransaction> findByUserIsCurrentUser();
}
