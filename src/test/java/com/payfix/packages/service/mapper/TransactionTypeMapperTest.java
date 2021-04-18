package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionTypeMapperTest {

    private TransactionTypeMapper transactionTypeMapper;

    @BeforeEach
    public void setUp() {
        transactionTypeMapper = new TransactionTypeMapperImpl();
    }
}
