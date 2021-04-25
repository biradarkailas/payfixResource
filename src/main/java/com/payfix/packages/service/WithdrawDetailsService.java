package com.payfix.packages.service;

import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.WithdrawDetails}.
 */
public interface WithdrawDetailsService {
    /**
     * Save a withdrawDetails.
     *
     * @param withdrawDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    WithdrawDetailsDTO save(WithdrawDetailsDTO withdrawDetailsDTO);

    /**
     * Get all the withdrawDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WithdrawDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" withdrawDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WithdrawDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" withdrawDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
