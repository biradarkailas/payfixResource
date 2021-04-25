package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.UserMemberShip;
import com.payfix.packages.service.dto.UserMemberShipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link UserMemberShip} and its DTO {@link UserMemberShipDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MemberShipMapper.class})
public interface UserMemberShipMapper extends EntityMapper<UserMemberShipDTO, UserMemberShip> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userName")
    @Mapping(source = "memberShip.id", target = "memberShipId")
    UserMemberShipDTO toDto(UserMemberShip userMemberShip);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "memberShipId", target = "memberShip")
    UserMemberShip toEntity(UserMemberShipDTO userMemberShipDTO);

    default UserMemberShip fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserMemberShip userMemberShip = new UserMemberShip();
        userMemberShip.setId(id);
        return userMemberShip;
    }
}
