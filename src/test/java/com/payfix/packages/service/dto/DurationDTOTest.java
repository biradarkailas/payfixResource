package com.payfix.packages.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payfix.packages.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DurationDTO.class);
        DurationDTO durationDTO1 = new DurationDTO();
        durationDTO1.setId(1L);
        DurationDTO durationDTO2 = new DurationDTO();
        assertThat(durationDTO1).isNotEqualTo(durationDTO2);
        durationDTO2.setId(durationDTO1.getId());
        assertThat(durationDTO1).isEqualTo(durationDTO2);
        durationDTO2.setId(2L);
        assertThat(durationDTO1).isNotEqualTo(durationDTO2);
        durationDTO1.setId(null);
        assertThat(durationDTO1).isNotEqualTo(durationDTO2);
    }
}
