package com.slapp.web.rest;

import static com.slapp.domain.RoomAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.slapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Room;
import com.slapp.domain.Studio;
import com.slapp.domain.enumeration.RoomType;
import com.slapp.repository.RoomRepository;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.mapper.RoomMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_HOURLY_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_HOURLY_RATE = new BigDecimal(1);
    private static final BigDecimal SMALLER_HOURLY_RATE = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;
    private static final Integer SMALLER_CAPACITY = 1 - 1;

    private static final Boolean DEFAULT_SOUNDPROOFED = false;
    private static final Boolean UPDATED_SOUNDPROOFED = true;

    private static final Boolean DEFAULT_AIR_CONDITIONING = false;
    private static final Boolean UPDATED_AIR_CONDITIONING = true;

    private static final RoomType DEFAULT_ROOM_TYPE = RoomType.RECORDING;
    private static final RoomType UPDATED_ROOM_TYPE = RoomType.REHEARSAL;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    private Room insertedRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .capacity(DEFAULT_CAPACITY)
            .soundproofed(DEFAULT_SOUNDPROOFED)
            .airConditioning(DEFAULT_AIR_CONDITIONING)
            .roomType(DEFAULT_ROOM_TYPE)
            .active(DEFAULT_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Studio studio;
        if (TestUtil.findAll(em, Studio.class).isEmpty()) {
            studio = StudioResourceIT.createEntity(em);
            em.persist(studio);
            em.flush();
        } else {
            studio = TestUtil.findAll(em, Studio.class).get(0);
        }
        room.setStudio(studio);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room updatedRoom = new Room()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .capacity(UPDATED_CAPACITY)
            .soundproofed(UPDATED_SOUNDPROOFED)
            .airConditioning(UPDATED_AIR_CONDITIONING)
            .roomType(UPDATED_ROOM_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Studio studio;
        if (TestUtil.findAll(em, Studio.class).isEmpty()) {
            studio = StudioResourceIT.createUpdatedEntity(em);
            em.persist(studio);
            em.flush();
        } else {
            studio = TestUtil.findAll(em, Studio.class).get(0);
        }
        updatedRoom.setStudio(studio);
        return updatedRoom;
    }

    @BeforeEach
    void initTest() {
        room = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRoom != null) {
            roomRepository.delete(insertedRoom);
            insertedRoom = null;
        }
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        var returnedRoomDTO = om.readValue(
            restRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomDTO.class
        );

        // Validate the Room in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoom = roomMapper.toEntity(returnedRoomDTO);
        assertRoomUpdatableFieldsEquals(returnedRoom, getPersistedRoom(returnedRoom));

        insertedRoom = returnedRoom;
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setName(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHourlyRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setHourlyRate(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoomTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setRoomType(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        room.setActive(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].soundproofed").value(hasItem(DEFAULT_SOUNDPROOFED)))
            .andExpect(jsonPath("$.[*].airConditioning").value(hasItem(DEFAULT_AIR_CONDITIONING)))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.hourlyRate").value(sameNumber(DEFAULT_HOURLY_RATE)))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.soundproofed").value(DEFAULT_SOUNDPROOFED))
            .andExpect(jsonPath("$.airConditioning").value(DEFAULT_AIR_CONDITIONING))
            .andExpect(jsonPath("$.roomType").value(DEFAULT_ROOM_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getRoomsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        Long id = room.getId();

        defaultRoomFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRoomFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRoomFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where name equals to
        defaultRoomFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where name in
        defaultRoomFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where name is not null
        defaultRoomFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where name contains
        defaultRoomFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where name does not contain
        defaultRoomFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate equals to
        defaultRoomFiltering("hourlyRate.equals=" + DEFAULT_HOURLY_RATE, "hourlyRate.equals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate in
        defaultRoomFiltering("hourlyRate.in=" + DEFAULT_HOURLY_RATE + "," + UPDATED_HOURLY_RATE, "hourlyRate.in=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate is not null
        defaultRoomFiltering("hourlyRate.specified=true", "hourlyRate.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate is greater than or equal to
        defaultRoomFiltering(
            "hourlyRate.greaterThanOrEqual=" + DEFAULT_HOURLY_RATE,
            "hourlyRate.greaterThanOrEqual=" + UPDATED_HOURLY_RATE
        );
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate is less than or equal to
        defaultRoomFiltering("hourlyRate.lessThanOrEqual=" + DEFAULT_HOURLY_RATE, "hourlyRate.lessThanOrEqual=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate is less than
        defaultRoomFiltering("hourlyRate.lessThan=" + UPDATED_HOURLY_RATE, "hourlyRate.lessThan=" + DEFAULT_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllRoomsByHourlyRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where hourlyRate is greater than
        defaultRoomFiltering("hourlyRate.greaterThan=" + SMALLER_HOURLY_RATE, "hourlyRate.greaterThan=" + DEFAULT_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity equals to
        defaultRoomFiltering("capacity.equals=" + DEFAULT_CAPACITY, "capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity in
        defaultRoomFiltering("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY, "capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity is not null
        defaultRoomFiltering("capacity.specified=true", "capacity.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity is greater than or equal to
        defaultRoomFiltering("capacity.greaterThanOrEqual=" + DEFAULT_CAPACITY, "capacity.greaterThanOrEqual=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity is less than or equal to
        defaultRoomFiltering("capacity.lessThanOrEqual=" + DEFAULT_CAPACITY, "capacity.lessThanOrEqual=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity is less than
        defaultRoomFiltering("capacity.lessThan=" + UPDATED_CAPACITY, "capacity.lessThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsByCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where capacity is greater than
        defaultRoomFiltering("capacity.greaterThan=" + SMALLER_CAPACITY, "capacity.greaterThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllRoomsBySoundproofedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where soundproofed equals to
        defaultRoomFiltering("soundproofed.equals=" + DEFAULT_SOUNDPROOFED, "soundproofed.equals=" + UPDATED_SOUNDPROOFED);
    }

    @Test
    @Transactional
    void getAllRoomsBySoundproofedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where soundproofed in
        defaultRoomFiltering(
            "soundproofed.in=" + DEFAULT_SOUNDPROOFED + "," + UPDATED_SOUNDPROOFED,
            "soundproofed.in=" + UPDATED_SOUNDPROOFED
        );
    }

    @Test
    @Transactional
    void getAllRoomsBySoundproofedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where soundproofed is not null
        defaultRoomFiltering("soundproofed.specified=true", "soundproofed.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByAirConditioningIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where airConditioning equals to
        defaultRoomFiltering("airConditioning.equals=" + DEFAULT_AIR_CONDITIONING, "airConditioning.equals=" + UPDATED_AIR_CONDITIONING);
    }

    @Test
    @Transactional
    void getAllRoomsByAirConditioningIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where airConditioning in
        defaultRoomFiltering(
            "airConditioning.in=" + DEFAULT_AIR_CONDITIONING + "," + UPDATED_AIR_CONDITIONING,
            "airConditioning.in=" + UPDATED_AIR_CONDITIONING
        );
    }

    @Test
    @Transactional
    void getAllRoomsByAirConditioningIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where airConditioning is not null
        defaultRoomFiltering("airConditioning.specified=true", "airConditioning.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByRoomTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomType equals to
        defaultRoomFiltering("roomType.equals=" + DEFAULT_ROOM_TYPE, "roomType.equals=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomType in
        defaultRoomFiltering("roomType.in=" + DEFAULT_ROOM_TYPE + "," + UPDATED_ROOM_TYPE, "roomType.in=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomsByRoomTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where roomType is not null
        defaultRoomFiltering("roomType.specified=true", "roomType.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where active equals to
        defaultRoomFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRoomsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where active in
        defaultRoomFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRoomsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where active is not null
        defaultRoomFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where createdAt equals to
        defaultRoomFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllRoomsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where createdAt in
        defaultRoomFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllRoomsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where createdAt is not null
        defaultRoomFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where updatedAt equals to
        defaultRoomFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllRoomsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where updatedAt in
        defaultRoomFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllRoomsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        // Get all the roomList where updatedAt is not null
        defaultRoomFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByStudioIsEqualToSomething() throws Exception {
        Studio studio;
        if (TestUtil.findAll(em, Studio.class).isEmpty()) {
            roomRepository.saveAndFlush(room);
            studio = StudioResourceIT.createEntity(em);
        } else {
            studio = TestUtil.findAll(em, Studio.class).get(0);
        }
        em.persist(studio);
        em.flush();
        room.setStudio(studio);
        roomRepository.saveAndFlush(room);
        Long studioId = studio.getId();
        // Get all the roomList where studio equals to studioId
        defaultRoomShouldBeFound("studioId.equals=" + studioId);

        // Get all the roomList where studio equals to (studioId + 1)
        defaultRoomShouldNotBeFound("studioId.equals=" + (studioId + 1));
    }

    private void defaultRoomFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRoomShouldBeFound(shouldBeFound);
        defaultRoomShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomShouldBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].soundproofed").value(hasItem(DEFAULT_SOUNDPROOFED)))
            .andExpect(jsonPath("$.[*].airConditioning").value(hasItem(DEFAULT_AIR_CONDITIONING)))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomShouldNotBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .capacity(UPDATED_CAPACITY)
            .soundproofed(UPDATED_SOUNDPROOFED)
            .airConditioning(UPDATED_AIR_CONDITIONING)
            .roomType(UPDATED_ROOM_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isOk());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomToMatchAllProperties(updatedRoom);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL_ID, roomDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom.description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoom, room), getPersistedRoom(room));
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .capacity(UPDATED_CAPACITY)
            .soundproofed(UPDATED_SOUNDPROOFED)
            .airConditioning(UPDATED_AIR_CONDITIONING)
            .roomType(UPDATED_ROOM_TYPE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomUpdatableFieldsEquals(partialUpdatedRoom, getPersistedRoom(partialUpdatedRoom));
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        room.setId(longCount.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        insertedRoom = roomRepository.saveAndFlush(room);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomRepository.count();
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

    protected Room getPersistedRoom(Room room) {
        return roomRepository.findById(room.getId()).orElseThrow();
    }

    protected void assertPersistedRoomToMatchAllProperties(Room expectedRoom) {
        assertRoomAllPropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }

    protected void assertPersistedRoomToMatchUpdatableProperties(Room expectedRoom) {
        assertRoomAllUpdatablePropertiesEquals(expectedRoom, getPersistedRoom(expectedRoom));
    }
}
