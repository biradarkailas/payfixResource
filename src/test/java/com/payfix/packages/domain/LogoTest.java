package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Logo.class);
        Logo logo1 = new Logo();
        logo1.setId(1L);
        Logo logo2 = new Logo();
        logo2.setId(logo1.getId());
        assertThat(logo1).isEqualTo(logo2);
        logo2.setId(2L);
        assertThat(logo1).isNotEqualTo(logo2);
        logo1.setId(null);
        assertThat(logo1).isNotEqualTo(logo2);
    }
}
