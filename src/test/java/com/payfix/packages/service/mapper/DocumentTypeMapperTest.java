package com.payfix.packages.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTypeMapperTest {

    private DocumentTypeMapper documentTypeMapper;

    @BeforeEach
    public void setUp() {
        documentTypeMapper = new DocumentTypeMapperImpl();
    }
}
