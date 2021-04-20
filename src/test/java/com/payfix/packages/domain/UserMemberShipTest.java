package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMemberShipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMemberShip.class);
        UserMemberShip userMemberShip1 = new UserMemberShip();
        userMemberShip1.setId(1L);
        UserMemberShip userMemberShip2 = new UserMemberShip();
        userMemberShip2.setId(userMemberShip1.getId());
        assertThat(userMemberShip1).isEqualTo(userMemberShip2);
        userMemberShip2.setId(2L);
        assertThat(userMemberShip1).isNotEqualTo(userMemberShip2);
        userMemberShip1.setId(null);
        assertThat(userMemberShip1).isNotEqualTo(userMemberShip2);
    }
}
