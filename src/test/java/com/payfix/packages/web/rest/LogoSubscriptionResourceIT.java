package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.Logo;
import com.payfix.packages.domain.LogoSubscription;
import com.payfix.packages.domain.User;
import com.payfix.packages.repository.LogoSubscriptionRepository;
import com.payfix.packages.service.criteria.LogoSubscriptionCriteria;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import com.payfix.packages.service.mapper.LogoSubscriptionMapper;
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
 * Integration tests for the {@link LogoSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LogoSubscriptionResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/logo-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LogoSubscriptionRepository logoSubscriptionRepository;

    @Autowired
    private LogoSubscriptionMapper logoSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLogoSubscriptionMockMvc;

    private LogoSubscription logoSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LogoSubscription createEntity(EntityManager em) {
        LogoSubscription logoSubscription = new LogoSubscription().date(DEFAULT_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoSubscription.setUser(user);
        // Add required entity
        Logo logo;
        if (TestUtil.findAll(em, Logo.class).isEmpty()) {
            logo = LogoResourceIT.createEntity(em);
            em.persist(logo);
            em.flush();
        } else {
            logo = TestUtil.findAll(em, Logo.class).get(0);
        }
        logoSubscription.setLogo(logo);
        return logoSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LogoSubscription createUpdatedEntity(EntityManager em) {
        LogoSubscription logoSubscription = new LogoSubscription().date(UPDATED_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoSubscription.setUser(user);
        // Add required entity
        Logo logo;
        if (TestUtil.findAll(em, Logo.class).isEmpty()) {
            logo = LogoResourceIT.createUpdatedEntity(em);
            em.persist(logo);
            em.flush();
        } else {
            logo = TestUtil.findAll(em, Logo.class).get(0);
        }
        logoSubscription.setLogo(logo);
        return logoSubscription;
    }

    @BeforeEach
    public void initTest() {
        logoSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createLogoSubscription() throws Exception {
        int databaseSizeBeforeCreate = logoSubscriptionRepository.findAll().size();
        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);
        restLogoSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        LogoSubscription testLogoSubscription = logoSubscriptionList.get(logoSubscriptionList.size() - 1);
        assertThat(testLogoSubscription.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createLogoSubscriptionWithExistingId() throws Exception {
        // Create the LogoSubscription with an existing ID
        logoSubscription.setId(1L);
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        int databaseSizeBeforeCreate = logoSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogoSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = logoSubscriptionRepository.findAll().size();
        // set the field null
        logoSubscription.setDate(null);

        // Create the LogoSubscription, which fails.
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        restLogoSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptions() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logoSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getLogoSubscription() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get the logoSubscription
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, logoSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(logoSubscription.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getLogoSubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        Long id = logoSubscription.getId();

        defaultLogoSubscriptionShouldBeFound("id.equals=" + id);
        defaultLogoSubscriptionShouldNotBeFound("id.notEquals=" + id);

        defaultLogoSubscriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLogoSubscriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultLogoSubscriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLogoSubscriptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date equals to DEFAULT_DATE
        defaultLogoSubscriptionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date equals to UPDATED_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date not equals to DEFAULT_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date not equals to UPDATED_DATE
        defaultLogoSubscriptionShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultLogoSubscriptionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the logoSubscriptionList where date equals to UPDATED_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date is not null
        defaultLogoSubscriptionShouldBeFound("date.specified=true");

        // Get all the logoSubscriptionList where date is null
        defaultLogoSubscriptionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date is greater than or equal to DEFAULT_DATE
        defaultLogoSubscriptionShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date is greater than or equal to UPDATED_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date is less than or equal to DEFAULT_DATE
        defaultLogoSubscriptionShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date is less than or equal to SMALLER_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date is less than DEFAULT_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date is less than UPDATED_DATE
        defaultLogoSubscriptionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        // Get all the logoSubscriptionList where date is greater than DEFAULT_DATE
        defaultLogoSubscriptionShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the logoSubscriptionList where date is greater than SMALLER_DATE
        defaultLogoSubscriptionShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logoSubscription.setUser(user);
        logoSubscriptionRepository.saveAndFlush(logoSubscription);
        Long userId = user.getId();

        // Get all the logoSubscriptionList where user equals to userId
        defaultLogoSubscriptionShouldBeFound("userId.equals=" + userId);

        // Get all the logoSubscriptionList where user equals to (userId + 1)
        defaultLogoSubscriptionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllLogoSubscriptionsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);
        Logo logo = LogoResourceIT.createEntity(em);
        em.persist(logo);
        em.flush();
        logoSubscription.setLogo(logo);
        logoSubscriptionRepository.saveAndFlush(logoSubscription);
        Long logoId = logo.getId();

        // Get all the logoSubscriptionList where logo equals to logoId
        defaultLogoSubscriptionShouldBeFound("logoId.equals=" + logoId);

        // Get all the logoSubscriptionList where logo equals to (logoId + 1)
        defaultLogoSubscriptionShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLogoSubscriptionShouldBeFound(String filter) throws Exception {
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logoSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLogoSubscriptionShouldNotBeFound(String filter) throws Exception {
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLogoSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLogoSubscription() throws Exception {
        // Get the logoSubscription
        restLogoSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLogoSubscription() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();

        // Update the logoSubscription
        LogoSubscription updatedLogoSubscription = logoSubscriptionRepository.findById(logoSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedLogoSubscription are not directly saved in db
        em.detach(updatedLogoSubscription);
        updatedLogoSubscription.date(UPDATED_DATE);
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(updatedLogoSubscription);

        restLogoSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LogoSubscription testLogoSubscription = logoSubscriptionList.get(logoSubscriptionList.size() - 1);
        assertThat(testLogoSubscription.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLogoSubscriptionWithPatch() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();

        // Update the logoSubscription using partial update
        LogoSubscription partialUpdatedLogoSubscription = new LogoSubscription();
        partialUpdatedLogoSubscription.setId(logoSubscription.getId());

        partialUpdatedLogoSubscription.date(UPDATED_DATE);

        restLogoSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogoSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogoSubscription))
            )
            .andExpect(status().isOk());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LogoSubscription testLogoSubscription = logoSubscriptionList.get(logoSubscriptionList.size() - 1);
        assertThat(testLogoSubscription.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLogoSubscriptionWithPatch() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();

        // Update the logoSubscription using partial update
        LogoSubscription partialUpdatedLogoSubscription = new LogoSubscription();
        partialUpdatedLogoSubscription.setId(logoSubscription.getId());

        partialUpdatedLogoSubscription.date(UPDATED_DATE);

        restLogoSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogoSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogoSubscription))
            )
            .andExpect(status().isOk());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        LogoSubscription testLogoSubscription = logoSubscriptionList.get(logoSubscriptionList.size() - 1);
        assertThat(testLogoSubscription.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, logoSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLogoSubscription() throws Exception {
        int databaseSizeBeforeUpdate = logoSubscriptionRepository.findAll().size();
        logoSubscription.setId(count.incrementAndGet());

        // Create the LogoSubscription
        LogoSubscriptionDTO logoSubscriptionDTO = logoSubscriptionMapper.toDto(logoSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LogoSubscription in the database
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLogoSubscription() throws Exception {
        // Initialize the database
        logoSubscriptionRepository.saveAndFlush(logoSubscription);

        int databaseSizeBeforeDelete = logoSubscriptionRepository.findAll().size();

        // Delete the logoSubscription
        restLogoSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, logoSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LogoSubscription> logoSubscriptionList = logoSubscriptionRepository.findAll();
        assertThat(logoSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
