package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.MemberShipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberShip} and its DTO {@link MemberShipDTO}.
 */
@Mapper(componentModel = "spring", uses = { DurationMapper.class, DurationUnitMapper.class })
public interface MemberShipMapper extends EntityMapper<MemberShipDTO, MemberShip> {
    @Mapping(target = "duration", source = "duration", qualifiedByName = "id")
    @Mapping(target = "durationUnit", source = "durationUnit", qualifiedByName = "id")
    MemberShipDTO toDto(MemberShip s);
}
