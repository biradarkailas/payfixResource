package com.payfix.packages.web.rest;

import com.payfix.packages.repository.TransactionTypeRepository;
import com.payfix.packages.service.TransactionTypeService;
import com.payfix.packages.service.dto.TransactionTypeDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.TransactionType}.
 */
@RestController
@RequestMapping("/api")
public class TransactionTypeResource {

    private final Logger log = LoggerFactory.getLogger(TransactionTypeResource.class);

    private static final String ENTITY_NAME = "transactionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionTypeService transactionTypeService;

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeResource(TransactionTypeService transactionTypeService, TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeService = transactionTypeService;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * {@code POST  /transaction-types} : Create a new transactionType.
     *
     * @param transactionTypeDTO the transactionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionTypeDTO, or with status {@code 400 (Bad Request)} if the transactionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-types")
    public ResponseEntity<TransactionTypeDTO> createTransactionType(@Valid @RequestBody TransactionTypeDTO transactionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionType : {}", transactionTypeDTO);
        if (transactionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionTypeDTO result = transactionTypeService.save(transactionTypeDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-types/:id} : Updates an existing transactionType.
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-types/{id}")
    public ResponseEntity<TransactionTypeDTO> updateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionType : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionTypeDTO result = transactionTypeService.save(transactionTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-types/:id} : Partial updates given fields of an existing transactionType, field will ignore if it is null
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TransactionTypeDTO> partialUpdateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionType partially : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionTypeDTO> result = transactionTypeService.partialUpdate(transactionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-types} : get all the transactionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionTypes in body.
     */
    @GetMapping("/transaction-types")
    public List<TransactionTypeDTO> getAllTransactionTypes() {
        log.debug("REST request to get all TransactionTypes");
        return transactionTypeService.findAll();
    }

    /**
     * {@code GET  /transaction-types/:id} : get the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-types/{id}")
    public ResponseEntity<TransactionTypeDTO> getTransactionType(@PathVariable Long id) {
        log.debug("REST request to get TransactionType : {}", id);
        Optional<TransactionTypeDTO> transactionTypeDTO = transactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionTypeDTO);
    }

    /**
     * {@code DELETE  /transaction-types/:id} : delete the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-types/{id}")
    public ResponseEntity<Void> deleteTransactionType(@PathVariable Long id) {
        log.debug("REST request to delete TransactionType : {}", id);
        transactionTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
