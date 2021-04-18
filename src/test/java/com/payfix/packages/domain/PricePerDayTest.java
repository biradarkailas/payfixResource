package com.payfix.packages.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricePerDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PricePerDay.class);
        PricePerDay pricePerDay1 = new PricePerDay();
        pricePerDay1.setId(1L);
        PricePerDay pricePerDay2 = new PricePerDay();
        pricePerDay2.setId(pricePerDay1.getId());
        assertThat(pricePerDay1).isEqualTo(pricePerDay2);
        pricePerDay2.setId(2L);
        assertThat(pricePerDay1).isNotEqualTo(pricePerDay2);
        pricePerDay1.setId(null);
        assertThat(pricePerDay1).isNotEqualTo(pricePerDay2);
    }
}
