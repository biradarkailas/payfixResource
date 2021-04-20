package com.payfix.packages.service;

import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.LogoSubscription}.
 */
public interface LogoSubscriptionService {
    /**
     * Save a logoSubscription.
     *
     * @param logoSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    LogoSubscriptionDTO save(LogoSubscriptionDTO logoSubscriptionDTO);

    /**
     * Partially updates a logoSubscription.
     *
     * @param logoSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LogoSubscriptionDTO> partialUpdate(LogoSubscriptionDTO logoSubscriptionDTO);

    /**
     * Get all the logoSubscriptions.
     *
     * @return the list of entities.
     */
    List<LogoSubscriptionDTO> findAll();

    /**
     * Get the "id" logoSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LogoSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" logoSubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
