package com.payfix.packages.service;

import com.payfix.packages.service.dto.LogoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.Logo}.
 */
public interface LogoService {
    /**
     * Save a logo.
     *
     * @param logoDTO the entity to save.
     * @return the persisted entity.
     */
    LogoDTO save(LogoDTO logoDTO);

    /**
     * Get all the logos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LogoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" logo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LogoDTO> findOne(Long id);

    /**
     * Delete the "id" logo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
