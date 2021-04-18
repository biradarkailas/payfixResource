package com.payfix.packages.web.rest;

import com.payfix.packages.repository.WithdrawStatusRepository;
import com.payfix.packages.service.WithdrawStatusService;
import com.payfix.packages.service.dto.WithdrawStatusDTO;
import com.payfix.packages.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.payfix.packages.domain.WithdrawStatus}.
 */
@RestController
@RequestMapping("/api")
public class WithdrawStatusResource {

    private final Logger log = LoggerFactory.getLogger(WithdrawStatusResource.class);

    private static final String ENTITY_NAME = "withdrawStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WithdrawStatusService withdrawStatusService;

    private final WithdrawStatusRepository withdrawStatusRepository;

    public WithdrawStatusResource(WithdrawStatusService withdrawStatusService, WithdrawStatusRepository withdrawStatusRepository) {
        this.withdrawStatusService = withdrawStatusService;
        this.withdrawStatusRepository = withdrawStatusRepository;
    }

    /**
     * {@code POST  /withdraw-statuses} : Create a new withdrawStatus.
     *
     * @param withdrawStatusDTO the withdrawStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new withdrawStatusDTO, or with status {@code 400 (Bad Request)} if the withdrawStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/withdraw-statuses")
    public ResponseEntity<WithdrawStatusDTO> createWithdrawStatus(@Valid @RequestBody WithdrawStatusDTO withdrawStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save WithdrawStatus : {}", withdrawStatusDTO);
        if (withdrawStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new withdrawStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawStatusDTO result = withdrawStatusService.save(withdrawStatusDTO);
        return ResponseEntity
            .created(new URI("/api/withdraw-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /withdraw-statuses/:id} : Updates an existing withdrawStatus.
     *
     * @param id the id of the withdrawStatusDTO to save.
     * @param withdrawStatusDTO the withdrawStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated withdrawStatusDTO,
     * or with status {@code 400 (Bad Request)} if the withdrawStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the withdrawStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/withdraw-statuses/{id}")
    public ResponseEntity<WithdrawStatusDTO> updateWithdrawStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WithdrawStatusDTO withdrawStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WithdrawStatus : {}, {}", id, withdrawStatusDTO);
        if (withdrawStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, withdrawStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!withdrawStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WithdrawStatusDTO result = withdrawStatusService.save(withdrawStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, withdrawStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /withdraw-statuses/:id} : Partial updates given fields of an existing withdrawStatus, field will ignore if it is null
     *
     * @param id the id of the withdrawStatusDTO to save.
     * @param withdrawStatusDTO the withdrawStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated withdrawStatusDTO,
     * or with status {@code 400 (Bad Request)} if the withdrawStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the withdrawStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the withdrawStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/withdraw-statuses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WithdrawStatusDTO> partialUpdateWithdrawStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WithdrawStatusDTO withdrawStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WithdrawStatus partially : {}, {}", id, withdrawStatusDTO);
        if (withdrawStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, withdrawStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!withdrawStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WithdrawStatusDTO> result = withdrawStatusService.partialUpdate(withdrawStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, withdrawStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /withdraw-statuses} : get all the withdrawStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of withdrawStatuses in body.
     */
    @GetMapping("/withdraw-statuses")
    public List<WithdrawStatusDTO> getAllWithdrawStatuses() {
        log.debug("REST request to get all WithdrawStatuses");
        return withdrawStatusService.findAll();
    }

    /**
     * {@code GET  /withdraw-statuses/:id} : get the "id" withdrawStatus.
     *
     * @param id the id of the withdrawStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the withdrawStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/withdraw-statuses/{id}")
    public ResponseEntity<WithdrawStatusDTO> getWithdrawStatus(@PathVariable Long id) {
        log.debug("REST request to get WithdrawStatus : {}", id);
        Optional<WithdrawStatusDTO> withdrawStatusDTO = withdrawStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(withdrawStatusDTO);
    }

    /**
     * {@code DELETE  /withdraw-statuses/:id} : delete the "id" withdrawStatus.
     *
     * @param id the id of the withdrawStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/withdraw-statuses/{id}")
    public ResponseEntity<Void> deleteWithdrawStatus(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawStatus : {}", id);
        withdrawStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
