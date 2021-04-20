package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.UserMemberShipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMemberShip} and its DTO {@link UserMemberShipDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, MemberShipMapper.class })
public interface UserMemberShipMapper extends EntityMapper<UserMemberShipDTO, UserMemberShip> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "memberShip", source = "memberShip", qualifiedByName = "id")
    UserMemberShipDTO toDto(UserMemberShip s);
}
