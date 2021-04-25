package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.Logo;
import com.payfix.packages.service.dto.LogoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Logo} and its DTO {@link LogoDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, CategoryMapper.class, DurationMapper.class, DurationUnitMapper.class, PricePerDayMapper.class}
)
public interface LogoMapper extends EntityMapper<LogoDTO, Logo> {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "durationId", source = "duration.id")
    @Mapping(target = "durationUnitId", source = "durationUnit.id")
    @Mapping(target = "pricePerDayId", source = "pricePerDay.id")
    LogoDTO toDto(Logo logo);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "duration", source = "durationId")
    @Mapping(target = "durationUnit", source = "durationUnitId")
    @Mapping(target = "pricePerDay", source = "pricePerDayId")
    Logo toEntity(LogoDTO logoDTO);

    default Logo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Logo logo = new Logo();
        logo.setId(id);
        return logo;
    }
}
