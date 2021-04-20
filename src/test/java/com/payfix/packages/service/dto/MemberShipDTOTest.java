package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberShipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberShipDTO.class);
        MemberShipDTO memberShipDTO1 = new MemberShipDTO();
        memberShipDTO1.setId(1L);
        MemberShipDTO memberShipDTO2 = new MemberShipDTO();
        assertThat(memberShipDTO1).isNotEqualTo(memberShipDTO2);
        memberShipDTO2.setId(memberShipDTO1.getId());
        assertThat(memberShipDTO1).isEqualTo(memberShipDTO2);
        memberShipDTO2.setId(2L);
        assertThat(memberShipDTO1).isNotEqualTo(memberShipDTO2);
        memberShipDTO1.setId(null);
        assertThat(memberShipDTO1).isNotEqualTo(memberShipDTO2);
    }
}
