package com.payfix.packages.service;

import com.payfix.packages.service.dto.BankAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.BankAccount}.
 */
public interface BankAccountService {
    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save.
     * @return the persisted entity.
     */
    BankAccountDTO save(BankAccountDTO bankAccountDTO);

    /**
     * Partially updates a bankAccount.
     *
     * @param bankAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankAccountDTO> partialUpdate(BankAccountDTO bankAccountDTO);

    /**
     * Get all the bankAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bankAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankAccountDTO> findOne(Long id);

    /**
     * Delete the "id" bankAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}