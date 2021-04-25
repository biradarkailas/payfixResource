package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.PricePerDay;
import com.payfix.packages.service.dto.PricePerDayDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PricePerDay} and its DTO {@link PricePerDayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PricePerDayMapper extends EntityMapper<PricePerDayDTO, PricePerDay> {

    PricePerDayDTO toDto(PricePerDay pricePerDay);

    PricePerDay toEntity(PricePerDayDTO pricePerDayDTO);

    default PricePerDay fromId(Long id) {
        if (id == null) {
            return null;
        }
        PricePerDay pricePerDay = new PricePerDay();
        pricePerDay.setId(id);
        return pricePerDay;
    }
}
