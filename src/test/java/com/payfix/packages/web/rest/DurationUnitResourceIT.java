package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.DurationUnit;
import com.payfix.packages.repository.DurationUnitRepository;
import com.payfix.packages.service.criteria.DurationUnitCriteria;
import com.payfix.packages.service.dto.DurationUnitDTO;
import com.payfix.packages.service.mapper.DurationUnitMapper;
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
 * Integration tests for the {@link DurationUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DurationUnitResourceIT {

    private static final Integer DEFAULT_UNIT_VALUE = 1;
    private static final Integer UPDATED_UNIT_VALUE = 2;
    private static final Integer SMALLER_UNIT_VALUE = 1 - 1;

    private static final String DEFAULT_ALIES = "AAAAAAAAAA";
    private static final String UPDATED_ALIES = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/duration-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DurationUnitRepository durationUnitRepository;

    @Autowired
    private DurationUnitMapper durationUnitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDurationUnitMockMvc;

    private DurationUnit durationUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DurationUnit createEntity(EntityManager em) {
        DurationUnit durationUnit = new DurationUnit().unitValue(DEFAULT_UNIT_VALUE).alies(DEFAULT_ALIES).name(DEFAULT_NAME);
        return durationUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DurationUnit createUpdatedEntity(EntityManager em) {
        DurationUnit durationUnit = new DurationUnit().unitValue(UPDATED_UNIT_VALUE).alies(UPDATED_ALIES).name(UPDATED_NAME);
        return durationUnit;
    }

    @BeforeEach
    public void initTest() {
        durationUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createDurationUnit() throws Exception {
        int databaseSizeBeforeCreate = durationUnitRepository.findAll().size();
        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);
        restDurationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeCreate + 1);
        DurationUnit testDurationUnit = durationUnitList.get(durationUnitList.size() - 1);
        assertThat(testDurationUnit.getUnitValue()).isEqualTo(DEFAULT_UNIT_VALUE);
        assertThat(testDurationUnit.getAlies()).isEqualTo(DEFAULT_ALIES);
        assertThat(testDurationUnit.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDurationUnitWithExistingId() throws Exception {
        // Create the DurationUnit with an existing ID
        durationUnit.setId(1L);
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        int databaseSizeBeforeCreate = durationUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDurationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = durationUnitRepository.findAll().size();
        // set the field null
        durationUnit.setUnitValue(null);

        // Create the DurationUnit, which fails.
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        restDurationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = durationUnitRepository.findAll().size();
        // set the field null
        durationUnit.setAlies(null);

        // Create the DurationUnit, which fails.
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        restDurationUnitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDurationUnits() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(durationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitValue").value(hasItem(DEFAULT_UNIT_VALUE)))
            .andExpect(jsonPath("$.[*].alies").value(hasItem(DEFAULT_ALIES)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDurationUnit() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get the durationUnit
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, durationUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(durationUnit.getId().intValue()))
            .andExpect(jsonPath("$.unitValue").value(DEFAULT_UNIT_VALUE))
            .andExpect(jsonPath("$.alies").value(DEFAULT_ALIES))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getDurationUnitsByIdFiltering() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        Long id = durationUnit.getId();

        defaultDurationUnitShouldBeFound("id.equals=" + id);
        defaultDurationUnitShouldNotBeFound("id.notEquals=" + id);

        defaultDurationUnitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDurationUnitShouldNotBeFound("id.greaterThan=" + id);

        defaultDurationUnitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDurationUnitShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue equals to DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.equals=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue equals to UPDATED_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.equals=" + UPDATED_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue not equals to DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.notEquals=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue not equals to UPDATED_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.notEquals=" + UPDATED_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsInShouldWork() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue in DEFAULT_UNIT_VALUE or UPDATED_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.in=" + DEFAULT_UNIT_VALUE + "," + UPDATED_UNIT_VALUE);

        // Get all the durationUnitList where unitValue equals to UPDATED_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.in=" + UPDATED_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue is not null
        defaultDurationUnitShouldBeFound("unitValue.specified=true");

        // Get all the durationUnitList where unitValue is null
        defaultDurationUnitShouldNotBeFound("unitValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue is greater than or equal to DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.greaterThanOrEqual=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue is greater than or equal to UPDATED_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.greaterThanOrEqual=" + UPDATED_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue is less than or equal to DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.lessThanOrEqual=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue is less than or equal to SMALLER_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.lessThanOrEqual=" + SMALLER_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsLessThanSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue is less than DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.lessThan=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue is less than UPDATED_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.lessThan=" + UPDATED_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByUnitValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where unitValue is greater than DEFAULT_UNIT_VALUE
        defaultDurationUnitShouldNotBeFound("unitValue.greaterThan=" + DEFAULT_UNIT_VALUE);

        // Get all the durationUnitList where unitValue is greater than SMALLER_UNIT_VALUE
        defaultDurationUnitShouldBeFound("unitValue.greaterThan=" + SMALLER_UNIT_VALUE);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesIsEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies equals to DEFAULT_ALIES
        defaultDurationUnitShouldBeFound("alies.equals=" + DEFAULT_ALIES);

        // Get all the durationUnitList where alies equals to UPDATED_ALIES
        defaultDurationUnitShouldNotBeFound("alies.equals=" + UPDATED_ALIES);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies not equals to DEFAULT_ALIES
        defaultDurationUnitShouldNotBeFound("alies.notEquals=" + DEFAULT_ALIES);

        // Get all the durationUnitList where alies not equals to UPDATED_ALIES
        defaultDurationUnitShouldBeFound("alies.notEquals=" + UPDATED_ALIES);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesIsInShouldWork() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies in DEFAULT_ALIES or UPDATED_ALIES
        defaultDurationUnitShouldBeFound("alies.in=" + DEFAULT_ALIES + "," + UPDATED_ALIES);

        // Get all the durationUnitList where alies equals to UPDATED_ALIES
        defaultDurationUnitShouldNotBeFound("alies.in=" + UPDATED_ALIES);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesIsNullOrNotNull() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies is not null
        defaultDurationUnitShouldBeFound("alies.specified=true");

        // Get all the durationUnitList where alies is null
        defaultDurationUnitShouldNotBeFound("alies.specified=false");
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesContainsSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies contains DEFAULT_ALIES
        defaultDurationUnitShouldBeFound("alies.contains=" + DEFAULT_ALIES);

        // Get all the durationUnitList where alies contains UPDATED_ALIES
        defaultDurationUnitShouldNotBeFound("alies.contains=" + UPDATED_ALIES);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByAliesNotContainsSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where alies does not contain DEFAULT_ALIES
        defaultDurationUnitShouldNotBeFound("alies.doesNotContain=" + DEFAULT_ALIES);

        // Get all the durationUnitList where alies does not contain UPDATED_ALIES
        defaultDurationUnitShouldBeFound("alies.doesNotContain=" + UPDATED_ALIES);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name equals to DEFAULT_NAME
        defaultDurationUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the durationUnitList where name equals to UPDATED_NAME
        defaultDurationUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name not equals to DEFAULT_NAME
        defaultDurationUnitShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the durationUnitList where name not equals to UPDATED_NAME
        defaultDurationUnitShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDurationUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the durationUnitList where name equals to UPDATED_NAME
        defaultDurationUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name is not null
        defaultDurationUnitShouldBeFound("name.specified=true");

        // Get all the durationUnitList where name is null
        defaultDurationUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameContainsSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name contains DEFAULT_NAME
        defaultDurationUnitShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the durationUnitList where name contains UPDATED_NAME
        defaultDurationUnitShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDurationUnitsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        // Get all the durationUnitList where name does not contain DEFAULT_NAME
        defaultDurationUnitShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the durationUnitList where name does not contain UPDATED_NAME
        defaultDurationUnitShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDurationUnitShouldBeFound(String filter) throws Exception {
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(durationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitValue").value(hasItem(DEFAULT_UNIT_VALUE)))
            .andExpect(jsonPath("$.[*].alies").value(hasItem(DEFAULT_ALIES)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDurationUnitShouldNotBeFound(String filter) throws Exception {
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDurationUnitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDurationUnit() throws Exception {
        // Get the durationUnit
        restDurationUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDurationUnit() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();

        // Update the durationUnit
        DurationUnit updatedDurationUnit = durationUnitRepository.findById(durationUnit.getId()).get();
        // Disconnect from session so that the updates on updatedDurationUnit are not directly saved in db
        em.detach(updatedDurationUnit);
        updatedDurationUnit.unitValue(UPDATED_UNIT_VALUE).alies(UPDATED_ALIES).name(UPDATED_NAME);
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(updatedDurationUnit);

        restDurationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, durationUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
        DurationUnit testDurationUnit = durationUnitList.get(durationUnitList.size() - 1);
        assertThat(testDurationUnit.getUnitValue()).isEqualTo(UPDATED_UNIT_VALUE);
        assertThat(testDurationUnit.getAlies()).isEqualTo(UPDATED_ALIES);
        assertThat(testDurationUnit.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, durationUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDurationUnitWithPatch() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();

        // Update the durationUnit using partial update
        DurationUnit partialUpdatedDurationUnit = new DurationUnit();
        partialUpdatedDurationUnit.setId(durationUnit.getId());

        partialUpdatedDurationUnit.unitValue(UPDATED_UNIT_VALUE).alies(UPDATED_ALIES).name(UPDATED_NAME);

        restDurationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDurationUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDurationUnit))
            )
            .andExpect(status().isOk());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
        DurationUnit testDurationUnit = durationUnitList.get(durationUnitList.size() - 1);
        assertThat(testDurationUnit.getUnitValue()).isEqualTo(UPDATED_UNIT_VALUE);
        assertThat(testDurationUnit.getAlies()).isEqualTo(UPDATED_ALIES);
        assertThat(testDurationUnit.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDurationUnitWithPatch() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();

        // Update the durationUnit using partial update
        DurationUnit partialUpdatedDurationUnit = new DurationUnit();
        partialUpdatedDurationUnit.setId(durationUnit.getId());

        partialUpdatedDurationUnit.unitValue(UPDATED_UNIT_VALUE).alies(UPDATED_ALIES).name(UPDATED_NAME);

        restDurationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDurationUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDurationUnit))
            )
            .andExpect(status().isOk());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
        DurationUnit testDurationUnit = durationUnitList.get(durationUnitList.size() - 1);
        assertThat(testDurationUnit.getUnitValue()).isEqualTo(UPDATED_UNIT_VALUE);
        assertThat(testDurationUnit.getAlies()).isEqualTo(UPDATED_ALIES);
        assertThat(testDurationUnit.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, durationUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDurationUnit() throws Exception {
        int databaseSizeBeforeUpdate = durationUnitRepository.findAll().size();
        durationUnit.setId(count.incrementAndGet());

        // Create the DurationUnit
        DurationUnitDTO durationUnitDTO = durationUnitMapper.toDto(durationUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationUnitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(durationUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DurationUnit in the database
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDurationUnit() throws Exception {
        // Initialize the database
        durationUnitRepository.saveAndFlush(durationUnit);

        int databaseSizeBeforeDelete = durationUnitRepository.findAll().size();

        // Delete the durationUnit
        restDurationUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, durationUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DurationUnit> durationUnitList = durationUnitRepository.findAll();
        assertThat(durationUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
