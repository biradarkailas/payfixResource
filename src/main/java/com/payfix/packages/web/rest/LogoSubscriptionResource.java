package com.payfix.packages.web.rest;

import com.payfix.packages.repository.LogoSubscriptionRepository;
import com.payfix.packages.service.LogoSubscriptionQueryService;
import com.payfix.packages.service.LogoSubscriptionService;
import com.payfix.packages.service.criteria.LogoSubscriptionCriteria;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.LogoSubscription}.
 */
@RestController
@RequestMapping("/api")
public class LogoSubscriptionResource {

    private static final String ENTITY_NAME = "logoSubscription";
    private final Logger log = LoggerFactory.getLogger(LogoSubscriptionResource.class);
    private final LogoSubscriptionService logoSubscriptionService;
    private final LogoSubscriptionRepository logoSubscriptionRepository;
    private final LogoSubscriptionQueryService logoSubscriptionQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LogoSubscriptionResource(
        LogoSubscriptionService logoSubscriptionService,
        LogoSubscriptionRepository logoSubscriptionRepository,
        LogoSubscriptionQueryService logoSubscriptionQueryService
    ) {
        this.logoSubscriptionService = logoSubscriptionService;
        this.logoSubscriptionRepository = logoSubscriptionRepository;
        this.logoSubscriptionQueryService = logoSubscriptionQueryService;
    }

    /**
     * {@code POST  /logo-subscriptions} : Create a new logoSubscription.
     *
     * @param logoSubscriptionDTO the logoSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new logoSubscriptionDTO, or with status {@code 400 (Bad Request)} if the logoSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/logo-subscriptions")
    public ResponseEntity<LogoSubscriptionDTO> createLogoSubscription(@Valid @RequestBody LogoSubscriptionDTO logoSubscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save LogoSubscription : {}", logoSubscriptionDTO);
        if (logoSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new logoSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogoSubscriptionDTO result = logoSubscriptionService.save(logoSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/logo-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /logo-subscriptions/:id} : Updates an existing logoSubscription.
     *
     * @param id                  the id of the logoSubscriptionDTO to save.
     * @param logoSubscriptionDTO the logoSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logoSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the logoSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the logoSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/logo-subscriptions/{id}")
    public ResponseEntity<LogoSubscriptionDTO> updateLogoSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LogoSubscriptionDTO logoSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LogoSubscription : {}, {}", id, logoSubscriptionDTO);
        if (logoSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, logoSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!logoSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LogoSubscriptionDTO result = logoSubscriptionService.save(logoSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, logoSubscriptionDTO.getId().toString()))
            .body(result);
    }


    /**
     * {@code GET  /logo-subscriptions} : get all the logoSubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logoSubscriptions in body.
     */
    @GetMapping("/logo-subscriptions")
    public ResponseEntity<List<LogoSubscriptionDTO>> getAllLogoSubscriptions(LogoSubscriptionCriteria criteria) {
        log.debug("REST request to get LogoSubscriptions by criteria: {}", criteria);
        List<LogoSubscriptionDTO> entityList = logoSubscriptionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /logo-subscriptions/count} : count all the logoSubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/logo-subscriptions/count")
    public ResponseEntity<Long> countLogoSubscriptions(LogoSubscriptionCriteria criteria) {
        log.debug("REST request to count LogoSubscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(logoSubscriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /logo-subscriptions/:id} : get the "id" logoSubscription.
     *
     * @param id the id of the logoSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the logoSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logo-subscriptions/{id}")
    public ResponseEntity<LogoSubscriptionDTO> getLogoSubscription(@PathVariable Long id) {
        log.debug("REST request to get LogoSubscription : {}", id);
        Optional<LogoSubscriptionDTO> logoSubscriptionDTO = logoSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(logoSubscriptionDTO);
    }

    /**
     * {@code DELETE  /logo-subscriptions/:id} : delete the "id" logoSubscription.
     *
     * @param id the id of the logoSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logo-subscriptions/{id}")
    public ResponseEntity<Void> deleteLogoSubscription(@PathVariable Long id) {
        log.debug("REST request to delete LogoSubscription : {}", id);
        logoSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
