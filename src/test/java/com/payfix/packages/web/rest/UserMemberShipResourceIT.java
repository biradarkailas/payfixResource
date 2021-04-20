package com.payfix.packages.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payfix.packages.IntegrationTest;
import com.payfix.packages.domain.UserMemberShip;
import com.payfix.packages.repository.UserMemberShipRepository;
import com.payfix.packages.service.dto.UserMemberShipDTO;
import com.payfix.packages.service.mapper.UserMemberShipMapper;
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
 * Integration tests for the {@link UserMemberShipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserMemberShipResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/user-member-ships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserMemberShipRepository userMemberShipRepository;

    @Autowired
    private UserMemberShipMapper userMemberShipMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMemberShipMockMvc;

    private UserMemberShip userMemberShip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMemberShip createEntity(EntityManager em) {
        UserMemberShip userMemberShip = new UserMemberShip().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE);
        return userMemberShip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMemberShip createUpdatedEntity(EntityManager em) {
        UserMemberShip userMemberShip = new UserMemberShip().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        return userMemberShip;
    }

    @BeforeEach
    public void initTest() {
        userMemberShip = createEntity(em);
    }

    @Test
    @Transactional
    void createUserMemberShip() throws Exception {
        int databaseSizeBeforeCreate = userMemberShipRepository.findAll().size();
        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);
        restUserMemberShipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeCreate + 1);
        UserMemberShip testUserMemberShip = userMemberShipList.get(userMemberShipList.size() - 1);
        assertThat(testUserMemberShip.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testUserMemberShip.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createUserMemberShipWithExistingId() throws Exception {
        // Create the UserMemberShip with an existing ID
        userMemberShip.setId(1L);
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        int databaseSizeBeforeCreate = userMemberShipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMemberShipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserMemberShips() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        // Get all the userMemberShipList
        restUserMemberShipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMemberShip.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserMemberShip() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        // Get the userMemberShip
        restUserMemberShipMockMvc
            .perform(get(ENTITY_API_URL_ID, userMemberShip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMemberShip.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserMemberShip() throws Exception {
        // Get the userMemberShip
        restUserMemberShipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserMemberShip() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();

        // Update the userMemberShip
        UserMemberShip updatedUserMemberShip = userMemberShipRepository.findById(userMemberShip.getId()).get();
        // Disconnect from session so that the updates on updatedUserMemberShip are not directly saved in db
        em.detach(updatedUserMemberShip);
        updatedUserMemberShip.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(updatedUserMemberShip);

        restUserMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMemberShipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
        UserMemberShip testUserMemberShip = userMemberShipList.get(userMemberShipList.size() - 1);
        assertThat(testUserMemberShip.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserMemberShip.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMemberShipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserMemberShipWithPatch() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();

        // Update the userMemberShip using partial update
        UserMemberShip partialUpdatedUserMemberShip = new UserMemberShip();
        partialUpdatedUserMemberShip.setId(userMemberShip.getId());

        partialUpdatedUserMemberShip.startDate(UPDATED_START_DATE);

        restUserMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMemberShip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMemberShip))
            )
            .andExpect(status().isOk());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
        UserMemberShip testUserMemberShip = userMemberShipList.get(userMemberShipList.size() - 1);
        assertThat(testUserMemberShip.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserMemberShip.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUserMemberShipWithPatch() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();

        // Update the userMemberShip using partial update
        UserMemberShip partialUpdatedUserMemberShip = new UserMemberShip();
        partialUpdatedUserMemberShip.setId(userMemberShip.getId());

        partialUpdatedUserMemberShip.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restUserMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMemberShip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMemberShip))
            )
            .andExpect(status().isOk());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
        UserMemberShip testUserMemberShip = userMemberShipList.get(userMemberShipList.size() - 1);
        assertThat(testUserMemberShip.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserMemberShip.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMemberShipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMemberShip() throws Exception {
        int databaseSizeBeforeUpdate = userMemberShipRepository.findAll().size();
        userMemberShip.setId(count.incrementAndGet());

        // Create the UserMemberShip
        UserMemberShipDTO userMemberShipDTO = userMemberShipMapper.toDto(userMemberShip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMemberShipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMemberShipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMemberShip in the database
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserMemberShip() throws Exception {
        // Initialize the database
        userMemberShipRepository.saveAndFlush(userMemberShip);

        int databaseSizeBeforeDelete = userMemberShipRepository.findAll().size();

        // Delete the userMemberShip
        restUserMemberShipMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMemberShip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserMemberShip> userMemberShipList = userMemberShipRepository.findAll();
        assertThat(userMemberShipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
