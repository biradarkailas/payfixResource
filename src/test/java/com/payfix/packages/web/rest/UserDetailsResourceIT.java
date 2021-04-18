package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.User;
import com.payfix.packages.domain.UserDetails;
import com.payfix.packages.repository.UserDetailsRepository;
import com.payfix.packages.service.criteria.UserDetailsCriteria;
import com.payfix.packages.service.dto.UserDetailsDTO;
import com.payfix.packages.service.mapper.UserDetailsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link UserDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDetailsResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_REGISTRATION_NUMBER = 1;
    private static final Integer UPDATED_REGISTRATION_NUMBER = 2;
    private static final Integer SMALLER_REGISTRATION_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    private static final Integer DEFAULT_MOBILE_NUMBER = 1;
    private static final Integer UPDATED_MOBILE_NUMBER = 2;
    private static final Integer SMALLER_MOBILE_NUMBER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/user-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .companyName(DEFAULT_COMPANY_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .balance(DEFAULT_BALANCE)
            .mobileNumber(DEFAULT_MOBILE_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDetails.setUser(user);
        return userDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createUpdatedEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .companyName(UPDATED_COMPANY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .balance(UPDATED_BALANCE)
            .mobileNumber(UPDATED_MOBILE_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userDetails.setUser(user);
        return userDetails;
    }

    @BeforeEach
    public void initTest() {
        userDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createUserDetails() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();
        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);
        restUserDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testUserDetails.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testUserDetails.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testUserDetails.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);

        // Validate the id for MapsId, the ids must be same
        assertThat(testUserDetails.getId()).isEqualTo(testUserDetails.getUser().getId());
    }

    @Test
    @Transactional
    void createUserDetailsWithExistingId() throws Exception {
        // Create the UserDetails with an existing ID
        userDetails.setId(1L);
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateUserDetailsMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).get();
        assertThat(updatedUserDetails).isNotNull();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);

        // Update the User with new association value
        updatedUserDetails.setUser(user);
        UserDetailsDTO updatedUserDetailsDTO = userDetailsMapper.toDto(updatedUserDetails);
        assertThat(updatedUserDetailsDTO).isNotNull();

        // Update the entity
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testUserDetails.getId()).isEqualTo(testUserDetails.getUser().getId());
    }

    @Test
    @Transactional
    void getAllUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)));
    }

    @Test
    @Transactional
    void getUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER));
    }

    @Test
    @Transactional
    void getUserDetailsByIdFiltering() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        Long id = userDetails.getId();

        defaultUserDetailsShouldBeFound("id.equals=" + id);
        defaultUserDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultUserDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultUserDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName equals to DEFAULT_COMPANY_NAME
        defaultUserDetailsShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the userDetailsList where companyName equals to UPDATED_COMPANY_NAME
        defaultUserDetailsShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultUserDetailsShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the userDetailsList where companyName not equals to UPDATED_COMPANY_NAME
        defaultUserDetailsShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultUserDetailsShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the userDetailsList where companyName equals to UPDATED_COMPANY_NAME
        defaultUserDetailsShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName is not null
        defaultUserDetailsShouldBeFound("companyName.specified=true");

        // Get all the userDetailsList where companyName is null
        defaultUserDetailsShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName contains DEFAULT_COMPANY_NAME
        defaultUserDetailsShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the userDetailsList where companyName contains UPDATED_COMPANY_NAME
        defaultUserDetailsShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllUserDetailsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultUserDetailsShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the userDetailsList where companyName does not contain UPDATED_COMPANY_NAME
        defaultUserDetailsShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber equals to DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber not equals to DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.notEquals=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber not equals to UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.notEquals=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber in DEFAULT_REGISTRATION_NUMBER or UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber equals to UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber is not null
        defaultUserDetailsShouldBeFound("registrationNumber.specified=true");

        // Get all the userDetailsList where registrationNumber is null
        defaultUserDetailsShouldNotBeFound("registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber is greater than or equal to DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.greaterThanOrEqual=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber is greater than or equal to UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.greaterThanOrEqual=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber is less than or equal to DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.lessThanOrEqual=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber is less than or equal to SMALLER_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.lessThanOrEqual=" + SMALLER_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber is less than DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.lessThan=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber is less than UPDATED_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.lessThan=" + UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationNumber is greater than DEFAULT_REGISTRATION_NUMBER
        defaultUserDetailsShouldNotBeFound("registrationNumber.greaterThan=" + DEFAULT_REGISTRATION_NUMBER);

        // Get all the userDetailsList where registrationNumber is greater than SMALLER_REGISTRATION_NUMBER
        defaultUserDetailsShouldBeFound("registrationNumber.greaterThan=" + SMALLER_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate is not null
        defaultUserDetailsShouldBeFound("registrationDate.specified=true");

        // Get all the userDetailsList where registrationDate is null
        defaultUserDetailsShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultUserDetailsShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the userDetailsList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultUserDetailsShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance equals to DEFAULT_BALANCE
        defaultUserDetailsShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance equals to UPDATED_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance not equals to DEFAULT_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance not equals to UPDATED_BALANCE
        defaultUserDetailsShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultUserDetailsShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the userDetailsList where balance equals to UPDATED_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance is not null
        defaultUserDetailsShouldBeFound("balance.specified=true");

        // Get all the userDetailsList where balance is null
        defaultUserDetailsShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance is greater than or equal to DEFAULT_BALANCE
        defaultUserDetailsShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance is greater than or equal to UPDATED_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance is less than or equal to DEFAULT_BALANCE
        defaultUserDetailsShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance is less than or equal to SMALLER_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance is less than DEFAULT_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance is less than UPDATED_BALANCE
        defaultUserDetailsShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where balance is greater than DEFAULT_BALANCE
        defaultUserDetailsShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the userDetailsList where balance is greater than SMALLER_BALANCE
        defaultUserDetailsShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber not equals to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.notEquals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber not equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.notEquals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is not null
        defaultUserDetailsShouldBeFound("mobileNumber.specified=true");

        // Get all the userDetailsList where mobileNumber is null
        defaultUserDetailsShouldNotBeFound("mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is greater than or equal to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.greaterThanOrEqual=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber is greater than or equal to UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.greaterThanOrEqual=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is less than or equal to DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.lessThanOrEqual=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber is less than or equal to SMALLER_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.lessThanOrEqual=" + SMALLER_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is less than DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.lessThan=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber is less than UPDATED_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.lessThan=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByMobileNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList where mobileNumber is greater than DEFAULT_MOBILE_NUMBER
        defaultUserDetailsShouldNotBeFound("mobileNumber.greaterThan=" + DEFAULT_MOBILE_NUMBER);

        // Get all the userDetailsList where mobileNumber is greater than SMALLER_MOBILE_NUMBER
        defaultUserDetailsShouldBeFound("mobileNumber.greaterThan=" + SMALLER_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUserDetailsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userDetails.getUser();
        userDetailsRepository.saveAndFlush(userDetails);
        Long userId = user.getId();

        // Get all the userDetailsList where user equals to userId
        defaultUserDetailsShouldBeFound("userId.equals=" + userId);

        // Get all the userDetailsList where user equals to (userId + 1)
        defaultUserDetailsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserDetailsShouldBeFound(String filter) throws Exception {
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)));

        // Check, that the count call also returns 1
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserDetailsShouldNotBeFound(String filter) throws Exception {
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).get();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);
        updatedUserDetails
            .companyName(UPDATED_COMPANY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .balance(UPDATED_BALANCE)
            .mobileNumber(UPDATED_MOBILE_NUMBER);
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(updatedUserDetails);

        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testUserDetails.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testUserDetails.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testUserDetails.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails.registrationDate(UPDATED_REGISTRATION_DATE).mobileNumber(UPDATED_MOBILE_NUMBER);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testUserDetails.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
        assertThat(testUserDetails.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testUserDetails.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails
            .companyName(UPDATED_COMPANY_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .balance(UPDATED_BALANCE)
            .mobileNumber(UPDATED_MOBILE_NUMBER);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testUserDetails.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(testUserDetails.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testUserDetails.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testUserDetails.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // Create the UserDetails
        UserDetailsDTO userDetailsDTO = userDetailsMapper.toDto(userDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeDelete = userDetailsRepository.findAll().size();

        // Delete the userDetails
        restUserDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
