package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.MemberShip;
import com.payfix.packages.service.dto.MemberShipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MemberShip} and its DTO {@link MemberShipDTO}.
 */
@Mapper(componentModel = "spring", uses = {DurationMapper.class, DurationUnitMapper.class})
public interface MemberShipMapper extends EntityMapper<MemberShipDTO, MemberShip> {
    @Mapping(target = "durationId", source = "duration.id")
    @Mapping(target = "durationUnitId", source = "durationUnit.id")
    MemberShipDTO toDto(MemberShip memberShip);

    @Mapping(target = "duration", source = "durationId")
    @Mapping(target = "durationUnit", source = "durationUnitId")
    MemberShip toEntity(MemberShipDTO memberShipDTO);

    default MemberShip fromId(Long id) {
        if (id == null) {
            return null;
        }
        MemberShip memberShip = new MemberShip();
        memberShip.setId(id);
        return memberShip;
    }
}
