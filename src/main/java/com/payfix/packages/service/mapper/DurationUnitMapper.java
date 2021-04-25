package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.DurationUnit;
import com.payfix.packages.service.dto.DurationUnitDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link DurationUnit} and its DTO {@link DurationUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DurationUnitMapper extends EntityMapper<DurationUnitDTO, DurationUnit> {

    DurationUnitDTO toDto(DurationUnit durationUnit);

    DurationUnit toEntity(DurationUnitDTO durationUnitDTO);

    default DurationUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        DurationUnit durationUnit = new DurationUnit();
        durationUnit.setId(id);
        return durationUnit;
    }
}
