package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.LogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Logo} and its DTO {@link LogoDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class, CategoryMapper.class, DurationMapper.class, DurationUnitMapper.class, PricePerDayMapper.class }
)
public interface LogoMapper extends EntityMapper<LogoDTO, Logo> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "id")
    @Mapping(target = "durationUnit", source = "durationUnit", qualifiedByName = "id")
    @Mapping(target = "pricePerDay", source = "pricePerDay", qualifiedByName = "id")
    LogoDTO toDto(Logo s);
}
