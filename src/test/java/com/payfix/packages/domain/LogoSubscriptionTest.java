package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogoSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogoSubscription.class);
        LogoSubscription logoSubscription1 = new LogoSubscription();
        logoSubscription1.setId(1L);
        LogoSubscription logoSubscription2 = new LogoSubscription();
        logoSubscription2.setId(logoSubscription1.getId());
        assertThat(logoSubscription1).isEqualTo(logoSubscription2);
        logoSubscription2.setId(2L);
        assertThat(logoSubscription1).isNotEqualTo(logoSubscription2);
        logoSubscription1.setId(null);
        assertThat(logoSubscription1).isNotEqualTo(logoSubscription2);
    }
}
