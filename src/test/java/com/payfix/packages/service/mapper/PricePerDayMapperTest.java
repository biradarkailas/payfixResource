package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricePerDayMapperTest {

    private PricePerDayMapper pricePerDayMapper;

    @BeforeEach
    public void setUp() {
        pricePerDayMapper = new PricePerDayMapperImpl();
    }
}
