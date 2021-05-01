package com.payfix.packages.service;

import com.payfix.packages.service.dto.CompanyDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.CompanyDetails}.
 */
public interface CompanyDetailsService {
    /**
     * Save a companyDetails.
     *
     * @param companyDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    CompanyDetailsDTO save(CompanyDetailsDTO companyDetailsDTO);

    /**
     * Get all the companyDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompanyDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" companyDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanyDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" companyDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
