package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.Logo;
import com.payfix.packages.domain.LogoTransaction;
import com.payfix.packages.domain.TransactionType;
import com.payfix.packages.domain.User;
import com.payfix.packages.repository.LogoTransactionRepository;
import com.payfix.packages.service.criteria.LogoTransactionCriteria;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import com.payfix.packages.service.mapper.LogoTransactionMapper;
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
 * Integration tests for the {@link LogoTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LogoTransactionResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/logo-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LogoTransactionRepository logoTransactionRepository;

    @Autowired
    private LogoTransactionMapper logoTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLogoTransactionMockMvc;

    private LogoTransaction logoTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LogoTransaction createEntity(EntityManager em) {
        LogoTransaction logoTransaction = new LogoTransaction().amount(DEFAULT_AMOUNT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoTransaction.setUser(user);
        return logoTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LogoTransaction createUpdatedEntity(EntityManager em) {
        LogoTransaction logoTransaction = new LogoTransaction().amount(UPDATED_AMOUNT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoTransaction.setUser(user);
        return logoTransaction;
    }

    @BeforeEach
    public void initTest() {
        logoTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createLogoTransaction() throws Exception {
        int databaseSizeBeforeCreate = logoTransactionRepository.findAll().size();
        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);
        restLogoTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        LogoTransaction testLogoTransaction = logoTransactionList.get(logoTransactionList.size() - 1);
        assertThat(testLogoTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createLogoTransactionWithExistingId() throws Exception {
        // Create the LogoTransaction with an existing ID
        logoTransaction.setId(1L);
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        int databaseSizeBeforeCreate = logoTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogoTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = logoTransactionRepository.findAll().size();
        // set the field null
        logoTransaction.setAmount(null);

        // Create the LogoTransaction, which fails.
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        restLogoTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLogoTransactions() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logoTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getLogoTransaction() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get the logoTransaction
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, logoTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(logoTransaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getLogoTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        Long id = logoTransaction.getId();

        defaultLogoTransactionShouldBeFound("id.equals=" + id);
        defaultLogoTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultLogoTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLogoTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultLogoTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLogoTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount equals to DEFAULT_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount equals to UPDATED_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount not equals to DEFAULT_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount not equals to UPDATED_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the logoTransactionList where amount equals to UPDATED_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount is not null
        defaultLogoTransactionShouldBeFound("amount.specified=true");

        // Get all the logoTransactionList where amount is null
        defaultLogoTransactionShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount is greater than or equal to UPDATED_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount is less than or equal to DEFAULT_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount is less than or equal to SMALLER_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount is less than DEFAULT_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount is less than UPDATED_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        // Get all the logoTransactionList where amount is greater than DEFAULT_AMOUNT
        defaultLogoTransactionShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the logoTransactionList where amount is greater than SMALLER_AMOUNT
        defaultLogoTransactionShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoTransaction.setUser(user);
        logoTransactionRepository.saveAndFlush(logoTransaction);
        Long userId = user.getId();

        // Get all the logoTransactionList where user equals to userId
        defaultLogoTransactionShouldBeFound("userId.equals=" + userId);

        // Get all the logoTransactionList where user equals to (userId + 1)
        defaultLogoTransactionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);
        Logo logo = LogoResourceIT.createEntity(em);
        em.persist(logo);
        em.flush();
        logoTransaction.setLogo(logo);
        logoTransactionRepository.saveAndFlush(logoTransaction);
        Long logoId = logo.getId();

        // Get all the logoTransactionList where logo equals to logoId
        defaultLogoTransactionShouldBeFound("logoId.equals=" + logoId);

        // Get all the logoTransactionList where logo equals to (logoId + 1)
        defaultLogoTransactionShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }

    @Test
    @Transactional
    void getAllLogoTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        logoTransaction.setTransactionType(transactionType);
        logoTransactionRepository.saveAndFlush(logoTransaction);
        Long transactionTypeId = transactionType.getId();

        // Get all the logoTransactionList where transactionType equals to transactionTypeId
        defaultLogoTransactionShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the logoTransactionList where transactionType equals to (transactionTypeId + 1)
        defaultLogoTransactionShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLogoTransactionShouldBeFound(String filter) throws Exception {
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logoTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLogoTransactionShouldNotBeFound(String filter) throws Exception {
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLogoTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLogoTransaction() throws Exception {
        // Get the logoTransaction
        restLogoTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLogoTransaction() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();

        // Update the logoTransaction
        LogoTransaction updatedLogoTransaction = logoTransactionRepository.findById(logoTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedLogoTransaction are not directly saved in db
        em.detach(updatedLogoTransaction);
        updatedLogoTransaction.amount(UPDATED_AMOUNT);
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(updatedLogoTransaction);

        restLogoTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
        LogoTransaction testLogoTransaction = logoTransactionList.get(logoTransactionList.size() - 1);
        assertThat(testLogoTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLogoTransactionWithPatch() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();

        // Update the logoTransaction using partial update
        LogoTransaction partialUpdatedLogoTransaction = new LogoTransaction();
        partialUpdatedLogoTransaction.setId(logoTransaction.getId());

        restLogoTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogoTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogoTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
        LogoTransaction testLogoTransaction = logoTransactionList.get(logoTransactionList.size() - 1);
        assertThat(testLogoTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateLogoTransactionWithPatch() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();

        // Update the logoTransaction using partial update
        LogoTransaction partialUpdatedLogoTransaction = new LogoTransaction();
        partialUpdatedLogoTransaction.setId(logoTransaction.getId());

        partialUpdatedLogoTransaction.amount(UPDATED_AMOUNT);

        restLogoTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogoTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogoTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
        LogoTransaction testLogoTransaction = logoTransactionList.get(logoTransactionList.size() - 1);
        assertThat(testLogoTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, logoTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLogoTransaction() throws Exception {
        int databaseSizeBeforeUpdate = logoTransactionRepository.findAll().size();
        logoTransaction.setId(count.incrementAndGet());

        // Create the LogoTransaction
        LogoTransactionDTO logoTransactionDTO = logoTransactionMapper.toDto(logoTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LogoTransaction in the database
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLogoTransaction() throws Exception {
        // Initialize the database
        logoTransactionRepository.saveAndFlush(logoTransaction);

        int databaseSizeBeforeDelete = logoTransactionRepository.findAll().size();

        // Delete the logoTransaction
        restLogoTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, logoTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LogoTransaction> logoTransactionList = logoTransactionRepository.findAll();
        assertThat(logoTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
