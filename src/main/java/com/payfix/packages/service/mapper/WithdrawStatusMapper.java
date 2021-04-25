package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.WithdrawStatus;
import com.payfix.packages.service.dto.WithdrawStatusDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link WithdrawStatus} and its DTO {@link WithdrawStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WithdrawStatusMapper extends EntityMapper<WithdrawStatusDTO, WithdrawStatus> {

    WithdrawStatusDTO toDto(WithdrawStatus withdrawStatus);

    WithdrawStatus toEntity(WithdrawStatusDTO withdrawStatusDTO);

    default WithdrawStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        WithdrawStatus withdrawStatus = new WithdrawStatus();
        withdrawStatus.setId(id);
        return withdrawStatus;
    }
}
