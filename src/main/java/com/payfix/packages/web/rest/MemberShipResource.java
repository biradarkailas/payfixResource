package com.payfix.packages.web.rest;

import com.payfix.packages.repository.MemberShipRepository;
import com.payfix.packages.service.MemberShipService;
import com.payfix.packages.service.dto.MemberShipDTO;
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
 * REST controller for managing {@link com.payfix.packages.domain.MemberShip}.
 */
@RestController
@RequestMapping("/api")
public class MemberShipResource {

    private final Logger log = LoggerFactory.getLogger(MemberShipResource.class);

    private static final String ENTITY_NAME = "memberShip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberShipService memberShipService;

    private final MemberShipRepository memberShipRepository;

    public MemberShipResource(MemberShipService memberShipService, MemberShipRepository memberShipRepository) {
        this.memberShipService = memberShipService;
        this.memberShipRepository = memberShipRepository;
    }

    /**
     * {@code POST  /member-ships} : Create a new memberShip.
     *
     * @param memberShipDTO the memberShipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberShipDTO, or with status {@code 400 (Bad Request)} if the memberShip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-ships")
    public ResponseEntity<MemberShipDTO> createMemberShip(@Valid @RequestBody MemberShipDTO memberShipDTO) throws URISyntaxException {
        log.debug("REST request to save MemberShip : {}", memberShipDTO);
        if (memberShipDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberShip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberShipDTO result = memberShipService.save(memberShipDTO);
        return ResponseEntity
            .created(new URI("/api/member-ships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-ships/:id} : Updates an existing memberShip.
     *
     * @param id the id of the memberShipDTO to save.
     * @param memberShipDTO the memberShipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberShipDTO,
     * or with status {@code 400 (Bad Request)} if the memberShipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberShipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-ships/{id}")
    public ResponseEntity<MemberShipDTO> updateMemberShip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberShipDTO memberShipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MemberShip : {}, {}", id, memberShipDTO);
        if (memberShipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberShipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberShipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MemberShipDTO result = memberShipService.save(memberShipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, memberShipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-ships} : get all the memberShips.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberShips in body.
     */
    @GetMapping("/member-ships")
    public ResponseEntity<List<MemberShipDTO>> getAllMemberShips(Pageable pageable) {
        log.debug("REST request to get a page of MemberShips");
        Page<MemberShipDTO> page = memberShipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /member-ships/:id} : get the "id" memberShip.
     *
     * @param id the id of the memberShipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberShipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-ships/{id}")
    public ResponseEntity<MemberShipDTO> getMemberShip(@PathVariable Long id) {
        log.debug("REST request to get MemberShip : {}", id);
        Optional<MemberShipDTO> memberShipDTO = memberShipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberShipDTO);
    }

    /**
     * {@code DELETE  /member-ships/:id} : delete the "id" memberShip.
     *
     * @param id the id of the memberShipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-ships/{id}")
    public ResponseEntity<Void> deleteMemberShip(@PathVariable Long id) {
        log.debug("REST request to delete MemberShip : {}", id);
        memberShipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
