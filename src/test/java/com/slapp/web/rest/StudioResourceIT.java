package com.slapp.web.rest;

import static com.slapp.domain.StudioAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.CancellationPolicy;
import com.slapp.domain.Studio;
import com.slapp.domain.UserProfile;
import com.slapp.repository.StudioRepository;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.mapper.StudioMapper;
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
 * Integration tests for the {@link StudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudioResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/studios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private StudioMapper studioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudioMockMvc;

    private Studio studio;

    private Studio insertedStudio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Studio createEntity(EntityManager em) {
        Studio studio = new Studio()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE)
            .image(DEFAULT_IMAGE)
            .active(DEFAULT_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        studio.setOwner(userProfile);
        return studio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Studio createUpdatedEntity(EntityManager em) {
        Studio updatedStudio = new Studio()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .image(UPDATED_IMAGE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        updatedStudio.setOwner(userProfile);
        return updatedStudio;
    }

    @BeforeEach
    void initTest() {
        studio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedStudio != null) {
            studioRepository.delete(insertedStudio);
            insertedStudio = null;
        }
    }

    @Test
    @Transactional
    void createStudio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);
        var returnedStudioDTO = om.readValue(
            restStudioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudioDTO.class
        );

        // Validate the Studio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudio = studioMapper.toEntity(returnedStudioDTO);
        assertStudioUpdatableFieldsEquals(returnedStudio, getPersistedStudio(returnedStudio));

        insertedStudio = returnedStudio;
    }

    @Test
    @Transactional
    void createStudioWithExistingId() throws Exception {
        // Create the Studio with an existing ID
        studio.setId(1L);
        StudioDTO studioDTO = studioMapper.toDto(studio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studio.setName(null);

        // Create the Studio, which fails.
        StudioDTO studioDTO = studioMapper.toDto(studio);

        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studio.setAddress(null);

        // Create the Studio, which fails.
        StudioDTO studioDTO = studioMapper.toDto(studio);

        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studio.setCity(null);

        // Create the Studio, which fails.
        StudioDTO studioDTO = studioMapper.toDto(studio);

        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studio.setState(null);

        // Create the Studio, which fails.
        StudioDTO studioDTO = studioMapper.toDto(studio);

        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studio.setActive(null);

        // Create the Studio, which fails.
        StudioDTO studioDTO = studioMapper.toDto(studio);

        restStudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudios() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList
        restStudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studio.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getStudio() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get the studio
        restStudioMockMvc
            .perform(get(ENTITY_API_URL_ID, studio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studio.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getStudiosByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        Long id = studio.getId();

        defaultStudioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudiosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where name equals to
        defaultStudioFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudiosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where name in
        defaultStudioFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudiosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where name is not null
        defaultStudioFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where name contains
        defaultStudioFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStudiosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where name does not contain
        defaultStudioFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllStudiosByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where address equals to
        defaultStudioFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudiosByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where address in
        defaultStudioFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudiosByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where address is not null
        defaultStudioFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where address contains
        defaultStudioFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudiosByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where address does not contain
        defaultStudioFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStudiosByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where city equals to
        defaultStudioFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudiosByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where city in
        defaultStudioFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudiosByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where city is not null
        defaultStudioFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where city contains
        defaultStudioFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllStudiosByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where city does not contain
        defaultStudioFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllStudiosByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where state equals to
        defaultStudioFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllStudiosByStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where state in
        defaultStudioFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllStudiosByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where state is not null
        defaultStudioFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByStateContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where state contains
        defaultStudioFiltering("state.contains=" + DEFAULT_STATE, "state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllStudiosByStateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where state does not contain
        defaultStudioFiltering("state.doesNotContain=" + UPDATED_STATE, "state.doesNotContain=" + DEFAULT_STATE);
    }

    @Test
    @Transactional
    void getAllStudiosByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where zipCode equals to
        defaultStudioFiltering("zipCode.equals=" + DEFAULT_ZIP_CODE, "zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllStudiosByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where zipCode in
        defaultStudioFiltering("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE, "zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllStudiosByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where zipCode is not null
        defaultStudioFiltering("zipCode.specified=true", "zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where zipCode contains
        defaultStudioFiltering("zipCode.contains=" + DEFAULT_ZIP_CODE, "zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllStudiosByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where zipCode does not contain
        defaultStudioFiltering("zipCode.doesNotContain=" + UPDATED_ZIP_CODE, "zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude equals to
        defaultStudioFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude in
        defaultStudioFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude is not null
        defaultStudioFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude is greater than or equal to
        defaultStudioFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude is less than or equal to
        defaultStudioFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude is less than
        defaultStudioFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where latitude is greater than
        defaultStudioFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude equals to
        defaultStudioFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude in
        defaultStudioFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude is not null
        defaultStudioFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude is greater than or equal to
        defaultStudioFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude is less than or equal to
        defaultStudioFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude is less than
        defaultStudioFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where longitude is greater than
        defaultStudioFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllStudiosByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where phone equals to
        defaultStudioFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllStudiosByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where phone in
        defaultStudioFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllStudiosByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where phone is not null
        defaultStudioFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where phone contains
        defaultStudioFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllStudiosByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where phone does not contain
        defaultStudioFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllStudiosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where email equals to
        defaultStudioFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudiosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where email in
        defaultStudioFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudiosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where email is not null
        defaultStudioFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where email contains
        defaultStudioFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudiosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where email does not contain
        defaultStudioFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudiosByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where website equals to
        defaultStudioFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStudiosByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where website in
        defaultStudioFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStudiosByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where website is not null
        defaultStudioFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where website contains
        defaultStudioFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStudiosByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where website does not contain
        defaultStudioFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStudiosByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where image equals to
        defaultStudioFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllStudiosByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where image in
        defaultStudioFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllStudiosByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where image is not null
        defaultStudioFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where image contains
        defaultStudioFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllStudiosByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where image does not contain
        defaultStudioFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllStudiosByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where active equals to
        defaultStudioFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStudiosByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where active in
        defaultStudioFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStudiosByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where active is not null
        defaultStudioFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where createdAt equals to
        defaultStudioFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllStudiosByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where createdAt in
        defaultStudioFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllStudiosByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where createdAt is not null
        defaultStudioFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where updatedAt equals to
        defaultStudioFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllStudiosByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where updatedAt in
        defaultStudioFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllStudiosByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        // Get all the studioList where updatedAt is not null
        defaultStudioFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiosByOwnerIsEqualToSomething() throws Exception {
        UserProfile owner;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            studioRepository.saveAndFlush(studio);
            owner = UserProfileResourceIT.createEntity(em);
        } else {
            owner = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(owner);
        em.flush();
        studio.setOwner(owner);
        studioRepository.saveAndFlush(studio);
        Long ownerId = owner.getId();
        // Get all the studioList where owner equals to ownerId
        defaultStudioShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the studioList where owner equals to (ownerId + 1)
        defaultStudioShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllStudiosByCancellationPolicyIsEqualToSomething() throws Exception {
        CancellationPolicy cancellationPolicy;
        if (TestUtil.findAll(em, CancellationPolicy.class).isEmpty()) {
            studioRepository.saveAndFlush(studio);
            cancellationPolicy = CancellationPolicyResourceIT.createEntity();
        } else {
            cancellationPolicy = TestUtil.findAll(em, CancellationPolicy.class).get(0);
        }
        em.persist(cancellationPolicy);
        em.flush();
        studio.setCancellationPolicy(cancellationPolicy);
        studioRepository.saveAndFlush(studio);
        Long cancellationPolicyId = cancellationPolicy.getId();
        // Get all the studioList where cancellationPolicy equals to cancellationPolicyId
        defaultStudioShouldBeFound("cancellationPolicyId.equals=" + cancellationPolicyId);

        // Get all the studioList where cancellationPolicy equals to (cancellationPolicyId + 1)
        defaultStudioShouldNotBeFound("cancellationPolicyId.equals=" + (cancellationPolicyId + 1));
    }

    private void defaultStudioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudioShouldBeFound(shouldBeFound);
        defaultStudioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudioShouldBeFound(String filter) throws Exception {
        restStudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studio.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restStudioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudioShouldNotBeFound(String filter) throws Exception {
        restStudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudio() throws Exception {
        // Get the studio
        restStudioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudio() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studio
        Studio updatedStudio = studioRepository.findById(studio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudio are not directly saved in db
        em.detach(updatedStudio);
        updatedStudio
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .image(UPDATED_IMAGE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        StudioDTO studioDTO = studioMapper.toDto(updatedStudio);

        restStudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudioToMatchAllProperties(updatedStudio);
    }

    @Test
    @Transactional
    void putNonExistingStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudioWithPatch() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studio using partial update
        Studio partialUpdatedStudio = new Studio();
        partialUpdatedStudio.setId(studio.getId());

        partialUpdatedStudio.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).address(UPDATED_ADDRESS).image(UPDATED_IMAGE);

        restStudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudio))
            )
            .andExpect(status().isOk());

        // Validate the Studio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStudio, studio), getPersistedStudio(studio));
    }

    @Test
    @Transactional
    void fullUpdateStudioWithPatch() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studio using partial update
        Studio partialUpdatedStudio = new Studio();
        partialUpdatedStudio.setId(studio.getId());

        partialUpdatedStudio
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .image(UPDATED_IMAGE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restStudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudio))
            )
            .andExpect(status().isOk());

        // Validate the Studio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudioUpdatableFieldsEquals(partialUpdatedStudio, getPersistedStudio(partialUpdatedStudio));
    }

    @Test
    @Transactional
    void patchNonExistingStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studio.setId(longCount.incrementAndGet());

        // Create the Studio
        StudioDTO studioDTO = studioMapper.toDto(studio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Studio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudio() throws Exception {
        // Initialize the database
        insertedStudio = studioRepository.saveAndFlush(studio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studio
        restStudioMockMvc
            .perform(delete(ENTITY_API_URL_ID, studio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studioRepository.count();
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

    protected Studio getPersistedStudio(Studio studio) {
        return studioRepository.findById(studio.getId()).orElseThrow();
    }

    protected void assertPersistedStudioToMatchAllProperties(Studio expectedStudio) {
        assertStudioAllPropertiesEquals(expectedStudio, getPersistedStudio(expectedStudio));
    }

    protected void assertPersistedStudioToMatchUpdatableProperties(Studio expectedStudio) {
        assertStudioAllUpdatablePropertiesEquals(expectedStudio, getPersistedStudio(expectedStudio));
    }
}
