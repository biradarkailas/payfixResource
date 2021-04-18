package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WithdrawDetails} and its DTO {@link WithdrawDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, WithdrawStatusMapper.class })
public interface WithdrawDetailsMapper extends EntityMapper<WithdrawDetailsDTO, WithdrawDetails> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "withdrawStatus", source = "withdrawStatus", qualifiedByName = "id")
    WithdrawDetailsDTO toDto(WithdrawDetails s);
}
