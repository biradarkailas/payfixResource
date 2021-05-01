package com.payfix.packages.web.rest;

import com.payfix.packages.repository.CompanyDetailsRepository;
import com.payfix.packages.service.CompanyDetailsService;
import com.payfix.packages.service.dto.CompanyDetailsDTO;
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
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.payfix.packages.domain.CompanyDetails}.
 */
@RestController
@RequestMapping("/api")
public class CompanyDetailsResource {

    private static final String ENTITY_NAME = "companyDetails";
    private final Logger log = LoggerFactory.getLogger(CompanyDetailsResource.class);
    private final CompanyDetailsService companyDetailsService;
    private final CompanyDetailsRepository companyDetailsRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CompanyDetailsResource(CompanyDetailsService companyDetailsService, CompanyDetailsRepository companyDetailsRepository) {
        this.companyDetailsService = companyDetailsService;
        this.companyDetailsRepository = companyDetailsRepository;
    }

    /**
     * {@code POST  /company-details} : Create a new companyDetails.
     *
     * @param companyDetailsDTO the companyDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDetailsDTO, or with status {@code 400 (Bad Request)} if the companyDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-details")
    public ResponseEntity<CompanyDetailsDTO> createCompanyDetails(@Valid @RequestBody CompanyDetailsDTO companyDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompanyDetails : {}", companyDetailsDTO);
        if (companyDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(companyDetailsDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        CompanyDetailsDTO result = companyDetailsService.save(companyDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/company-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-details/:id} : Updates an existing companyDetails.
     *
     * @param id                the id of the companyDetailsDTO to save.
     * @param companyDetailsDTO the companyDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the companyDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-details/{id}")
    public ResponseEntity<CompanyDetailsDTO> updateCompanyDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyDetailsDTO companyDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyDetails : {}, {}", id, companyDetailsDTO);
        if (companyDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyDetailsDTO result = companyDetailsService.save(companyDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-details/:id} : Partial updates given fields of an existing companyDetails, field will ignore if it is null
     *
     * @param id                the id of the companyDetailsDTO to save.
     * @param companyDetailsDTO the companyDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the companyDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CompanyDetailsDTO> partialUpdateCompanyDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyDetailsDTO companyDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyDetails partially : {}, {}", id, companyDetailsDTO);
        if (companyDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyDetailsDTO result = companyDetailsService.save(companyDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            Optional.of(result),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, companyDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-details} : get all the companyDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyDetails in body.
     */
    @GetMapping("/company-details")
    public ResponseEntity<List<CompanyDetailsDTO>> getAllCompanyDetails(Pageable pageable) {
        log.debug("REST request to get a page of CompanyDetails");
        Page<CompanyDetailsDTO> page = companyDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-details/:id} : get the "id" companyDetails.
     *
     * @param id the id of the companyDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-details/{id}")
    public ResponseEntity<CompanyDetailsDTO> getCompanyDetails(@PathVariable Long id) {
        log.debug("REST request to get CompanyDetails : {}", id);
        Optional<CompanyDetailsDTO> companyDetailsDTO = companyDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDetailsDTO);
    }

    /**
     * {@code DELETE  /company-details/:id} : delete the "id" companyDetails.
     *
     * @param id the id of the companyDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-details/{id}")
    public ResponseEntity<Void> deleteCompanyDetails(@PathVariable Long id) {
        log.debug("REST request to delete CompanyDetails : {}", id);
        companyDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
