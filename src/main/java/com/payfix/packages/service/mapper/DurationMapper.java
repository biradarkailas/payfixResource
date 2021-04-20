package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.DurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Duration} and its DTO {@link DurationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DurationMapper extends EntityMapper<DurationDTO, Duration> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DurationDTO toDtoId(Duration duration);
}
