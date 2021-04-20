package com.payfix.packages.repository;

import com.payfix.packages.domain.UserMemberShip;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserMemberShip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMemberShipRepository extends JpaRepository<UserMemberShip, Long> {
    @Query("select userMemberShip from UserMemberShip userMemberShip where userMemberShip.user.login = ?#{principal.username}")
    List<UserMemberShip> findByUserIsCurrentUser();
}
