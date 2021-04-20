package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMemberShipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMemberShipDTO.class);
        UserMemberShipDTO userMemberShipDTO1 = new UserMemberShipDTO();
        userMemberShipDTO1.setId(1L);
        UserMemberShipDTO userMemberShipDTO2 = new UserMemberShipDTO();
        assertThat(userMemberShipDTO1).isNotEqualTo(userMemberShipDTO2);
        userMemberShipDTO2.setId(userMemberShipDTO1.getId());
        assertThat(userMemberShipDTO1).isEqualTo(userMemberShipDTO2);
        userMemberShipDTO2.setId(2L);
        assertThat(userMemberShipDTO1).isNotEqualTo(userMemberShipDTO2);
        userMemberShipDTO1.setId(null);
        assertThat(userMemberShipDTO1).isNotEqualTo(userMemberShipDTO2);
    }
}
