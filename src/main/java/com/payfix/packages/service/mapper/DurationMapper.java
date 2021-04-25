package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.Duration;
import com.payfix.packages.service.dto.DurationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Duration} and its DTO {@link DurationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DurationMapper extends EntityMapper<DurationDTO, Duration> {

    DurationDTO toDto(Duration duration);

    Duration toEntity(DurationDTO durationDTO);

    default Duration fromId(Long id) {
        if (id == null) {
            return null;
        }
        Duration duration = new Duration();
        duration.setId(id);
        return duration;
    }
}
