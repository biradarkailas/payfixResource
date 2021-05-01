package com.payfix.packages.service.mapper;

import com.payfix.packages.domain.CompanyDetails;
import com.payfix.packages.service.dto.CompanyDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link CompanyDetails} and its DTO {@link CompanyDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CompanyDetailsMapper extends EntityMapper<CompanyDetailsDTO, CompanyDetails> {
    @Mapping(target = "userId", source = "user.id")
    CompanyDetailsDTO toDto(CompanyDetails companyDetails);

    @Mapping(target = "user", source = "userId")
    CompanyDetails toEntity(CompanyDetailsDTO companyDetailsDTO);

    default CompanyDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setId(id);
        return companyDetails;
    }
}
