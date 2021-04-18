package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.WithdrawStatus;
import com.payfix.packages.repository.WithdrawStatusRepository;
import com.payfix.packages.service.dto.WithdrawStatusDTO;
import com.payfix.packages.service.mapper.WithdrawStatusMapper;
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
 * Integration tests for the {@link WithdrawStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WithdrawStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/withdraw-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WithdrawStatusRepository withdrawStatusRepository;

    @Autowired
    private WithdrawStatusMapper withdrawStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWithdrawStatusMockMvc;

    private WithdrawStatus withdrawStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WithdrawStatus createEntity(EntityManager em) {
        WithdrawStatus withdrawStatus = new WithdrawStatus().name(DEFAULT_NAME);
        return withdrawStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WithdrawStatus createUpdatedEntity(EntityManager em) {
        WithdrawStatus withdrawStatus = new WithdrawStatus().name(UPDATED_NAME);
        return withdrawStatus;
    }

    @BeforeEach
    public void initTest() {
        withdrawStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createWithdrawStatus() throws Exception {
        int databaseSizeBeforeCreate = withdrawStatusRepository.findAll().size();
        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);
        restWithdrawStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawStatus testWithdrawStatus = withdrawStatusList.get(withdrawStatusList.size() - 1);
        assertThat(testWithdrawStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createWithdrawStatusWithExistingId() throws Exception {
        // Create the WithdrawStatus with an existing ID
        withdrawStatus.setId(1L);
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        int databaseSizeBeforeCreate = withdrawStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = withdrawStatusRepository.findAll().size();
        // set the field null
        withdrawStatus.setName(null);

        // Create the WithdrawStatus, which fails.
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        restWithdrawStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWithdrawStatuses() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        // Get all the withdrawStatusList
        restWithdrawStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getWithdrawStatus() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        // Get the withdrawStatus
        restWithdrawStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, withdrawStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingWithdrawStatus() throws Exception {
        // Get the withdrawStatus
        restWithdrawStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWithdrawStatus() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();

        // Update the withdrawStatus
        WithdrawStatus updatedWithdrawStatus = withdrawStatusRepository.findById(withdrawStatus.getId()).get();
        // Disconnect from session so that the updates on updatedWithdrawStatus are not directly saved in db
        em.detach(updatedWithdrawStatus);
        updatedWithdrawStatus.name(UPDATED_NAME);
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(updatedWithdrawStatus);

        restWithdrawStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, withdrawStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
        WithdrawStatus testWithdrawStatus = withdrawStatusList.get(withdrawStatusList.size() - 1);
        assertThat(testWithdrawStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, withdrawStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWithdrawStatusWithPatch() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();

        // Update the withdrawStatus using partial update
        WithdrawStatus partialUpdatedWithdrawStatus = new WithdrawStatus();
        partialUpdatedWithdrawStatus.setId(withdrawStatus.getId());

        restWithdrawStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWithdrawStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWithdrawStatus))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
        WithdrawStatus testWithdrawStatus = withdrawStatusList.get(withdrawStatusList.size() - 1);
        assertThat(testWithdrawStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateWithdrawStatusWithPatch() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();

        // Update the withdrawStatus using partial update
        WithdrawStatus partialUpdatedWithdrawStatus = new WithdrawStatus();
        partialUpdatedWithdrawStatus.setId(withdrawStatus.getId());

        partialUpdatedWithdrawStatus.name(UPDATED_NAME);

        restWithdrawStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWithdrawStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWithdrawStatus))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
        WithdrawStatus testWithdrawStatus = withdrawStatusList.get(withdrawStatusList.size() - 1);
        assertThat(testWithdrawStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, withdrawStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWithdrawStatus() throws Exception {
        int databaseSizeBeforeUpdate = withdrawStatusRepository.findAll().size();
        withdrawStatus.setId(count.incrementAndGet());

        // Create the WithdrawStatus
        WithdrawStatusDTO withdrawStatusDTO = withdrawStatusMapper.toDto(withdrawStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WithdrawStatus in the database
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWithdrawStatus() throws Exception {
        // Initialize the database
        withdrawStatusRepository.saveAndFlush(withdrawStatus);

        int databaseSizeBeforeDelete = withdrawStatusRepository.findAll().size();

        // Delete the withdrawStatus
        restWithdrawStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, withdrawStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WithdrawStatus> withdrawStatusList = withdrawStatusRepository.findAll();
        assertThat(withdrawStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
