package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WithdrawStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawStatusDTO.class);
        WithdrawStatusDTO withdrawStatusDTO1 = new WithdrawStatusDTO();
        withdrawStatusDTO1.setId(1L);
        WithdrawStatusDTO withdrawStatusDTO2 = new WithdrawStatusDTO();
        assertThat(withdrawStatusDTO1).isNotEqualTo(withdrawStatusDTO2);
        withdrawStatusDTO2.setId(withdrawStatusDTO1.getId());
        assertThat(withdrawStatusDTO1).isEqualTo(withdrawStatusDTO2);
        withdrawStatusDTO2.setId(2L);
        assertThat(withdrawStatusDTO1).isNotEqualTo(withdrawStatusDTO2);
        withdrawStatusDTO1.setId(null);
        assertThat(withdrawStatusDTO1).isNotEqualTo(withdrawStatusDTO2);
    }
}
