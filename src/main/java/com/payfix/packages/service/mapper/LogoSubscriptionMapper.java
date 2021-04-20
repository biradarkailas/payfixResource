package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LogoSubscription} and its DTO {@link LogoSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, LogoMapper.class })
public interface LogoSubscriptionMapper extends EntityMapper<LogoSubscriptionDTO, LogoSubscription> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "logo", source = "logo", qualifiedByName = "id")
    LogoSubscriptionDTO toDto(LogoSubscription s);
}
