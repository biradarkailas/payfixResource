package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.TransactionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionType} and its DTO {@link TransactionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionTypeMapper extends EntityMapper<TransactionTypeDTO, TransactionType> {}
