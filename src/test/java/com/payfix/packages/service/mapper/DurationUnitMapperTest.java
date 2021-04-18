package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DurationUnitMapperTest {

    private DurationUnitMapper durationUnitMapper;

    @BeforeEach
    public void setUp() {
        durationUnitMapper = new DurationUnitMapperImpl();
    }
}
