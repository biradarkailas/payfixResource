package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.*;
import com.payfix.packages.service.dto.PricePerDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PricePerDay} and its DTO {@link PricePerDayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PricePerDayMapper extends EntityMapper<PricePerDayDTO, PricePerDay> {}
