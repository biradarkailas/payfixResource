package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.Document;
import com.payfix.packages.service.dto.DocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "userId", source = "user.id")
    DocumentDTO toDto(Document document);

    @Mapping(target = "user", source = "userId")
    Document toEntity(DocumentDTO documentDTO);
}
