package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WithdrawDetailsMapperTest {

    private WithdrawDetailsMapper withdrawDetailsMapper;

    @BeforeEach
    public void setUp() {
        withdrawDetailsMapper = new WithdrawDetailsMapperImpl();
    }
}
