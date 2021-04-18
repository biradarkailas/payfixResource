package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.DocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    DocumentDTO toDto(Document s);
}
