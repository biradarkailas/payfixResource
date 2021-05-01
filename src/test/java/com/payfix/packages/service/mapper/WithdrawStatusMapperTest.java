package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WithdrawStatusMapperTest {

    private WithdrawStatusMapper withdrawStatusMapper;

    @BeforeEach
    public void setUp() {
        withdrawStatusMapper = new WithdrawStatusMapperImpl();
    }
}
