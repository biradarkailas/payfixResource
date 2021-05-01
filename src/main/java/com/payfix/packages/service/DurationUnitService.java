package com.payfix.packages.service;

import com.payfix.packages.service.dto.DurationUnitDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.DurationUnit}.
 */
public interface DurationUnitService {
    /**
     * Save a durationUnit.
     *
     * @param durationUnitDTO the entity to save.
     * @return the persisted entity.
     */
    DurationUnitDTO save(DurationUnitDTO durationUnitDTO);

    /**
     * Get all the durationUnits.
     *
     * @return the list of entities.
     */
    List<DurationUnitDTO> findAll();

    /**
     * Get the "id" durationUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DurationUnitDTO> findOne(Long id);

    /**
     * Delete the "id" durationUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
