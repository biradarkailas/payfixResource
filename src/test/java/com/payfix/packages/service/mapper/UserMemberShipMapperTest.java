package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMemberShipMapperTest {

    private UserMemberShipMapper userMemberShipMapper;

    @BeforeEach
    public void setUp() {
        userMemberShipMapper = new UserMemberShipMapperImpl();
    }
}
