package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DurationUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DurationUnitDTO.class);
        DurationUnitDTO durationUnitDTO1 = new DurationUnitDTO();
        durationUnitDTO1.setId(1L);
        DurationUnitDTO durationUnitDTO2 = new DurationUnitDTO();
        assertThat(durationUnitDTO1).isNotEqualTo(durationUnitDTO2);
        durationUnitDTO2.setId(durationUnitDTO1.getId());
        assertThat(durationUnitDTO1).isEqualTo(durationUnitDTO2);
        durationUnitDTO2.setId(2L);
        assertThat(durationUnitDTO1).isNotEqualTo(durationUnitDTO2);
        durationUnitDTO1.setId(null);
        assertThat(durationUnitDTO1).isNotEqualTo(durationUnitDTO2);
    }
}
