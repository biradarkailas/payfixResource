package com.payfix.packages.service;

import com.payfix.packages.service.dto.PricePerDayDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.PricePerDay}.
 */
public interface PricePerDayService {
    /**
     * Save a pricePerDay.
     *
     * @param pricePerDayDTO the entity to save.
     * @return the persisted entity.
     */
    PricePerDayDTO save(PricePerDayDTO pricePerDayDTO);

    /**
     * Get all the pricePerDays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PricePerDayDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pricePerDay.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PricePerDayDTO> findOne(Long id);

    /**
     * Delete the "id" pricePerDay.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
