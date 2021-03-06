package com.payfix.packages.web.rest;

import com.payfix.packages.repository.WithdrawDetailsRepository;
import com.payfix.packages.service.WithdrawDetailsQueryService;
import com.payfix.packages.service.WithdrawDetailsService;
import com.payfix.packages.service.criteria.WithdrawDetailsCriteria;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import com.payfix.packages.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.payfix.packages.domain.WithdrawDetails}.
 */
@RestController
@RequestMapping("/api")
public class WithdrawDetailsResource {

    private static final String ENTITY_NAME = "withdrawDetails";
    private final Logger log = LoggerFactory.getLogger(WithdrawDetailsResource.class);
    private final WithdrawDetailsService withdrawDetailsService;
    private final WithdrawDetailsRepository withdrawDetailsRepository;
    private final WithdrawDetailsQueryService withdrawDetailsQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public WithdrawDetailsResource(
        WithdrawDetailsService withdrawDetailsService,
        WithdrawDetailsRepository withdrawDetailsRepository,
        WithdrawDetailsQueryService withdrawDetailsQueryService
    ) {
        this.withdrawDetailsService = withdrawDetailsService;
        this.withdrawDetailsRepository = withdrawDetailsRepository;
        this.withdrawDetailsQueryService = withdrawDetailsQueryService;
    }

    /**
     * {@code POST  /withdraw-details} : Create a new withdrawDetails.
     *
     * @param withdrawDetailsDTO the withdrawDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new withdrawDetailsDTO, or with status {@code 400 (Bad Request)} if the withdrawDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/withdraw-details")
    public ResponseEntity<WithdrawDetailsDTO> createWithdrawDetails(@Valid @RequestBody WithdrawDetailsDTO withdrawDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save WithdrawDetails : {}", withdrawDetailsDTO);
        if (withdrawDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new withdrawDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawDetailsDTO result = withdrawDetailsService.save(withdrawDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/withdraw-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /withdraw-details/:id} : Updates an existing withdrawDetails.
     *
     * @param id                 the id of the withdrawDetailsDTO to save.
     * @param withdrawDetailsDTO the withdrawDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated withdrawDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the withdrawDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the withdrawDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/withdraw-details/{id}")
    public ResponseEntity<WithdrawDetailsDTO> updateWithdrawDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WithdrawDetailsDTO withdrawDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WithdrawDetails : {}, {}", id, withdrawDetailsDTO);
        if (withdrawDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, withdrawDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!withdrawDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WithdrawDetailsDTO result = withdrawDetailsService.save(withdrawDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, withdrawDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /withdraw-details} : get all the withdrawDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of withdrawDetails in body.
     */
    @GetMapping("/withdraw-details")
    public ResponseEntity<List<WithdrawDetailsDTO>> getAllWithdrawDetails(WithdrawDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WithdrawDetails by criteria: {}", criteria);
        Page<WithdrawDetailsDTO> page = withdrawDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /withdraw-details/count} : count all the withdrawDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/withdraw-details/count")
    public ResponseEntity<Long> countWithdrawDetails(WithdrawDetailsCriteria criteria) {
        log.debug("REST request to count WithdrawDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(withdrawDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /withdraw-details/:id} : get the "id" withdrawDetails.
     *
     * @param id the id of the withdrawDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the withdrawDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/withdraw-details/{id}")
    public ResponseEntity<WithdrawDetailsDTO> getWithdrawDetails(@PathVariable Long id) {
        log.debug("REST request to get WithdrawDetails : {}", id);
        Optional<WithdrawDetailsDTO> withdrawDetailsDTO = withdrawDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(withdrawDetailsDTO);
    }

    /**
     * {@code DELETE  /withdraw-details/:id} : delete the "id" withdrawDetails.
     *
     * @param id the id of the withdrawDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/withdraw-details/{id}")
    public ResponseEntity<Void> deleteWithdrawDetails(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawDetails : {}", id);
        withdrawDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
