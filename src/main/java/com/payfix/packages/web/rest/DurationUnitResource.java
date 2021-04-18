package com.payfix.packages.web.rest;

import com.payfix.packages.repository.DurationUnitRepository;
import com.payfix.packages.service.DurationUnitQueryService;
import com.payfix.packages.service.DurationUnitService;
import com.payfix.packages.service.criteria.DurationUnitCriteria;
import com.payfix.packages.service.dto.DurationUnitDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.DurationUnit}.
 */
@RestController
@RequestMapping("/api")
public class DurationUnitResource {

    private final Logger log = LoggerFactory.getLogger(DurationUnitResource.class);

    private static final String ENTITY_NAME = "durationUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DurationUnitService durationUnitService;

    private final DurationUnitRepository durationUnitRepository;

    private final DurationUnitQueryService durationUnitQueryService;

    public DurationUnitResource(
        DurationUnitService durationUnitService,
        DurationUnitRepository durationUnitRepository,
        DurationUnitQueryService durationUnitQueryService
    ) {
        this.durationUnitService = durationUnitService;
        this.durationUnitRepository = durationUnitRepository;
        this.durationUnitQueryService = durationUnitQueryService;
    }

    /**
     * {@code POST  /duration-units} : Create a new durationUnit.
     *
     * @param durationUnitDTO the durationUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new durationUnitDTO, or with status {@code 400 (Bad Request)} if the durationUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/duration-units")
    public ResponseEntity<DurationUnitDTO> createDurationUnit(@Valid @RequestBody DurationUnitDTO durationUnitDTO)
        throws URISyntaxException {
        log.debug("REST request to save DurationUnit : {}", durationUnitDTO);
        if (durationUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new durationUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DurationUnitDTO result = durationUnitService.save(durationUnitDTO);
        return ResponseEntity
            .created(new URI("/api/duration-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /duration-units/:id} : Updates an existing durationUnit.
     *
     * @param id the id of the durationUnitDTO to save.
     * @param durationUnitDTO the durationUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated durationUnitDTO,
     * or with status {@code 400 (Bad Request)} if the durationUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the durationUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/duration-units/{id}")
    public ResponseEntity<DurationUnitDTO> updateDurationUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DurationUnitDTO durationUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DurationUnit : {}, {}", id, durationUnitDTO);
        if (durationUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, durationUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!durationUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DurationUnitDTO result = durationUnitService.save(durationUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, durationUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /duration-units/:id} : Partial updates given fields of an existing durationUnit, field will ignore if it is null
     *
     * @param id the id of the durationUnitDTO to save.
     * @param durationUnitDTO the durationUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated durationUnitDTO,
     * or with status {@code 400 (Bad Request)} if the durationUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the durationUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the durationUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/duration-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DurationUnitDTO> partialUpdateDurationUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DurationUnitDTO durationUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DurationUnit partially : {}, {}", id, durationUnitDTO);
        if (durationUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, durationUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!durationUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DurationUnitDTO> result = durationUnitService.partialUpdate(durationUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, durationUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /duration-units} : get all the durationUnits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of durationUnits in body.
     */
    @GetMapping("/duration-units")
    public ResponseEntity<List<DurationUnitDTO>> getAllDurationUnits(DurationUnitCriteria criteria) {
        log.debug("REST request to get DurationUnits by criteria: {}", criteria);
        List<DurationUnitDTO> entityList = durationUnitQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /duration-units/count} : count all the durationUnits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/duration-units/count")
    public ResponseEntity<Long> countDurationUnits(DurationUnitCriteria criteria) {
        log.debug("REST request to count DurationUnits by criteria: {}", criteria);
        return ResponseEntity.ok().body(durationUnitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /duration-units/:id} : get the "id" durationUnit.
     *
     * @param id the id of the durationUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the durationUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/duration-units/{id}")
    public ResponseEntity<DurationUnitDTO> getDurationUnit(@PathVariable Long id) {
        log.debug("REST request to get DurationUnit : {}", id);
        Optional<DurationUnitDTO> durationUnitDTO = durationUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(durationUnitDTO);
    }

    /**
     * {@code DELETE  /duration-units/:id} : delete the "id" durationUnit.
     *
     * @param id the id of the durationUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/duration-units/{id}")
    public ResponseEntity<Void> deleteDurationUnit(@PathVariable Long id) {
        log.debug("REST request to delete DurationUnit : {}", id);
        durationUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
