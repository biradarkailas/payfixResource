package com.payfix.packages.service;

import com.payfix.packages.service.dto.LogoTransactionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.LogoTransaction}.
 */
public interface LogoTransactionService {
    /**
     * Save a logoTransaction.
     *
     * @param logoTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    LogoTransactionDTO save(LogoTransactionDTO logoTransactionDTO);

    /**
     * Get all the logoTransactions.
     *
     * @return the list of entities.
     */
    List<LogoTransactionDTO> findAll();

    /**
     * Get the "id" logoTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LogoTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" logoTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
