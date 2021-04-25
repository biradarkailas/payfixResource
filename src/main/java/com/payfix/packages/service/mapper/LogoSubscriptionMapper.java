package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.LogoSubscription;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link LogoSubscription} and its DTO {@link LogoSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, LogoMapper.class})
public interface LogoSubscriptionMapper extends EntityMapper<LogoSubscriptionDTO, LogoSubscription> {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "logoId", source = "logo.id")
    LogoSubscriptionDTO toDto(LogoSubscription logoSubscription);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "logo", source = "logoId")
    LogoSubscription toEntity(LogoSubscriptionDTO logoSubscriptionDTO);
}
