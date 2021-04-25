package com.payfix.packages.web.rest;

import com.payfix.packages.repository.LogoRepository;
import com.payfix.packages.service.LogoQueryService;
import com.payfix.packages.service.LogoService;
import com.payfix.packages.service.criteria.LogoCriteria;
import com.payfix.packages.service.dto.LogoDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.Logo}.
 */
@RestController
@RequestMapping("/api")
public class LogoResource {

    private static final String ENTITY_NAME = "logo";
    private final Logger log = LoggerFactory.getLogger(LogoResource.class);
    private final LogoService logoService;
    private final LogoRepository logoRepository;
    private final LogoQueryService logoQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LogoResource(LogoService logoService, LogoRepository logoRepository, LogoQueryService logoQueryService) {
        this.logoService = logoService;
        this.logoRepository = logoRepository;
        this.logoQueryService = logoQueryService;
    }

    /**
     * {@code POST  /logos} : Create a new logo.
     *
     * @param logoDTO the logoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new logoDTO, or with status {@code 400 (Bad Request)} if the logo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/logos")
    public ResponseEntity<LogoDTO> createLogo(@Valid @RequestBody LogoDTO logoDTO) throws URISyntaxException {
        log.debug("REST request to save Logo : {}", logoDTO);
        if (logoDTO.getId() != null) {
            throw new BadRequestAlertException("A new logo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogoDTO result = logoService.save(logoDTO);
        return ResponseEntity
            .created(new URI("/api/logos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /logos/:id} : Updates an existing logo.
     *
     * @param id      the id of the logoDTO to save.
     * @param logoDTO the logoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logoDTO,
     * or with status {@code 400 (Bad Request)} if the logoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the logoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/logos/{id}")
    public ResponseEntity<LogoDTO> updateLogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LogoDTO logoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Logo : {}, {}", id, logoDTO);
        if (logoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, logoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!logoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LogoDTO result = logoService.save(logoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, logoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /logos} : get all the logos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logos in body.
     */
    @GetMapping("/logos")
    public ResponseEntity<List<LogoDTO>> getAllLogos(LogoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Logos by criteria: {}", criteria);
        Page<LogoDTO> page = logoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logos/count} : count all the logos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/logos/count")
    public ResponseEntity<Long> countLogos(LogoCriteria criteria) {
        log.debug("REST request to count Logos by criteria: {}", criteria);
        return ResponseEntity.ok().body(logoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /logos/:id} : get the "id" logo.
     *
     * @param id the id of the logoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the logoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logos/{id}")
    public ResponseEntity<LogoDTO> getLogo(@PathVariable Long id) {
        log.debug("REST request to get Logo : {}", id);
        Optional<LogoDTO> logoDTO = logoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(logoDTO);
    }

    /**
     * {@code DELETE  /logos/:id} : delete the "id" logo.
     *
     * @param id the id of the logoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logos/{id}")
    public ResponseEntity<Void> deleteLogo(@PathVariable Long id) {
        log.debug("REST request to delete Logo : {}", id);
        logoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
