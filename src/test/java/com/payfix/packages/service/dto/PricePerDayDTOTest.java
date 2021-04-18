package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricePerDayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PricePerDayDTO.class);
        PricePerDayDTO pricePerDayDTO1 = new PricePerDayDTO();
        pricePerDayDTO1.setId(1L);
        PricePerDayDTO pricePerDayDTO2 = new PricePerDayDTO();
        assertThat(pricePerDayDTO1).isNotEqualTo(pricePerDayDTO2);
        pricePerDayDTO2.setId(pricePerDayDTO1.getId());
        assertThat(pricePerDayDTO1).isEqualTo(pricePerDayDTO2);
        pricePerDayDTO2.setId(2L);
        assertThat(pricePerDayDTO1).isNotEqualTo(pricePerDayDTO2);
        pricePerDayDTO1.setId(null);
        assertThat(pricePerDayDTO1).isNotEqualTo(pricePerDayDTO2);
    }
}
