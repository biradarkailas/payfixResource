package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogoSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogoSubscriptionDTO.class);
        LogoSubscriptionDTO logoSubscriptionDTO1 = new LogoSubscriptionDTO();
        logoSubscriptionDTO1.setId(1L);
        LogoSubscriptionDTO logoSubscriptionDTO2 = new LogoSubscriptionDTO();
        assertThat(logoSubscriptionDTO1).isNotEqualTo(logoSubscriptionDTO2);
        logoSubscriptionDTO2.setId(logoSubscriptionDTO1.getId());
        assertThat(logoSubscriptionDTO1).isEqualTo(logoSubscriptionDTO2);
        logoSubscriptionDTO2.setId(2L);
        assertThat(logoSubscriptionDTO1).isNotEqualTo(logoSubscriptionDTO2);
        logoSubscriptionDTO1.setId(null);
        assertThat(logoSubscriptionDTO1).isNotEqualTo(logoSubscriptionDTO2);
    }
}
