package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.DocumentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentType} and its DTO {@link DocumentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentTypeMapper extends EntityMapper<DocumentTypeDTO, DocumentType> {}
