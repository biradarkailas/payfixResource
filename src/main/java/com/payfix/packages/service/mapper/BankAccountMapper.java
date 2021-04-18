package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.BankAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankAccount} and its DTO {@link BankAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {}
