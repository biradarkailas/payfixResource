package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.Category;
import com.payfix.packages.domain.Duration;
import com.payfix.packages.domain.DurationUnit;
import com.payfix.packages.domain.Logo;
import com.payfix.packages.domain.PricePerDay;
import com.payfix.packages.domain.User;
import com.payfix.packages.repository.LogoRepository;
import com.payfix.packages.service.criteria.LogoCriteria;
import com.payfix.packages.service.dto.LogoDTO;
import com.payfix.packages.service.mapper.LogoMapper;
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
 * Integration tests for the {@link LogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LogoResourceIT {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TERM_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_TERM_CONDITIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/logos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LogoRepository logoRepository;

    @Autowired
    private LogoMapper logoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLogoMockMvc;

    private Logo logo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Logo createEntity(EntityManager em) {
        Logo logo = new Logo().imageUrl(DEFAULT_IMAGE_URL).termConditions(DEFAULT_TERM_CONDITIONS).about(DEFAULT_ABOUT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logo.setUser(user);
        return logo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Logo createUpdatedEntity(EntityManager em) {
        Logo logo = new Logo().imageUrl(UPDATED_IMAGE_URL).termConditions(UPDATED_TERM_CONDITIONS).about(UPDATED_ABOUT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logo.setUser(user);
        return logo;
    }

    @BeforeEach
    public void initTest() {
        logo = createEntity(em);
    }

    @Test
    @Transactional
    void createLogo() throws Exception {
        int databaseSizeBeforeCreate = logoRepository.findAll().size();
        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);
        restLogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoDTO)))
            .andExpect(status().isCreated());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeCreate + 1);
        Logo testLogo = logoList.get(logoList.size() - 1);
        assertThat(testLogo.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testLogo.getTermConditions()).isEqualTo(DEFAULT_TERM_CONDITIONS);
        assertThat(testLogo.getAbout()).isEqualTo(DEFAULT_ABOUT);
    }

    @Test
    @Transactional
    void createLogoWithExistingId() throws Exception {
        // Create the Logo with an existing ID
        logo.setId(1L);
        LogoDTO logoDTO = logoMapper.toDto(logo);

        int databaseSizeBeforeCreate = logoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImageUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = logoRepository.findAll().size();
        // set the field null
        logo.setImageUrl(null);

        // Create the Logo, which fails.
        LogoDTO logoDTO = logoMapper.toDto(logo);

        restLogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoDTO)))
            .andExpect(status().isBadRequest());

        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLogos() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList
        restLogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logo.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].termConditions").value(hasItem(DEFAULT_TERM_CONDITIONS)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT)));
    }

    @Test
    @Transactional
    void getLogo() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get the logo
        restLogoMockMvc
            .perform(get(ENTITY_API_URL_ID, logo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(logo.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.termConditions").value(DEFAULT_TERM_CONDITIONS))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT));
    }

    @Test
    @Transactional
    void getLogosByIdFiltering() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        Long id = logo.getId();

        defaultLogoShouldBeFound("id.equals=" + id);
        defaultLogoShouldNotBeFound("id.notEquals=" + id);

        defaultLogoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLogoShouldNotBeFound("id.greaterThan=" + id);

        defaultLogoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLogoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultLogoShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the logoList where imageUrl equals to UPDATED_IMAGE_URL
        defaultLogoShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultLogoShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the logoList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultLogoShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultLogoShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the logoList where imageUrl equals to UPDATED_IMAGE_URL
        defaultLogoShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl is not null
        defaultLogoShouldBeFound("imageUrl.specified=true");

        // Get all the logoList where imageUrl is null
        defaultLogoShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl contains DEFAULT_IMAGE_URL
        defaultLogoShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the logoList where imageUrl contains UPDATED_IMAGE_URL
        defaultLogoShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllLogosByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultLogoShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the logoList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultLogoShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions equals to DEFAULT_TERM_CONDITIONS
        defaultLogoShouldBeFound("termConditions.equals=" + DEFAULT_TERM_CONDITIONS);

        // Get all the logoList where termConditions equals to UPDATED_TERM_CONDITIONS
        defaultLogoShouldNotBeFound("termConditions.equals=" + UPDATED_TERM_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions not equals to DEFAULT_TERM_CONDITIONS
        defaultLogoShouldNotBeFound("termConditions.notEquals=" + DEFAULT_TERM_CONDITIONS);

        // Get all the logoList where termConditions not equals to UPDATED_TERM_CONDITIONS
        defaultLogoShouldBeFound("termConditions.notEquals=" + UPDATED_TERM_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsIsInShouldWork() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions in DEFAULT_TERM_CONDITIONS or UPDATED_TERM_CONDITIONS
        defaultLogoShouldBeFound("termConditions.in=" + DEFAULT_TERM_CONDITIONS + "," + UPDATED_TERM_CONDITIONS);

        // Get all the logoList where termConditions equals to UPDATED_TERM_CONDITIONS
        defaultLogoShouldNotBeFound("termConditions.in=" + UPDATED_TERM_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions is not null
        defaultLogoShouldBeFound("termConditions.specified=true");

        // Get all the logoList where termConditions is null
        defaultLogoShouldNotBeFound("termConditions.specified=false");
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions contains DEFAULT_TERM_CONDITIONS
        defaultLogoShouldBeFound("termConditions.contains=" + DEFAULT_TERM_CONDITIONS);

        // Get all the logoList where termConditions contains UPDATED_TERM_CONDITIONS
        defaultLogoShouldNotBeFound("termConditions.contains=" + UPDATED_TERM_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllLogosByTermConditionsNotContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where termConditions does not contain DEFAULT_TERM_CONDITIONS
        defaultLogoShouldNotBeFound("termConditions.doesNotContain=" + DEFAULT_TERM_CONDITIONS);

        // Get all the logoList where termConditions does not contain UPDATED_TERM_CONDITIONS
        defaultLogoShouldBeFound("termConditions.doesNotContain=" + UPDATED_TERM_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllLogosByAboutIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about equals to DEFAULT_ABOUT
        defaultLogoShouldBeFound("about.equals=" + DEFAULT_ABOUT);

        // Get all the logoList where about equals to UPDATED_ABOUT
        defaultLogoShouldNotBeFound("about.equals=" + UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void getAllLogosByAboutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about not equals to DEFAULT_ABOUT
        defaultLogoShouldNotBeFound("about.notEquals=" + DEFAULT_ABOUT);

        // Get all the logoList where about not equals to UPDATED_ABOUT
        defaultLogoShouldBeFound("about.notEquals=" + UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void getAllLogosByAboutIsInShouldWork() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about in DEFAULT_ABOUT or UPDATED_ABOUT
        defaultLogoShouldBeFound("about.in=" + DEFAULT_ABOUT + "," + UPDATED_ABOUT);

        // Get all the logoList where about equals to UPDATED_ABOUT
        defaultLogoShouldNotBeFound("about.in=" + UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void getAllLogosByAboutIsNullOrNotNull() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about is not null
        defaultLogoShouldBeFound("about.specified=true");

        // Get all the logoList where about is null
        defaultLogoShouldNotBeFound("about.specified=false");
    }

    @Test
    @Transactional
    void getAllLogosByAboutContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about contains DEFAULT_ABOUT
        defaultLogoShouldBeFound("about.contains=" + DEFAULT_ABOUT);

        // Get all the logoList where about contains UPDATED_ABOUT
        defaultLogoShouldNotBeFound("about.contains=" + UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void getAllLogosByAboutNotContainsSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        // Get all the logoList where about does not contain DEFAULT_ABOUT
        defaultLogoShouldNotBeFound("about.doesNotContain=" + DEFAULT_ABOUT);

        // Get all the logoList where about does not contain UPDATED_ABOUT
        defaultLogoShouldBeFound("about.doesNotContain=" + UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void getAllLogosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        logo.setUser(user);
        logoRepository.saveAndFlush(logo);
        Long userId = user.getId();

        // Get all the logoList where user equals to userId
        defaultLogoShouldBeFound("userId.equals=" + userId);

        // Get all the logoList where user equals to (userId + 1)
        defaultLogoShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllLogosByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        logo.setCategory(category);
        logoRepository.saveAndFlush(logo);
        Long categoryId = category.getId();

        // Get all the logoList where category equals to categoryId
        defaultLogoShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the logoList where category equals to (categoryId + 1)
        defaultLogoShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllLogosByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);
        Duration duration = DurationResourceIT.createEntity(em);
        em.persist(duration);
        em.flush();
        logo.setDuration(duration);
        logoRepository.saveAndFlush(logo);
        Long durationId = duration.getId();

        // Get all the logoList where duration equals to durationId
        defaultLogoShouldBeFound("durationId.equals=" + durationId);

        // Get all the logoList where duration equals to (durationId + 1)
        defaultLogoShouldNotBeFound("durationId.equals=" + (durationId + 1));
    }

    @Test
    @Transactional
    void getAllLogosByDurationUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);
        DurationUnit durationUnit = DurationUnitResourceIT.createEntity(em);
        em.persist(durationUnit);
        em.flush();
        logo.setDurationUnit(durationUnit);
        logoRepository.saveAndFlush(logo);
        Long durationUnitId = durationUnit.getId();

        // Get all the logoList where durationUnit equals to durationUnitId
        defaultLogoShouldBeFound("durationUnitId.equals=" + durationUnitId);

        // Get all the logoList where durationUnit equals to (durationUnitId + 1)
        defaultLogoShouldNotBeFound("durationUnitId.equals=" + (durationUnitId + 1));
    }

    @Test
    @Transactional
    void getAllLogosByPricePerDayIsEqualToSomething() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);
        PricePerDay pricePerDay = PricePerDayResourceIT.createEntity(em);
        em.persist(pricePerDay);
        em.flush();
        logo.setPricePerDay(pricePerDay);
        logoRepository.saveAndFlush(logo);
        Long pricePerDayId = pricePerDay.getId();

        // Get all the logoList where pricePerDay equals to pricePerDayId
        defaultLogoShouldBeFound("pricePerDayId.equals=" + pricePerDayId);

        // Get all the logoList where pricePerDay equals to (pricePerDayId + 1)
        defaultLogoShouldNotBeFound("pricePerDayId.equals=" + (pricePerDayId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLogoShouldBeFound(String filter) throws Exception {
        restLogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logo.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].termConditions").value(hasItem(DEFAULT_TERM_CONDITIONS)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT)));

        // Check, that the count call also returns 1
        restLogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLogoShouldNotBeFound(String filter) throws Exception {
        restLogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLogoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLogo() throws Exception {
        // Get the logo
        restLogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLogo() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        int databaseSizeBeforeUpdate = logoRepository.findAll().size();

        // Update the logo
        Logo updatedLogo = logoRepository.findById(logo.getId()).get();
        // Disconnect from session so that the updates on updatedLogo are not directly saved in db
        em.detach(updatedLogo);
        updatedLogo.imageUrl(UPDATED_IMAGE_URL).termConditions(UPDATED_TERM_CONDITIONS).about(UPDATED_ABOUT);
        LogoDTO logoDTO = logoMapper.toDto(updatedLogo);

        restLogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
        Logo testLogo = logoList.get(logoList.size() - 1);
        assertThat(testLogo.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testLogo.getTermConditions()).isEqualTo(UPDATED_TERM_CONDITIONS);
        assertThat(testLogo.getAbout()).isEqualTo(UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void putNonExistingLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLogoWithPatch() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        int databaseSizeBeforeUpdate = logoRepository.findAll().size();

        // Update the logo using partial update
        Logo partialUpdatedLogo = new Logo();
        partialUpdatedLogo.setId(logo.getId());

        partialUpdatedLogo.termConditions(UPDATED_TERM_CONDITIONS).about(UPDATED_ABOUT);

        restLogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogo))
            )
            .andExpect(status().isOk());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
        Logo testLogo = logoList.get(logoList.size() - 1);
        assertThat(testLogo.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testLogo.getTermConditions()).isEqualTo(UPDATED_TERM_CONDITIONS);
        assertThat(testLogo.getAbout()).isEqualTo(UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void fullUpdateLogoWithPatch() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        int databaseSizeBeforeUpdate = logoRepository.findAll().size();

        // Update the logo using partial update
        Logo partialUpdatedLogo = new Logo();
        partialUpdatedLogo.setId(logo.getId());

        partialUpdatedLogo.imageUrl(UPDATED_IMAGE_URL).termConditions(UPDATED_TERM_CONDITIONS).about(UPDATED_ABOUT);

        restLogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogo))
            )
            .andExpect(status().isOk());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
        Logo testLogo = logoList.get(logoList.size() - 1);
        assertThat(testLogo.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testLogo.getTermConditions()).isEqualTo(UPDATED_TERM_CONDITIONS);
        assertThat(testLogo.getAbout()).isEqualTo(UPDATED_ABOUT);
    }

    @Test
    @Transactional
    void patchNonExistingLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, logoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLogo() throws Exception {
        int databaseSizeBeforeUpdate = logoRepository.findAll().size();
        logo.setId(count.incrementAndGet());

        // Create the Logo
        LogoDTO logoDTO = logoMapper.toDto(logo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(logoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Logo in the database
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLogo() throws Exception {
        // Initialize the database
        logoRepository.saveAndFlush(logo);

        int databaseSizeBeforeDelete = logoRepository.findAll().size();

        // Delete the logo
        restLogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, logo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Logo> logoList = logoRepository.findAll();
        assertThat(logoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
