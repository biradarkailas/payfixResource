package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.MemberShip;
import com.payfix.packages.repository.MemberShipRepository;
import com.payfix.packages.service.dto.MemberShipDTO;
import com.payfix.packages.service.mapper.MemberShipMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MemberShipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemberShipResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/member-ships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberShipRepository memberShipRepository;

    @Autowired
    private MemberShipMapper memberShipMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberShipMockMvc;

    private MemberShip memberShip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberShip createEntity(EntityManager em) {
        MemberShip memberShip = new MemberShip().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).price(DEFAULT_PRICE);
        return memberShip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberShip createUpdatedEntity(EntityManager em) {
        MemberShip memberShip = new MemberShip().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);
        return memberShip;
    }

    @BeforeEach
    public void initTest() {
        memberShip = createEntity(em);
    }

    @Test
    @Transactional
    void createMemberShip() throws Exception {
        int databaseSizeBeforeCreate = memberShipRepository.findAll().size();
        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);
        restMemberShipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberShipDTO)))
            .andExpect(status().isCreated());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeCreate + 1);
        MemberShip testMemberShip = memberShipList.get(memberShipList.size() - 1);
        assertThat(testMemberShip.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMemberShip.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMemberShip.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createMemberShipWithExistingId() throws Exception {
        // Create the MemberShip with an existing ID
        memberShip.setId(1L);
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        int databaseSizeBeforeCreate = memberShipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberShipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberShipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberShipRepository.findAll().size();
        // set the field null
        memberShip.setTitle(null);

        // Create the MemberShip, which fails.
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        restMemberShipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberShipDTO)))
            .andExpect(status().isBadRequest());

        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberShips() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        // Get all the memberShipList
        restMemberShipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberShip.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getMemberShip() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        // Get the memberShip
        restMemberShipMockMvc
            .perform(get(ENTITY_API_URL_ID, memberShip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberShip.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMemberShip() throws Exception {
        // Get the memberShip
        restMemberShipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMemberShip() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();

        // Update the memberShip
        MemberShip updatedMemberShip = memberShipRepository.findById(memberShip.getId()).get();
        // Disconnect from session so that the updates on updatedMemberShip are not directly saved in db
        em.detach(updatedMemberShip);
        updatedMemberShip.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(updatedMemberShip);

        restMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberShipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isOk());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
        MemberShip testMemberShip = memberShipList.get(memberShipList.size() - 1);
        assertThat(testMemberShip.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemberShip.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMemberShip.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberShipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memberShipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberShipWithPatch() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();

        // Update the memberShip using partial update
        MemberShip partialUpdatedMemberShip = new MemberShip();
        partialUpdatedMemberShip.setId(memberShip.getId());

        restMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberShip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberShip))
            )
            .andExpect(status().isOk());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
        MemberShip testMemberShip = memberShipList.get(memberShipList.size() - 1);
        assertThat(testMemberShip.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMemberShip.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMemberShip.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateMemberShipWithPatch() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();

        // Update the memberShip using partial update
        MemberShip partialUpdatedMemberShip = new MemberShip();
        partialUpdatedMemberShip.setId(memberShip.getId());

        partialUpdatedMemberShip.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberShip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberShip))
            )
            .andExpect(status().isOk());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
        MemberShip testMemberShip = memberShipList.get(memberShipList.size() - 1);
        assertThat(testMemberShip.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemberShip.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMemberShip.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberShipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = memberShipRepository.findAll().size();
        memberShip.setId(count.incrementAndGet());

        // Create the MemberShip
        MemberShipDTO memberShipDTO = memberShipMapper.toDto(memberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(memberShipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberShip in the database
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemberShip() throws Exception {
        // Initialize the database
        memberShipRepository.saveAndFlush(memberShip);

        int databaseSizeBeforeDelete = memberShipRepository.findAll().size();

        // Delete the memberShip
        restMemberShipMockMvc
            .perform(delete(ENTITY_API_URL_ID, memberShip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberShip> memberShipList = memberShipRepository.findAll();
        assertThat(memberShipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
