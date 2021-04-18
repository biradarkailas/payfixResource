package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WithdrawStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawStatus.class);
        WithdrawStatus withdrawStatus1 = new WithdrawStatus();
        withdrawStatus1.setId(1L);
        WithdrawStatus withdrawStatus2 = new WithdrawStatus();
        withdrawStatus2.setId(withdrawStatus1.getId());
        assertThat(withdrawStatus1).isEqualTo(withdrawStatus2);
        withdrawStatus2.setId(2L);
        assertThat(withdrawStatus1).isNotEqualTo(withdrawStatus2);
        withdrawStatus1.setId(null);
        assertThat(withdrawStatus1).isNotEqualTo(withdrawStatus2);
    }
}
