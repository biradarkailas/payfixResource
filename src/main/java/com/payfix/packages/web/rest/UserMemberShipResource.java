package com.payfix.packages.web.rest;

import com.payfix.packages.repository.UserMemberShipRepository;
import com.payfix.packages.service.UserMemberShipService;
import com.payfix.packages.service.dto.UserMemberShipDTO;
import com.payfix.packages.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.payfix.packages.domain.UserMemberShip}.
 */
@RestController
@RequestMapping("/api")
public class UserMemberShipResource {

    private final Logger log = LoggerFactory.getLogger(UserMemberShipResource.class);

    private static final String ENTITY_NAME = "userMemberShip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMemberShipService userMemberShipService;

    private final UserMemberShipRepository userMemberShipRepository;

    public UserMemberShipResource(UserMemberShipService userMemberShipService, UserMemberShipRepository userMemberShipRepository) {
        this.userMemberShipService = userMemberShipService;
        this.userMemberShipRepository = userMemberShipRepository;
    }

    /**
     * {@code POST  /user-member-ships} : Create a new userMemberShip.
     *
     * @param userMemberShipDTO the userMemberShipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMemberShipDTO, or with status {@code 400 (Bad Request)} if the userMemberShip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-member-ships")
    public ResponseEntity<UserMemberShipDTO> createUserMemberShip(@RequestBody UserMemberShipDTO userMemberShipDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserMemberShip : {}", userMemberShipDTO);
        if (userMemberShipDTO.getId() != null) {
            throw new BadRequestAlertException("A new userMemberShip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMemberShipDTO result = userMemberShipService.save(userMemberShipDTO);
        return ResponseEntity
            .created(new URI("/api/user-member-ships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-member-ships/:id} : Updates an existing userMemberShip.
     *
     * @param id the id of the userMemberShipDTO to save.
     * @param userMemberShipDTO the userMemberShipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMemberShipDTO,
     * or with status {@code 400 (Bad Request)} if the userMemberShipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMemberShipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-member-ships/{id}")
    public ResponseEntity<UserMemberShipDTO> updateUserMemberShip(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserMemberShipDTO userMemberShipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserMemberShip : {}, {}", id, userMemberShipDTO);
        if (userMemberShipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMemberShipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMemberShipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserMemberShipDTO result = userMemberShipService.save(userMemberShipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userMemberShipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-member-ships/:id} : Partial updates given fields of an existing userMemberShip, field will ignore if it is null
     *
     * @param id the id of the userMemberShipDTO to save.
     * @param userMemberShipDTO the userMemberShipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMemberShipDTO,
     * or with status {@code 400 (Bad Request)} if the userMemberShipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userMemberShipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMemberShipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-member-ships/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserMemberShipDTO> partialUpdateUserMemberShip(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserMemberShipDTO userMemberShipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserMemberShip partially : {}, {}", id, userMemberShipDTO);
        if (userMemberShipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMemberShipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMemberShipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserMemberShipDTO> result = userMemberShipService.partialUpdate(userMemberShipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userMemberShipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-member-ships} : get all the userMemberShips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMemberShips in body.
     */
    @GetMapping("/user-member-ships")
    public List<UserMemberShipDTO> getAllUserMemberShips() {
        log.debug("REST request to get all UserMemberShips");
        return userMemberShipService.findAll();
    }

    /**
     * {@code GET  /user-member-ships/:id} : get the "id" userMemberShip.
     *
     * @param id the id of the userMemberShipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMemberShipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-member-ships/{id}")
    public ResponseEntity<UserMemberShipDTO> getUserMemberShip(@PathVariable Long id) {
        log.debug("REST request to get UserMemberShip : {}", id);
        Optional<UserMemberShipDTO> userMemberShipDTO = userMemberShipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMemberShipDTO);
    }

    /**
     * {@code DELETE  /user-member-ships/:id} : delete the "id" userMemberShip.
     *
     * @param id the id of the userMemberShipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-member-ships/{id}")
    public ResponseEntity<Void> deleteUserMemberShip(@PathVariable Long id) {
        log.debug("REST request to delete UserMemberShip : {}", id);
        userMemberShipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
