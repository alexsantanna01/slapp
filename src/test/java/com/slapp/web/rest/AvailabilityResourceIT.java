package com.slapp.web.rest;

import static com.slapp.domain.AvailabilityAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Availability;
import com.slapp.domain.Room;
import com.slapp.repository.AvailabilityRepository;
import com.slapp.service.dto.AvailabilityDTO;
import com.slapp.service.mapper.AvailabilityMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AvailabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvailabilityResourceIT {

    private static final Instant DEFAULT_START_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/availabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AvailabilityMapper availabilityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvailabilityMockMvc;

    private Availability availability;

    private Availability insertedAvailability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createEntity(EntityManager em) {
        Availability availability = new Availability()
            .startDateTime(DEFAULT_START_DATE_TIME)
            .endDateTime(DEFAULT_END_DATE_TIME)
            .available(DEFAULT_AVAILABLE)
            .reason(DEFAULT_REASON)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        availability.setRoom(room);
        return availability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createUpdatedEntity(EntityManager em) {
        Availability updatedAvailability = new Availability()
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .available(UPDATED_AVAILABLE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        updatedAvailability.setRoom(room);
        return updatedAvailability;
    }

    @BeforeEach
    void initTest() {
        availability = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAvailability != null) {
            availabilityRepository.delete(insertedAvailability);
            insertedAvailability = null;
        }
    }

    @Test
    @Transactional
    void createAvailability() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);
        var returnedAvailabilityDTO = om.readValue(
            restAvailabilityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AvailabilityDTO.class
        );

        // Validate the Availability in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAvailability = availabilityMapper.toEntity(returnedAvailabilityDTO);
        assertAvailabilityUpdatableFieldsEquals(returnedAvailability, getPersistedAvailability(returnedAvailability));

        insertedAvailability = returnedAvailability;
    }

    @Test
    @Transactional
    void createAvailabilityWithExistingId() throws Exception {
        // Create the Availability with an existing ID
        availability.setId(1L);
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        availability.setStartDateTime(null);

        // Create the Availability, which fails.
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        availability.setEndDateTime(null);

        // Create the Availability, which fails.
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        availability.setAvailable(null);

        // Create the Availability, which fails.
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvailabilities() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(DEFAULT_START_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAvailability() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        // Get the availability
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(availability.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(DEFAULT_START_DATE_TIME.toString()))
            .andExpect(jsonPath("$.endDateTime").value(DEFAULT_END_DATE_TIME.toString()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAvailability() throws Exception {
        // Get the availability
        restAvailabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvailability() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the availability
        Availability updatedAvailability = availabilityRepository.findById(availability.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAvailability are not directly saved in db
        em.detach(updatedAvailability);
        updatedAvailability
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .available(UPDATED_AVAILABLE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT);
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(updatedAvailability);

        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(availabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAvailabilityToMatchAllProperties(updatedAvailability);
    }

    @Test
    @Transactional
    void putNonExistingAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(availabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(availabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability.startDateTime(UPDATED_START_DATE_TIME);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvailabilityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAvailability, availability),
            getPersistedAvailability(availability)
        );
    }

    @Test
    @Transactional
    void fullUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .available(UPDATED_AVAILABLE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAvailabilityUpdatableFieldsEquals(partialUpdatedAvailability, getPersistedAvailability(partialUpdatedAvailability));
    }

    @Test
    @Transactional
    void patchNonExistingAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, availabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(availabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(availabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        availability.setId(longCount.incrementAndGet());

        // Create the Availability
        AvailabilityDTO availabilityDTO = availabilityMapper.toDto(availability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(availabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvailability() throws Exception {
        // Initialize the database
        insertedAvailability = availabilityRepository.saveAndFlush(availability);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the availability
        restAvailabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, availability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return availabilityRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Availability getPersistedAvailability(Availability availability) {
        return availabilityRepository.findById(availability.getId()).orElseThrow();
    }

    protected void assertPersistedAvailabilityToMatchAllProperties(Availability expectedAvailability) {
        assertAvailabilityAllPropertiesEquals(expectedAvailability, getPersistedAvailability(expectedAvailability));
    }

    protected void assertPersistedAvailabilityToMatchUpdatableProperties(Availability expectedAvailability) {
        assertAvailabilityAllUpdatablePropertiesEquals(expectedAvailability, getPersistedAvailability(expectedAvailability));
    }
}
