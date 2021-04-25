package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.WithdrawDetails;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link WithdrawDetails} and its DTO {@link WithdrawDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, WithdrawStatusMapper.class})
public interface WithdrawDetailsMapper extends EntityMapper<WithdrawDetailsDTO, WithdrawDetails> {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "withdrawStatusId", source = "withdrawStatus.id")
    WithdrawDetailsDTO toDto(WithdrawDetails withdrawDetails);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "withdrawStatusId", target = "withdrawStatus")
    WithdrawDetails toEntity(WithdrawDetailsDTO withdrawDetailsDTO);

    default WithdrawDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        WithdrawDetails withdrawDetails = new WithdrawDetails();
        withdrawDetails.setId(id);
        return withdrawDetails;
    }
}
