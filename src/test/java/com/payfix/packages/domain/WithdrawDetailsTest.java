package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WithdrawDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawDetails.class);
        WithdrawDetails withdrawDetails1 = new WithdrawDetails();
        withdrawDetails1.setId(1L);
        WithdrawDetails withdrawDetails2 = new WithdrawDetails();
        withdrawDetails2.setId(withdrawDetails1.getId());
        assertThat(withdrawDetails1).isEqualTo(withdrawDetails2);
        withdrawDetails2.setId(2L);
        assertThat(withdrawDetails1).isNotEqualTo(withdrawDetails2);
        withdrawDetails1.setId(null);
        assertThat(withdrawDetails1).isNotEqualTo(withdrawDetails2);
    }
}
