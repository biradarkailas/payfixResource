package com.payfix.packages.service;

import com.payfix.packages.service.dto.UserMemberShipDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.payfix.packages.domain.UserMemberShip}.
 */
public interface UserMemberShipService {
    /**
     * Save a userMemberShip.
     *
     * @param userMemberShipDTO the entity to save.
     * @return the persisted entity.
     */
    UserMemberShipDTO save(UserMemberShipDTO userMemberShipDTO);

    /**
     * Get all the userMemberShips.
     *
     * @return the list of entities.
     */
    List<UserMemberShipDTO> findAll();

    /**
     * Get the "id" userMemberShip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserMemberShipDTO> findOne(Long id);

    /**
     * Delete the "id" userMemberShip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
