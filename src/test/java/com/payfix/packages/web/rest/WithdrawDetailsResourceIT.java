package com.payfix.packages.web.rest;

import static com.payfix.packages.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.User;
import com.payfix.packages.domain.WithdrawDetails;
import com.payfix.packages.domain.WithdrawStatus;
import com.payfix.packages.repository.WithdrawDetailsRepository;
import com.payfix.packages.service.criteria.WithdrawDetailsCriteria;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import com.payfix.packages.service.mapper.WithdrawDetailsMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link WithdrawDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WithdrawDetailsResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/withdraw-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WithdrawDetailsRepository withdrawDetailsRepository;

    @Autowired
    private WithdrawDetailsMapper withdrawDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWithdrawDetailsMockMvc;

    private WithdrawDetails withdrawDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WithdrawDetails createEntity(EntityManager em) {
        WithdrawDetails withdrawDetails = new WithdrawDetails().amount(DEFAULT_AMOUNT).date(DEFAULT_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        withdrawDetails.setUser(user);
        // Add required entity
        WithdrawStatus withdrawStatus;
        if (TestUtil.findAll(em, WithdrawStatus.class).isEmpty()) {
            withdrawStatus = WithdrawStatusResourceIT.createEntity(em);
            em.persist(withdrawStatus);
            em.flush();
        } else {
            withdrawStatus = TestUtil.findAll(em, WithdrawStatus.class).get(0);
        }
        withdrawDetails.setWithdrawStatus(withdrawStatus);
        return withdrawDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WithdrawDetails createUpdatedEntity(EntityManager em) {
        WithdrawDetails withdrawDetails = new WithdrawDetails().amount(UPDATED_AMOUNT).date(UPDATED_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        withdrawDetails.setUser(user);
        // Add required entity
        WithdrawStatus withdrawStatus;
        if (TestUtil.findAll(em, WithdrawStatus.class).isEmpty()) {
            withdrawStatus = WithdrawStatusResourceIT.createUpdatedEntity(em);
            em.persist(withdrawStatus);
            em.flush();
        } else {
            withdrawStatus = TestUtil.findAll(em, WithdrawStatus.class).get(0);
        }
        withdrawDetails.setWithdrawStatus(withdrawStatus);
        return withdrawDetails;
    }

    @BeforeEach
    public void initTest() {
        withdrawDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createWithdrawDetails() throws Exception {
        int databaseSizeBeforeCreate = withdrawDetailsRepository.findAll().size();
        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);
        restWithdrawDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawDetails testWithdrawDetails = withdrawDetailsList.get(withdrawDetailsList.size() - 1);
        assertThat(testWithdrawDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testWithdrawDetails.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createWithdrawDetailsWithExistingId() throws Exception {
        // Create the WithdrawDetails with an existing ID
        withdrawDetails.setId(1L);
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        int databaseSizeBeforeCreate = withdrawDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWithdrawDetails() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getWithdrawDetails() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get the withdrawDetails
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, withdrawDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawDetails.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getWithdrawDetailsByIdFiltering() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        Long id = withdrawDetails.getId();

        defaultWithdrawDetailsShouldBeFound("id.equals=" + id);
        defaultWithdrawDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultWithdrawDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWithdrawDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultWithdrawDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWithdrawDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount equals to DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount equals to UPDATED_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount not equals to DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount not equals to UPDATED_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the withdrawDetailsList where amount equals to UPDATED_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount is not null
        defaultWithdrawDetailsShouldBeFound("amount.specified=true");

        // Get all the withdrawDetailsList where amount is null
        defaultWithdrawDetailsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount is greater than or equal to UPDATED_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount is less than or equal to DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount is less than or equal to SMALLER_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount is less than DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount is less than UPDATED_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where amount is greater than DEFAULT_AMOUNT
        defaultWithdrawDetailsShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the withdrawDetailsList where amount is greater than SMALLER_AMOUNT
        defaultWithdrawDetailsShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date equals to DEFAULT_DATE
        defaultWithdrawDetailsShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date equals to UPDATED_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date not equals to DEFAULT_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date not equals to UPDATED_DATE
        defaultWithdrawDetailsShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date in DEFAULT_DATE or UPDATED_DATE
        defaultWithdrawDetailsShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the withdrawDetailsList where date equals to UPDATED_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date is not null
        defaultWithdrawDetailsShouldBeFound("date.specified=true");

        // Get all the withdrawDetailsList where date is null
        defaultWithdrawDetailsShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date is greater than or equal to DEFAULT_DATE
        defaultWithdrawDetailsShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date is greater than or equal to UPDATED_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date is less than or equal to DEFAULT_DATE
        defaultWithdrawDetailsShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date is less than or equal to SMALLER_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date is less than DEFAULT_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date is less than UPDATED_DATE
        defaultWithdrawDetailsShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        // Get all the withdrawDetailsList where date is greater than DEFAULT_DATE
        defaultWithdrawDetailsShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the withdrawDetailsList where date is greater than SMALLER_DATE
        defaultWithdrawDetailsShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        withdrawDetails.setUser(user);
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);
        Long userId = user.getId();

        // Get all the withdrawDetailsList where user equals to userId
        defaultWithdrawDetailsShouldBeFound("userId.equals=" + userId);

        // Get all the withdrawDetailsList where user equals to (userId + 1)
        defaultWithdrawDetailsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllWithdrawDetailsByWithdrawStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);
        WithdrawStatus withdrawStatus = WithdrawStatusResourceIT.createEntity(em);
        em.persist(withdrawStatus);
        em.flush();
        withdrawDetails.setWithdrawStatus(withdrawStatus);
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);
        Long withdrawStatusId = withdrawStatus.getId();

        // Get all the withdrawDetailsList where withdrawStatus equals to withdrawStatusId
        defaultWithdrawDetailsShouldBeFound("withdrawStatusId.equals=" + withdrawStatusId);

        // Get all the withdrawDetailsList where withdrawStatus equals to (withdrawStatusId + 1)
        defaultWithdrawDetailsShouldNotBeFound("withdrawStatusId.equals=" + (withdrawStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWithdrawDetailsShouldBeFound(String filter) throws Exception {
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));

        // Check, that the count call also returns 1
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWithdrawDetailsShouldNotBeFound(String filter) throws Exception {
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWithdrawDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWithdrawDetails() throws Exception {
        // Get the withdrawDetails
        restWithdrawDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWithdrawDetails() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();

        // Update the withdrawDetails
        WithdrawDetails updatedWithdrawDetails = withdrawDetailsRepository.findById(withdrawDetails.getId()).get();
        // Disconnect from session so that the updates on updatedWithdrawDetails are not directly saved in db
        em.detach(updatedWithdrawDetails);
        updatedWithdrawDetails.amount(UPDATED_AMOUNT).date(UPDATED_DATE);
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(updatedWithdrawDetails);

        restWithdrawDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, withdrawDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
        WithdrawDetails testWithdrawDetails = withdrawDetailsList.get(withdrawDetailsList.size() - 1);
        assertThat(testWithdrawDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testWithdrawDetails.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, withdrawDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWithdrawDetailsWithPatch() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();

        // Update the withdrawDetails using partial update
        WithdrawDetails partialUpdatedWithdrawDetails = new WithdrawDetails();
        partialUpdatedWithdrawDetails.setId(withdrawDetails.getId());

        restWithdrawDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWithdrawDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWithdrawDetails))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
        WithdrawDetails testWithdrawDetails = withdrawDetailsList.get(withdrawDetailsList.size() - 1);
        assertThat(testWithdrawDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testWithdrawDetails.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWithdrawDetailsWithPatch() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();

        // Update the withdrawDetails using partial update
        WithdrawDetails partialUpdatedWithdrawDetails = new WithdrawDetails();
        partialUpdatedWithdrawDetails.setId(withdrawDetails.getId());

        partialUpdatedWithdrawDetails.amount(UPDATED_AMOUNT).date(UPDATED_DATE);

        restWithdrawDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWithdrawDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWithdrawDetails))
            )
            .andExpect(status().isOk());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
        WithdrawDetails testWithdrawDetails = withdrawDetailsList.get(withdrawDetailsList.size() - 1);
        assertThat(testWithdrawDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testWithdrawDetails.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, withdrawDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWithdrawDetails() throws Exception {
        int databaseSizeBeforeUpdate = withdrawDetailsRepository.findAll().size();
        withdrawDetails.setId(count.incrementAndGet());

        // Create the WithdrawDetails
        WithdrawDetailsDTO withdrawDetailsDTO = withdrawDetailsMapper.toDto(withdrawDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWithdrawDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(withdrawDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WithdrawDetails in the database
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWithdrawDetails() throws Exception {
        // Initialize the database
        withdrawDetailsRepository.saveAndFlush(withdrawDetails);

        int databaseSizeBeforeDelete = withdrawDetailsRepository.findAll().size();

        // Delete the withdrawDetails
        restWithdrawDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, withdrawDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WithdrawDetails> withdrawDetailsList = withdrawDetailsRepository.findAll();
        assertThat(withdrawDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
