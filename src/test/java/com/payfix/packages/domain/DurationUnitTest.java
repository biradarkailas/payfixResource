package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DurationUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DurationUnit.class);
        DurationUnit durationUnit1 = new DurationUnit();
        durationUnit1.setId(1L);
        DurationUnit durationUnit2 = new DurationUnit();
        durationUnit2.setId(durationUnit1.getId());
        assertThat(durationUnit1).isEqualTo(durationUnit2);
        durationUnit2.setId(2L);
        assertThat(durationUnit1).isNotEqualTo(durationUnit2);
        durationUnit1.setId(null);
        assertThat(durationUnit1).isNotEqualTo(durationUnit2);
    }
}
