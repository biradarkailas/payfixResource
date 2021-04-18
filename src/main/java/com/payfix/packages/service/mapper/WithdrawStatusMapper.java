package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.WithdrawStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WithdrawStatus} and its DTO {@link WithdrawStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WithdrawStatusMapper extends EntityMapper<WithdrawStatusDTO, WithdrawStatus> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WithdrawStatusDTO toDtoId(WithdrawStatus withdrawStatus);
}
