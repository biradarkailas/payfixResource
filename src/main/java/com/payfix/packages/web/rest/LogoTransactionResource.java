package com.payfix.packages.web.rest;

import com.payfix.packages.repository.LogoTransactionRepository;
import com.payfix.packages.service.LogoTransactionQueryService;
import com.payfix.packages.service.LogoTransactionService;
import com.payfix.packages.service.criteria.LogoTransactionCriteria;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import com.payfix.packages.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.payfix.packages.domain.LogoTransaction}.
 */
@RestController
@RequestMapping("/api")
public class LogoTransactionResource {

    private static final String ENTITY_NAME = "logoTransaction";
    private final Logger log = LoggerFactory.getLogger(LogoTransactionResource.class);
    private final LogoTransactionService logoTransactionService;
    private final LogoTransactionRepository logoTransactionRepository;
    private final LogoTransactionQueryService logoTransactionQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LogoTransactionResource(
        LogoTransactionService logoTransactionService,
        LogoTransactionRepository logoTransactionRepository,
        LogoTransactionQueryService logoTransactionQueryService
    ) {
        this.logoTransactionService = logoTransactionService;
        this.logoTransactionRepository = logoTransactionRepository;
        this.logoTransactionQueryService = logoTransactionQueryService;
    }

    /**
     * {@code POST  /logo-transactions} : Create a new logoTransaction.
     *
     * @param logoTransactionDTO the logoTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new logoTransactionDTO, or with status {@code 400 (Bad Request)} if the logoTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/logo-transactions")
    public ResponseEntity<LogoTransactionDTO> createLogoTransaction(@Valid @RequestBody LogoTransactionDTO logoTransactionDTO)
        throws URISyntaxException {
        log.debug("REST request to save LogoTransaction : {}", logoTransactionDTO);
        if (logoTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new logoTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogoTransactionDTO result = logoTransactionService.save(logoTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/logo-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /logo-transactions/:id} : Updates an existing logoTransaction.
     *
     * @param id                 the id of the logoTransactionDTO to save.
     * @param logoTransactionDTO the logoTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logoTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the logoTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the logoTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/logo-transactions/{id}")
    public ResponseEntity<LogoTransactionDTO> updateLogoTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LogoTransactionDTO logoTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LogoTransaction : {}, {}", id, logoTransactionDTO);
        if (logoTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, logoTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!logoTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LogoTransactionDTO result = logoTransactionService.save(logoTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, logoTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /logo-transactions} : get all the logoTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logoTransactions in body.
     */
    @GetMapping("/logo-transactions")
    public ResponseEntity<List<LogoTransactionDTO>> getAllLogoTransactions(LogoTransactionCriteria criteria) {
        log.debug("REST request to get LogoTransactions by criteria: {}", criteria);
        List<LogoTransactionDTO> entityList = logoTransactionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /logo-transactions/count} : count all the logoTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/logo-transactions/count")
    public ResponseEntity<Long> countLogoTransactions(LogoTransactionCriteria criteria) {
        log.debug("REST request to count LogoTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(logoTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /logo-transactions/:id} : get the "id" logoTransaction.
     *
     * @param id the id of the logoTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the logoTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logo-transactions/{id}")
    public ResponseEntity<LogoTransactionDTO> getLogoTransaction(@PathVariable Long id) {
        log.debug("REST request to get LogoTransaction : {}", id);
        Optional<LogoTransactionDTO> logoTransactionDTO = logoTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(logoTransactionDTO);
    }

    /**
     * {@code DELETE  /logo-transactions/:id} : delete the "id" logoTransaction.
     *
     * @param id the id of the logoTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logo-transactions/{id}")
    public ResponseEntity<Void> deleteLogoTransaction(@PathVariable Long id) {
        log.debug("REST request to delete LogoTransaction : {}", id);
        logoTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
