package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DurationMapperTest {

    private DurationMapper durationMapper;

    @BeforeEach
    public void setUp() {
        durationMapper = new DurationMapperImpl();
    }
}
