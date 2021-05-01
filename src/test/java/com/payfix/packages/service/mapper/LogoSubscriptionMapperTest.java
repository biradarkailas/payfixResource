package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoSubscriptionMapperTest {

    private LogoSubscriptionMapper logoSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        logoSubscriptionMapper = new LogoSubscriptionMapperImpl();
    }
}
