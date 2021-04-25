package com.payfix.packages.service;

import com.payfix.packages.service.dto.MemberShipDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.MemberShip}.
 */
public interface MemberShipService {
    /**
     * Save a memberShip.
     *
     * @param memberShipDTO the entity to save.
     * @return the persisted entity.
     */
    MemberShipDTO save(MemberShipDTO memberShipDTO);

    /**
     * Get all the memberShips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberShipDTO> findAll(Pageable pageable);

    /**
     * Get the "id" memberShip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberShipDTO> findOne(Long id);

    /**
     * Delete the "id" memberShip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
