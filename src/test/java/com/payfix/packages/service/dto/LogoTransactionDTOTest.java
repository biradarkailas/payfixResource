package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogoTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LogoTransactionDTO.class);
        LogoTransactionDTO logoTransactionDTO1 = new LogoTransactionDTO();
        logoTransactionDTO1.setId(1L);
        LogoTransactionDTO logoTransactionDTO2 = new LogoTransactionDTO();
        assertThat(logoTransactionDTO1).isNotEqualTo(logoTransactionDTO2);
        logoTransactionDTO2.setId(logoTransactionDTO1.getId());
        assertThat(logoTransactionDTO1).isEqualTo(logoTransactionDTO2);
        logoTransactionDTO2.setId(2L);
        assertThat(logoTransactionDTO1).isNotEqualTo(logoTransactionDTO2);
        logoTransactionDTO1.setId(null);
        assertThat(logoTransactionDTO1).isNotEqualTo(logoTransactionDTO2);
    }
}
