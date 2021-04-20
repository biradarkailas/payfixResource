package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.DurationUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DurationUnit} and its DTO {@link DurationUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DurationUnitMapper extends EntityMapper<DurationUnitDTO, DurationUnit> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DurationUnitDTO toDtoId(DurationUnit durationUnit);
}
