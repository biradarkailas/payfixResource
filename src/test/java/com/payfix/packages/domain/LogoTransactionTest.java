package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogoTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogoTransaction.class);
        LogoTransaction logoTransaction1 = new LogoTransaction();
        logoTransaction1.setId(1L);
        LogoTransaction logoTransaction2 = new LogoTransaction();
        logoTransaction2.setId(logoTransaction1.getId());
        assertThat(logoTransaction1).isEqualTo(logoTransaction2);
        logoTransaction2.setId(2L);
        assertThat(logoTransaction1).isNotEqualTo(logoTransaction2);
        logoTransaction1.setId(null);
        assertThat(logoTransaction1).isNotEqualTo(logoTransaction2);
    }
}
