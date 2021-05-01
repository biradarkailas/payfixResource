package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyDetailsMapperTest {

    private CompanyDetailsMapper companyDetailsMapper;

    @BeforeEach
    public void setUp() {
        companyDetailsMapper = new CompanyDetailsMapperImpl();
    }
}
