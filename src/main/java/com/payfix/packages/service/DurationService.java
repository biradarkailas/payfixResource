package com.payfix.packages.service;

import com.payfix.packages.service.dto.DurationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.Duration}.
 */
public interface DurationService {
    /**
     * Save a duration.
     *
     * @param durationDTO the entity to save.
     * @return the persisted entity.
     */
    DurationDTO save(DurationDTO durationDTO);

    /**
     * Partially updates a duration.
     *
     * @param durationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DurationDTO> partialUpdate(DurationDTO durationDTO);

    /**
     * Get all the durations.
     *
     * @return the list of entities.
     */
    List<DurationDTO> findAll();

    /**
     * Get the "id" duration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DurationDTO> findOne(Long id);

    /**
     * Delete the "id" duration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
