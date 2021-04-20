package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoTransactionMapperTest {

    private LogoTransactionMapper logoTransactionMapper;

    @BeforeEach
    public void setUp() {
        logoTransactionMapper = new LogoTransactionMapperImpl();
    }
}
