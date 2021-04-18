package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.UserDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDetails} and its DTO {@link UserDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserDetailsMapper extends EntityMapper<UserDetailsDTO, UserDetails> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UserDetailsDTO toDto(UserDetails s);
}
