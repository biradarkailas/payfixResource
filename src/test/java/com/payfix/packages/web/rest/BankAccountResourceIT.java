package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.BankAccount;
import com.payfix.packages.repository.BankAccountRepository;
import com.payfix.packages.service.criteria.BankAccountCriteria;
import com.payfix.packages.service.dto.BankAccountDTO;
import com.payfix.packages.service.mapper.BankAccountMapper;
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
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IFSC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_IFSC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .name(DEFAULT_NAME)
            .ifscCode(DEFAULT_IFSC_CODE)
            .bankName(DEFAULT_BANK_NAME);
        return bankAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .name(UPDATED_NAME)
            .ifscCode(UPDATED_IFSC_CODE)
            .bankName(UPDATED_BANK_NAME);
        return bankAccount;
    }

    @BeforeEach
    public void initTest() {
        bankAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();
        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);
        restBankAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBankAccount.getIfscCode()).isEqualTo(DEFAULT_IFSC_CODE);
        assertThat(testBankAccount.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
    }

    @Test
    @Transactional
    void createBankAccountWithExistingId() throws Exception {
        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setAccountNumber(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        restBankAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ifscCode").value(hasItem(DEFAULT_IFSC_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));
    }

    @Test
    @Transactional
    void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ifscCode").value(DEFAULT_IFSC_CODE))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME));
    }

    @Test
    @Transactional
    void getBankAccountsByIdFiltering() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        Long id = bankAccount.getId();

        defaultBankAccountShouldBeFound("id.equals=" + id);
        defaultBankAccountShouldNotBeFound("id.notEquals=" + id);

        defaultBankAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultBankAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber is not null
        defaultBankAccountShouldBeFound("accountNumber.specified=true");

        // Get all the bankAccountList where accountNumber is null
        defaultBankAccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultBankAccountShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the bankAccountList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultBankAccountShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name equals to DEFAULT_NAME
        defaultBankAccountShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the bankAccountList where name equals to UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name not equals to DEFAULT_NAME
        defaultBankAccountShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the bankAccountList where name not equals to UPDATED_NAME
        defaultBankAccountShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBankAccountShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the bankAccountList where name equals to UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name is not null
        defaultBankAccountShouldBeFound("name.specified=true");

        // Get all the bankAccountList where name is null
        defaultBankAccountShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name contains DEFAULT_NAME
        defaultBankAccountShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the bankAccountList where name contains UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name does not contain DEFAULT_NAME
        defaultBankAccountShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the bankAccountList where name does not contain UPDATED_NAME
        defaultBankAccountShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode equals to DEFAULT_IFSC_CODE
        defaultBankAccountShouldBeFound("ifscCode.equals=" + DEFAULT_IFSC_CODE);

        // Get all the bankAccountList where ifscCode equals to UPDATED_IFSC_CODE
        defaultBankAccountShouldNotBeFound("ifscCode.equals=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode not equals to DEFAULT_IFSC_CODE
        defaultBankAccountShouldNotBeFound("ifscCode.notEquals=" + DEFAULT_IFSC_CODE);

        // Get all the bankAccountList where ifscCode not equals to UPDATED_IFSC_CODE
        defaultBankAccountShouldBeFound("ifscCode.notEquals=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode in DEFAULT_IFSC_CODE or UPDATED_IFSC_CODE
        defaultBankAccountShouldBeFound("ifscCode.in=" + DEFAULT_IFSC_CODE + "," + UPDATED_IFSC_CODE);

        // Get all the bankAccountList where ifscCode equals to UPDATED_IFSC_CODE
        defaultBankAccountShouldNotBeFound("ifscCode.in=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode is not null
        defaultBankAccountShouldBeFound("ifscCode.specified=true");

        // Get all the bankAccountList where ifscCode is null
        defaultBankAccountShouldNotBeFound("ifscCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode contains DEFAULT_IFSC_CODE
        defaultBankAccountShouldBeFound("ifscCode.contains=" + DEFAULT_IFSC_CODE);

        // Get all the bankAccountList where ifscCode contains UPDATED_IFSC_CODE
        defaultBankAccountShouldNotBeFound("ifscCode.contains=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllBankAccountsByIfscCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where ifscCode does not contain DEFAULT_IFSC_CODE
        defaultBankAccountShouldNotBeFound("ifscCode.doesNotContain=" + DEFAULT_IFSC_CODE);

        // Get all the bankAccountList where ifscCode does not contain UPDATED_IFSC_CODE
        defaultBankAccountShouldBeFound("ifscCode.doesNotContain=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName equals to DEFAULT_BANK_NAME
        defaultBankAccountShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the bankAccountList where bankName equals to UPDATED_BANK_NAME
        defaultBankAccountShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName not equals to DEFAULT_BANK_NAME
        defaultBankAccountShouldNotBeFound("bankName.notEquals=" + DEFAULT_BANK_NAME);

        // Get all the bankAccountList where bankName not equals to UPDATED_BANK_NAME
        defaultBankAccountShouldBeFound("bankName.notEquals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultBankAccountShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the bankAccountList where bankName equals to UPDATED_BANK_NAME
        defaultBankAccountShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName is not null
        defaultBankAccountShouldBeFound("bankName.specified=true");

        // Get all the bankAccountList where bankName is null
        defaultBankAccountShouldNotBeFound("bankName.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName contains DEFAULT_BANK_NAME
        defaultBankAccountShouldBeFound("bankName.contains=" + DEFAULT_BANK_NAME);

        // Get all the bankAccountList where bankName contains UPDATED_BANK_NAME
        defaultBankAccountShouldNotBeFound("bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName does not contain DEFAULT_BANK_NAME
        defaultBankAccountShouldNotBeFound("bankName.doesNotContain=" + DEFAULT_BANK_NAME);

        // Get all the bankAccountList where bankName does not contain UPDATED_BANK_NAME
        defaultBankAccountShouldBeFound("bankName.doesNotContain=" + UPDATED_BANK_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankAccountShouldBeFound(String filter) throws Exception {
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ifscCode").value(hasItem(DEFAULT_IFSC_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));

        // Check, that the count call also returns 1
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankAccountShouldNotBeFound(String filter) throws Exception {
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).get();
        // Disconnect from session so that the updates on updatedBankAccount are not directly saved in db
        em.detach(updatedBankAccount);
        updatedBankAccount.accountNumber(UPDATED_ACCOUNT_NUMBER).name(UPDATED_NAME).ifscCode(UPDATED_IFSC_CODE).bankName(UPDATED_BANK_NAME);
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(updatedBankAccount);

        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBankAccount.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
        assertThat(testBankAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount.name(UPDATED_NAME).ifscCode(UPDATED_IFSC_CODE);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBankAccount.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
        assertThat(testBankAccount.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .name(UPDATED_NAME)
            .ifscCode(UPDATED_IFSC_CODE)
            .bankName(UPDATED_BANK_NAME);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);
        assertThat(testBankAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBankAccount.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
        assertThat(testBankAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();
        bankAccount.setId(count.incrementAndGet());

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.toDto(bankAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bankAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Delete the bankAccount
        restBankAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
