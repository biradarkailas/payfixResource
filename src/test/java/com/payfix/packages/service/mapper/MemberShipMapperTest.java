package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberShipMapperTest {

    private MemberShipMapper memberShipMapper;

    @BeforeEach
    public void setUp() {
        memberShipMapper = new MemberShipMapperImpl();
    }
}
