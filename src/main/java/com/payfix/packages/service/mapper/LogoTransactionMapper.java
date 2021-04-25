package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.LogoTransaction;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link LogoTransaction} and its DTO {@link LogoTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, LogoMapper.class, TransactionTypeMapper.class})
public interface LogoTransactionMapper extends EntityMapper<LogoTransactionDTO, LogoTransaction> {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "logoId", source = "logo.id")
    @Mapping(target = "transactionTypeId", source = "transactionType.id")
    LogoTransactionDTO toDto(LogoTransaction logoTransaction);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "logo", source = "logoId")
    @Mapping(target = "transactionType", source = "transactionTypeId")
    LogoTransaction toEntity(LogoTransactionDTO logoTransactionDTO);

    default LogoTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        LogoTransaction logoTransaction = new LogoTransaction();
        logoTransaction.setId(id);
        return logoTransaction;
    }
}
