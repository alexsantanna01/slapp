package com.slapp.web.rest;

import static com.slapp.domain.EquipmentAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Equipment;
import com.slapp.domain.Room;
import com.slapp.domain.enumeration.EquipmentType;
import com.slapp.repository.EquipmentRepository;
import com.slapp.service.dto.EquipmentDTO;
import com.slapp.service.mapper.EquipmentMapper;
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
 * Integration tests for the {@link EquipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final EquipmentType DEFAULT_EQUIPMENT_TYPE = EquipmentType.MICROPHONE;
    private static final EquipmentType UPDATED_EQUIPMENT_TYPE = EquipmentType.AMPLIFIER;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/equipment";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    private Equipment insertedEquipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .name(DEFAULT_NAME)
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .description(DEFAULT_DESCRIPTION)
            .available(DEFAULT_AVAILABLE)
            .equipmentType(DEFAULT_EQUIPMENT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        equipment.setRoom(room);
        return equipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createUpdatedEntity(EntityManager em) {
        Equipment updatedEquipment = new Equipment()
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .equipmentType(UPDATED_EQUIPMENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        updatedEquipment.setRoom(room);
        return updatedEquipment;
    }

    @BeforeEach
    void initTest() {
        equipment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEquipment != null) {
            equipmentRepository.delete(insertedEquipment);
            insertedEquipment = null;
        }
    }

    @Test
    @Transactional
    void createEquipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        var returnedEquipmentDTO = om.readValue(
            restEquipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EquipmentDTO.class
        );

        // Validate the Equipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEquipment = equipmentMapper.toEntity(returnedEquipmentDTO);
        assertEquipmentUpdatableFieldsEquals(returnedEquipment, getPersistedEquipment(returnedEquipment));

        insertedEquipment = returnedEquipment;
    }

    @Test
    @Transactional
    void createEquipmentWithExistingId() throws Exception {
        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equipment.setName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equipment.setAvailable(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEquipmentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equipment.setEquipmentType(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipment() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getEquipment() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE))
            .andExpect(jsonPath("$.equipmentType").value(DEFAULT_EQUIPMENT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getEquipmentByIdFiltering() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        Long id = equipment.getId();

        defaultEquipmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEquipmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEquipmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEquipmentByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name equals to
        defaultEquipmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEquipmentByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name in
        defaultEquipmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEquipmentByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name is not null
        defaultEquipmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name contains
        defaultEquipmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEquipmentByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name does not contain
        defaultEquipmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand equals to
        defaultEquipmentFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand in
        defaultEquipmentFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand is not null
        defaultEquipmentFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand contains
        defaultEquipmentFiltering("brand.contains=" + DEFAULT_BRAND, "brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where brand does not contain
        defaultEquipmentFiltering("brand.doesNotContain=" + UPDATED_BRAND, "brand.doesNotContain=" + DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model equals to
        defaultEquipmentFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model in
        defaultEquipmentFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model is not null
        defaultEquipmentFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByModelContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model contains
        defaultEquipmentFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where model does not contain
        defaultEquipmentFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllEquipmentByAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where available equals to
        defaultEquipmentFiltering("available.equals=" + DEFAULT_AVAILABLE, "available.equals=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllEquipmentByAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where available in
        defaultEquipmentFiltering("available.in=" + DEFAULT_AVAILABLE + "," + UPDATED_AVAILABLE, "available.in=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllEquipmentByAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where available is not null
        defaultEquipmentFiltering("available.specified=true", "available.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByEquipmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType equals to
        defaultEquipmentFiltering("equipmentType.equals=" + DEFAULT_EQUIPMENT_TYPE, "equipmentType.equals=" + UPDATED_EQUIPMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEquipmentByEquipmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType in
        defaultEquipmentFiltering(
            "equipmentType.in=" + DEFAULT_EQUIPMENT_TYPE + "," + UPDATED_EQUIPMENT_TYPE,
            "equipmentType.in=" + UPDATED_EQUIPMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEquipmentByEquipmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType is not null
        defaultEquipmentFiltering("equipmentType.specified=true", "equipmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt equals to
        defaultEquipmentFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEquipmentByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt in
        defaultEquipmentFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEquipmentByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt is not null
        defaultEquipmentFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt equals to
        defaultEquipmentFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEquipmentByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt in
        defaultEquipmentFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEquipmentByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt is not null
        defaultEquipmentFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipmentByRoomIsEqualToSomething() throws Exception {
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            equipmentRepository.saveAndFlush(equipment);
            room = RoomResourceIT.createEntity(em);
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        em.persist(room);
        em.flush();
        equipment.setRoom(room);
        equipmentRepository.saveAndFlush(equipment);
        Long roomId = room.getId();
        // Get all the equipmentList where room equals to roomId
        defaultEquipmentShouldBeFound("roomId.equals=" + roomId);

        // Get all the equipmentList where room equals to (roomId + 1)
        defaultEquipmentShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }

    private void defaultEquipmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEquipmentShouldBeFound(shouldBeFound);
        defaultEquipmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentShouldBeFound(String filter) throws Exception {
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentShouldNotBeFound(String filter) throws Exception {
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquipment() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .equipmentType(UPDATED_EQUIPMENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEquipmentToMatchAllProperties(updatedEquipment);
    }

    @Test
    @Transactional
    void putNonExistingEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .available(UPDATED_AVAILABLE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEquipment, equipment),
            getPersistedEquipment(equipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .description(UPDATED_DESCRIPTION)
            .available(UPDATED_AVAILABLE)
            .equipmentType(UPDATED_EQUIPMENT_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquipmentUpdatableFieldsEquals(partialUpdatedEquipment, getPersistedEquipment(partialUpdatedEquipment));
    }

    @Test
    @Transactional
    void patchNonExistingEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipment.setId(longCount.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(equipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipment() throws Exception {
        // Initialize the database
        insertedEquipment = equipmentRepository.saveAndFlush(equipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the equipment
        restEquipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return equipmentRepository.count();
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

    protected Equipment getPersistedEquipment(Equipment equipment) {
        return equipmentRepository.findById(equipment.getId()).orElseThrow();
    }

    protected void assertPersistedEquipmentToMatchAllProperties(Equipment expectedEquipment) {
        assertEquipmentAllPropertiesEquals(expectedEquipment, getPersistedEquipment(expectedEquipment));
    }

    protected void assertPersistedEquipmentToMatchUpdatableProperties(Equipment expectedEquipment) {
        assertEquipmentAllUpdatablePropertiesEquals(expectedEquipment, getPersistedEquipment(expectedEquipment));
    }
}
