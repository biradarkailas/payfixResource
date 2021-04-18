package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.Duration;
import com.payfix.packages.repository.DurationRepository;
import com.payfix.packages.service.dto.DurationDTO;
import com.payfix.packages.service.mapper.DurationMapper;
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
 * Integration tests for the {@link DurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DurationResourceIT {

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final String ENTITY_API_URL = "/api/durations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DurationRepository durationRepository;

    @Autowired
    private DurationMapper durationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDurationMockMvc;

    private Duration duration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Duration createEntity(EntityManager em) {
        Duration duration = new Duration().value(DEFAULT_VALUE);
        return duration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Duration createUpdatedEntity(EntityManager em) {
        Duration duration = new Duration().value(UPDATED_VALUE);
        return duration;
    }

    @BeforeEach
    public void initTest() {
        duration = createEntity(em);
    }

    @Test
    @Transactional
    void createDuration() throws Exception {
        int databaseSizeBeforeCreate = durationRepository.findAll().size();
        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);
        restDurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationDTO)))
            .andExpect(status().isCreated());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeCreate + 1);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createDurationWithExistingId() throws Exception {
        // Create the Duration with an existing ID
        duration.setId(1L);
        DurationDTO durationDTO = durationMapper.toDto(duration);

        int databaseSizeBeforeCreate = durationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = durationRepository.findAll().size();
        // set the field null
        duration.setValue(null);

        // Create the Duration, which fails.
        DurationDTO durationDTO = durationMapper.toDto(duration);

        restDurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationDTO)))
            .andExpect(status().isBadRequest());

        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDurations() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get all the durationList
        restDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duration.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get the duration
        restDurationMockMvc
            .perform(get(ENTITY_API_URL_ID, duration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(duration.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingDuration() throws Exception {
        // Get the duration
        restDurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Update the duration
        Duration updatedDuration = durationRepository.findById(duration.getId()).get();
        // Disconnect from session so that the updates on updatedDuration are not directly saved in db
        em.detach(updatedDuration);
        updatedDuration.value(UPDATED_VALUE);
        DurationDTO durationDTO = durationMapper.toDto(updatedDuration);

        restDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, durationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, durationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(durationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDurationWithPatch() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Update the duration using partial update
        Duration partialUpdatedDuration = new Duration();
        partialUpdatedDuration.setId(duration.getId());

        restDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDuration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDuration))
            )
            .andExpect(status().isOk());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateDurationWithPatch() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Update the duration using partial update
        Duration partialUpdatedDuration = new Duration();
        partialUpdatedDuration.setId(duration.getId());

        partialUpdatedDuration.value(UPDATED_VALUE);

        restDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDuration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDuration))
            )
            .andExpect(status().isOk());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, durationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();
        duration.setId(count.incrementAndGet());

        // Create the Duration
        DurationDTO durationDTO = durationMapper.toDto(duration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDurationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(durationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        int databaseSizeBeforeDelete = durationRepository.findAll().size();

        // Delete the duration
        restDurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, duration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
