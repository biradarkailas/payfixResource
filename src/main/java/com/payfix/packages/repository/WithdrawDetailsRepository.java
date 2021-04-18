package com.payfix.packages.repository;

import com.payfix.packages.domain.WithdrawDetails;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WithdrawDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawDetailsRepository extends JpaRepository<WithdrawDetails, Long>, JpaSpecificationExecutor<WithdrawDetails> {
    @Query("select withdrawDetails from WithdrawDetails withdrawDetails where withdrawDetails.user.login = ?#{principal.username}")
    List<WithdrawDetails> findByUserIsCurrentUser();
}
