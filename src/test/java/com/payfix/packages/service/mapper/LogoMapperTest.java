package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoMapperTest {

    private LogoMapper logoMapper;

    @BeforeEach
    public void setUp() {
        logoMapper = new LogoMapperImpl();
    }
}
