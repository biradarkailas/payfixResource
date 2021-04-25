package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.UserDetails;
import com.payfix.packages.service.dto.UserDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link UserDetails} and its DTO {@link UserDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserDetailsMapper extends EntityMapper<UserDetailsDTO, UserDetails> {
    @Mapping(target = "userId", source = "user.id")
    UserDetailsDTO toDto(UserDetails userDetails);

    @Mapping(source = "userId", target = "user")
    UserDetails toEntity(UserDetailsDTO userDetailsDTO);
}
