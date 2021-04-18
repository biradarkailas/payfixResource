package com.payfix.packages.web.rest;

import com.payfix.packages.repository.PricePerDayRepository;
import com.payfix.packages.service.PricePerDayService;
import com.payfix.packages.service.dto.PricePerDayDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.payfix.packages.domain.PricePerDay}.
 */
@RestController
@RequestMapping("/api")
public class PricePerDayResource {

    private final Logger log = LoggerFactory.getLogger(PricePerDayResource.class);

    private static final String ENTITY_NAME = "pricePerDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricePerDayService pricePerDayService;

    private final PricePerDayRepository pricePerDayRepository;

    public PricePerDayResource(PricePerDayService pricePerDayService, PricePerDayRepository pricePerDayRepository) {
        this.pricePerDayService = pricePerDayService;
        this.pricePerDayRepository = pricePerDayRepository;
    }

    /**
     * {@code POST  /price-per-days} : Create a new pricePerDay.
     *
     * @param pricePerDayDTO the pricePerDayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricePerDayDTO, or with status {@code 400 (Bad Request)} if the pricePerDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/price-per-days")
    public ResponseEntity<PricePerDayDTO> createPricePerDay(@Valid @RequestBody PricePerDayDTO pricePerDayDTO) throws URISyntaxException {
        log.debug("REST request to save PricePerDay : {}", pricePerDayDTO);
        if (pricePerDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new pricePerDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PricePerDayDTO result = pricePerDayService.save(pricePerDayDTO);
        return ResponseEntity
            .created(new URI("/api/price-per-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-per-days/:id} : Updates an existing pricePerDay.
     *
     * @param id the id of the pricePerDayDTO to save.
     * @param pricePerDayDTO the pricePerDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricePerDayDTO,
     * or with status {@code 400 (Bad Request)} if the pricePerDayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricePerDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-per-days/{id}")
    public ResponseEntity<PricePerDayDTO> updatePricePerDay(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PricePerDayDTO pricePerDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PricePerDay : {}, {}", id, pricePerDayDTO);
        if (pricePerDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricePerDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricePerDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PricePerDayDTO result = pricePerDayService.save(pricePerDayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pricePerDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-per-days/:id} : Partial updates given fields of an existing pricePerDay, field will ignore if it is null
     *
     * @param id the id of the pricePerDayDTO to save.
     * @param pricePerDayDTO the pricePerDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricePerDayDTO,
     * or with status {@code 400 (Bad Request)} if the pricePerDayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pricePerDayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pricePerDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/price-per-days/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PricePerDayDTO> partialUpdatePricePerDay(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PricePerDayDTO pricePerDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PricePerDay partially : {}, {}", id, pricePerDayDTO);
        if (pricePerDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricePerDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricePerDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PricePerDayDTO> result = pricePerDayService.partialUpdate(pricePerDayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pricePerDayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /price-per-days} : get all the pricePerDays.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricePerDays in body.
     */
    @GetMapping("/price-per-days")
    public ResponseEntity<List<PricePerDayDTO>> getAllPricePerDays(Pageable pageable) {
        log.debug("REST request to get a page of PricePerDays");
        Page<PricePerDayDTO> page = pricePerDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-per-days/:id} : get the "id" pricePerDay.
     *
     * @param id the id of the pricePerDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricePerDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-per-days/{id}")
    public ResponseEntity<PricePerDayDTO> getPricePerDay(@PathVariable Long id) {
        log.debug("REST request to get PricePerDay : {}", id);
        Optional<PricePerDayDTO> pricePerDayDTO = pricePerDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pricePerDayDTO);
    }

    /**
     * {@code DELETE  /price-per-days/:id} : delete the "id" pricePerDay.
     *
     * @param id the id of the pricePerDayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-per-days/{id}")
    public ResponseEntity<Void> deletePricePerDay(@PathVariable Long id) {
        log.debug("REST request to delete PricePerDay : {}", id);
        pricePerDayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
