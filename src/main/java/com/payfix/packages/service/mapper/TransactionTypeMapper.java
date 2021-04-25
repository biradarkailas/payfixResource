package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.TransactionType;
import com.payfix.packages.service.dto.TransactionTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link TransactionType} and its DTO {@link TransactionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionTypeMapper extends EntityMapper<TransactionTypeDTO, TransactionType> {

    TransactionTypeDTO toDto(TransactionType transactionType);

    TransactionType toEntity(TransactionTypeDTO transactionTypeDTO);

    default TransactionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionType transactionType = new TransactionType();
        transactionType.setId(id);
        return transactionType;
    }
}
