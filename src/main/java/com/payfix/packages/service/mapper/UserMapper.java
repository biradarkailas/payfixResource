package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.Authority;
import com.payfix.packages.domain.User;
import com.payfix.packages.service.dto.AdminUserDTO;
import com.payfix.packages.service.dto.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<AdminUserDTO, User> {

    @Mapping(target = "authorities", ignore = true)
    AdminUserDTO toDto(User user);

    @Mapping(target = "authorities", ignore = true)
    User toEntity(AdminUserDTO adminUserDTO);

    @AfterMapping
    default void updateAdminUserDTO(@MappingTarget AdminUserDTO adminUserDTO, User user) {
        adminUserDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }

    @AfterMapping
    default void updateUser(@MappingTarget User user, AdminUserDTO adminUserDTO) {
        user.setAuthorities(authoritiesFromStrings(adminUserDTO.getAuthorities()));
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        string -> {
                            Authority auth = new Authority();
                            auth.setName(string);
                            return auth;
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
