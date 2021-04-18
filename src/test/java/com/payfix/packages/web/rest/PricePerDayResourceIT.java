package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.PricePerDay;
import com.payfix.packages.repository.PricePerDayRepository;
import com.payfix.packages.service.dto.PricePerDayDTO;
import com.payfix.packages.service.mapper.PricePerDayMapper;
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
 * Integration tests for the {@link PricePerDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricePerDayResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/price-per-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PricePerDayRepository pricePerDayRepository;

    @Autowired
    private PricePerDayMapper pricePerDayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPricePerDayMockMvc;

    private PricePerDay pricePerDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PricePerDay createEntity(EntityManager em) {
        PricePerDay pricePerDay = new PricePerDay().price(DEFAULT_PRICE);
        return pricePerDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PricePerDay createUpdatedEntity(EntityManager em) {
        PricePerDay pricePerDay = new PricePerDay().price(UPDATED_PRICE);
        return pricePerDay;
    }

    @BeforeEach
    public void initTest() {
        pricePerDay = createEntity(em);
    }

    @Test
    @Transactional
    void createPricePerDay() throws Exception {
        int databaseSizeBeforeCreate = pricePerDayRepository.findAll().size();
        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);
        restPricePerDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeCreate + 1);
        PricePerDay testPricePerDay = pricePerDayList.get(pricePerDayList.size() - 1);
        assertThat(testPricePerDay.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createPricePerDayWithExistingId() throws Exception {
        // Create the PricePerDay with an existing ID
        pricePerDay.setId(1L);
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        int databaseSizeBeforeCreate = pricePerDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricePerDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricePerDayRepository.findAll().size();
        // set the field null
        pricePerDay.setPrice(null);

        // Create the PricePerDay, which fails.
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        restPricePerDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPricePerDays() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        // Get all the pricePerDayList
        restPricePerDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricePerDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getPricePerDay() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        // Get the pricePerDay
        restPricePerDayMockMvc
            .perform(get(ENTITY_API_URL_ID, pricePerDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pricePerDay.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPricePerDay() throws Exception {
        // Get the pricePerDay
        restPricePerDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPricePerDay() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();

        // Update the pricePerDay
        PricePerDay updatedPricePerDay = pricePerDayRepository.findById(pricePerDay.getId()).get();
        // Disconnect from session so that the updates on updatedPricePerDay are not directly saved in db
        em.detach(updatedPricePerDay);
        updatedPricePerDay.price(UPDATED_PRICE);
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(updatedPricePerDay);

        restPricePerDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricePerDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isOk());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
        PricePerDay testPricePerDay = pricePerDayList.get(pricePerDayList.size() - 1);
        assertThat(testPricePerDay.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricePerDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePricePerDayWithPatch() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();

        // Update the pricePerDay using partial update
        PricePerDay partialUpdatedPricePerDay = new PricePerDay();
        partialUpdatedPricePerDay.setId(pricePerDay.getId());

        partialUpdatedPricePerDay.price(UPDATED_PRICE);

        restPricePerDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricePerDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricePerDay))
            )
            .andExpect(status().isOk());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
        PricePerDay testPricePerDay = pricePerDayList.get(pricePerDayList.size() - 1);
        assertThat(testPricePerDay.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdatePricePerDayWithPatch() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();

        // Update the pricePerDay using partial update
        PricePerDay partialUpdatedPricePerDay = new PricePerDay();
        partialUpdatedPricePerDay.setId(pricePerDay.getId());

        partialUpdatedPricePerDay.price(UPDATED_PRICE);

        restPricePerDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricePerDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPricePerDay))
            )
            .andExpect(status().isOk());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
        PricePerDay testPricePerDay = pricePerDayList.get(pricePerDayList.size() - 1);
        assertThat(testPricePerDay.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pricePerDayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPricePerDay() throws Exception {
        int databaseSizeBeforeUpdate = pricePerDayRepository.findAll().size();
        pricePerDay.setId(count.incrementAndGet());

        // Create the PricePerDay
        PricePerDayDTO pricePerDayDTO = pricePerDayMapper.toDto(pricePerDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricePerDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pricePerDayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PricePerDay in the database
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePricePerDay() throws Exception {
        // Initialize the database
        pricePerDayRepository.saveAndFlush(pricePerDay);

        int databaseSizeBeforeDelete = pricePerDayRepository.findAll().size();

        // Delete the pricePerDay
        restPricePerDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, pricePerDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PricePerDay> pricePerDayList = pricePerDayRepository.findAll();
        assertThat(pricePerDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
