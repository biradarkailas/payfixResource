package com.payfix.packages.service;

import com.payfix.packages.service.dto.WithdrawStatusDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.WithdrawStatus}.
 */
public interface WithdrawStatusService {
    /**
     * Save a withdrawStatus.
     *
     * @param withdrawStatusDTO the entity to save.
     * @return the persisted entity.
     */
    WithdrawStatusDTO save(WithdrawStatusDTO withdrawStatusDTO);

    /**
     * Partially updates a withdrawStatus.
     *
     * @param withdrawStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WithdrawStatusDTO> partialUpdate(WithdrawStatusDTO withdrawStatusDTO);

    /**
     * Get all the withdrawStatuses.
     *
     * @return the list of entities.
     */
    List<WithdrawStatusDTO> findAll();

    /**
     * Get the "id" withdrawStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WithdrawStatusDTO> findOne(Long id);

    /**
     * Delete the "id" withdrawStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
