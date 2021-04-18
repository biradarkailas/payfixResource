package com.payfix.packages.web.rest;

import com.payfix.packages.repository.DurationRepository;
import com.payfix.packages.service.DurationService;
import com.payfix.packages.service.dto.DurationDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.Duration}.
 */
@RestController
@RequestMapping("/api")
public class DurationResource {

    private final Logger log = LoggerFactory.getLogger(DurationResource.class);

    private static final String ENTITY_NAME = "duration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DurationService durationService;

    private final DurationRepository durationRepository;

    public DurationResource(DurationService durationService, DurationRepository durationRepository) {
        this.durationService = durationService;
        this.durationRepository = durationRepository;
    }

    /**
     * {@code POST  /durations} : Create a new duration.
     *
     * @param durationDTO the durationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new durationDTO, or with status {@code 400 (Bad Request)} if the duration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/durations")
    public ResponseEntity<DurationDTO> createDuration(@Valid @RequestBody DurationDTO durationDTO) throws URISyntaxException {
        log.debug("REST request to save Duration : {}", durationDTO);
        if (durationDTO.getId() != null) {
            throw new BadRequestAlertException("A new duration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DurationDTO result = durationService.save(durationDTO);
        return ResponseEntity
            .created(new URI("/api/durations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /durations/:id} : Updates an existing duration.
     *
     * @param id the id of the durationDTO to save.
     * @param durationDTO the durationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated durationDTO,
     * or with status {@code 400 (Bad Request)} if the durationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the durationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/durations/{id}")
    public ResponseEntity<DurationDTO> updateDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DurationDTO durationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Duration : {}, {}", id, durationDTO);
        if (durationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, durationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!durationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DurationDTO result = durationService.save(durationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, durationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /durations/:id} : Partial updates given fields of an existing duration, field will ignore if it is null
     *
     * @param id the id of the durationDTO to save.
     * @param durationDTO the durationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated durationDTO,
     * or with status {@code 400 (Bad Request)} if the durationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the durationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the durationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/durations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DurationDTO> partialUpdateDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DurationDTO durationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Duration partially : {}, {}", id, durationDTO);
        if (durationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, durationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!durationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DurationDTO> result = durationService.partialUpdate(durationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, durationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /durations} : get all the durations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of durations in body.
     */
    @GetMapping("/durations")
    public List<DurationDTO> getAllDurations() {
        log.debug("REST request to get all Durations");
        return durationService.findAll();
    }

    /**
     * {@code GET  /durations/:id} : get the "id" duration.
     *
     * @param id the id of the durationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the durationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/durations/{id}")
    public ResponseEntity<DurationDTO> getDuration(@PathVariable Long id) {
        log.debug("REST request to get Duration : {}", id);
        Optional<DurationDTO> durationDTO = durationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(durationDTO);
    }

    /**
     * {@code DELETE  /durations/:id} : delete the "id" duration.
     *
     * @param id the id of the durationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/durations/{id}")
    public ResponseEntity<Void> deleteDuration(@PathVariable Long id) {
        log.debug("REST request to delete Duration : {}", id);
        durationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
