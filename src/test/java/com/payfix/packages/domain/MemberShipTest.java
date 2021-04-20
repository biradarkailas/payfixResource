package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberShipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberShip.class);
        MemberShip memberShip1 = new MemberShip();
        memberShip1.setId(1L);
        MemberShip memberShip2 = new MemberShip();
        memberShip2.setId(memberShip1.getId());
        assertThat(memberShip1).isEqualTo(memberShip2);
        memberShip2.setId(2L);
        assertThat(memberShip1).isNotEqualTo(memberShip2);
        memberShip1.setId(null);
        assertThat(memberShip1).isNotEqualTo(memberShip2);
    }
}
