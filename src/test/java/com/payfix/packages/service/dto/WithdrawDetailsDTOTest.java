package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WithdrawDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawDetailsDTO.class);
        WithdrawDetailsDTO withdrawDetailsDTO1 = new WithdrawDetailsDTO();
        withdrawDetailsDTO1.setId(1L);
        WithdrawDetailsDTO withdrawDetailsDTO2 = new WithdrawDetailsDTO();
        assertThat(withdrawDetailsDTO1).isNotEqualTo(withdrawDetailsDTO2);
        withdrawDetailsDTO2.setId(withdrawDetailsDTO1.getId());
        assertThat(withdrawDetailsDTO1).isEqualTo(withdrawDetailsDTO2);
        withdrawDetailsDTO2.setId(2L);
        assertThat(withdrawDetailsDTO1).isNotEqualTo(withdrawDetailsDTO2);
        withdrawDetailsDTO1.setId(null);
        assertThat(withdrawDetailsDTO1).isNotEqualTo(withdrawDetailsDTO2);
    }
}
