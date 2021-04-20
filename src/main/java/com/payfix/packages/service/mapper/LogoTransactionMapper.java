package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LogoTransaction} and its DTO {@link LogoTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, LogoMapper.class, TransactionTypeMapper.class })
public interface LogoTransactionMapper extends EntityMapper<LogoTransactionDTO, LogoTransaction> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "logo", source = "logo", qualifiedByName = "id")
    @Mapping(target = "transactionType", source = "transactionType", qualifiedByName = "id")
    LogoTransactionDTO toDto(LogoTransaction s);
}
