package com.slapp.web.rest;

import static com.slapp.domain.ReservationAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.slapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Reservation;
import com.slapp.domain.Room;
import com.slapp.domain.UserProfile;
import com.slapp.domain.enumeration.ReservationStatus;
import com.slapp.repository.ReservationRepository;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.mapper.ReservationMapper;
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
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservationResourceIT {

    private static final Instant DEFAULT_START_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(0 - 1);

    private static final ReservationStatus DEFAULT_STATUS = ReservationStatus.PENDING;
    private static final ReservationStatus UPDATED_STATUS = ReservationStatus.CONFIRMED;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CANCELLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCELLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CANCEL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    private Reservation insertedReservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .startDateTime(DEFAULT_START_DATE_TIME)
            .endDateTime(DEFAULT_END_DATE_TIME)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .cancelledAt(DEFAULT_CANCELLED_AT)
            .cancelReason(DEFAULT_CANCEL_REASON);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        reservation.setCustomer(userProfile);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        reservation.setRoom(room);
        return reservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation updatedReservation = new Reservation()
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .cancelReason(UPDATED_CANCEL_REASON);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        updatedReservation.setCustomer(userProfile);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        updatedReservation.setRoom(room);
        return updatedReservation;
    }

    @BeforeEach
    void initTest() {
        reservation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReservation != null) {
            reservationRepository.delete(insertedReservation);
            insertedReservation = null;
        }
    }

    @Test
    @Transactional
    void createReservation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        var returnedReservationDTO = om.readValue(
            restReservationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReservationDTO.class
        );

        // Validate the Reservation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReservation = reservationMapper.toEntity(returnedReservationDTO);
        assertReservationUpdatableFieldsEquals(returnedReservation, getPersistedReservation(returnedReservation));

        insertedReservation = returnedReservation;
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservation.setId(1L);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservation.setStartDateTime(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservation.setEndDateTime(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservation.setTotalPrice(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservation.setStatus(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        restReservationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(DEFAULT_START_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelledAt").value(hasItem(DEFAULT_CANCELLED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)));
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(DEFAULT_START_DATE_TIME.toString()))
            .andExpect(jsonPath("$.endDateTime").value(DEFAULT_END_DATE_TIME.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.cancelledAt").value(DEFAULT_CANCELLED_AT.toString()))
            .andExpect(jsonPath("$.cancelReason").value(DEFAULT_CANCEL_REASON));
    }

    @Test
    @Transactional
    void getReservationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        Long id = reservation.getId();

        defaultReservationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReservationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReservationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDateTime equals to
        defaultReservationFiltering("startDateTime.equals=" + DEFAULT_START_DATE_TIME, "startDateTime.equals=" + UPDATED_START_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByStartDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDateTime in
        defaultReservationFiltering(
            "startDateTime.in=" + DEFAULT_START_DATE_TIME + "," + UPDATED_START_DATE_TIME,
            "startDateTime.in=" + UPDATED_START_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllReservationsByStartDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where startDateTime is not null
        defaultReservationFiltering("startDateTime.specified=true", "startDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateTime equals to
        defaultReservationFiltering("endDateTime.equals=" + DEFAULT_END_DATE_TIME, "endDateTime.equals=" + UPDATED_END_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateTime in
        defaultReservationFiltering(
            "endDateTime.in=" + DEFAULT_END_DATE_TIME + "," + UPDATED_END_DATE_TIME,
            "endDateTime.in=" + UPDATED_END_DATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllReservationsByEndDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where endDateTime is not null
        defaultReservationFiltering("endDateTime.specified=true", "endDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice equals to
        defaultReservationFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice in
        defaultReservationFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice is not null
        defaultReservationFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice is greater than or equal to
        defaultReservationFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice is less than or equal to
        defaultReservationFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice is less than
        defaultReservationFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllReservationsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where totalPrice is greater than
        defaultReservationFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllReservationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where status equals to
        defaultReservationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReservationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where status in
        defaultReservationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReservationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where status is not null
        defaultReservationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdAt equals to
        defaultReservationFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdAt in
        defaultReservationFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReservationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where createdAt is not null
        defaultReservationFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedAt equals to
        defaultReservationFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedAt in
        defaultReservationFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllReservationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where updatedAt is not null
        defaultReservationFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByCancelledAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelledAt equals to
        defaultReservationFiltering("cancelledAt.equals=" + DEFAULT_CANCELLED_AT, "cancelledAt.equals=" + UPDATED_CANCELLED_AT);
    }

    @Test
    @Transactional
    void getAllReservationsByCancelledAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelledAt in
        defaultReservationFiltering(
            "cancelledAt.in=" + DEFAULT_CANCELLED_AT + "," + UPDATED_CANCELLED_AT,
            "cancelledAt.in=" + UPDATED_CANCELLED_AT
        );
    }

    @Test
    @Transactional
    void getAllReservationsByCancelledAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelledAt is not null
        defaultReservationFiltering("cancelledAt.specified=true", "cancelledAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByCancelReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelReason equals to
        defaultReservationFiltering("cancelReason.equals=" + DEFAULT_CANCEL_REASON, "cancelReason.equals=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllReservationsByCancelReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelReason in
        defaultReservationFiltering(
            "cancelReason.in=" + DEFAULT_CANCEL_REASON + "," + UPDATED_CANCEL_REASON,
            "cancelReason.in=" + UPDATED_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllReservationsByCancelReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelReason is not null
        defaultReservationFiltering("cancelReason.specified=true", "cancelReason.specified=false");
    }

    @Test
    @Transactional
    void getAllReservationsByCancelReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelReason contains
        defaultReservationFiltering("cancelReason.contains=" + DEFAULT_CANCEL_REASON, "cancelReason.contains=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllReservationsByCancelReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where cancelReason does not contain
        defaultReservationFiltering(
            "cancelReason.doesNotContain=" + UPDATED_CANCEL_REASON,
            "cancelReason.doesNotContain=" + DEFAULT_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllReservationsByCustomerIsEqualToSomething() throws Exception {
        UserProfile customer;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            reservationRepository.saveAndFlush(reservation);
            customer = UserProfileResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(customer);
        em.flush();
        reservation.setCustomer(customer);
        reservationRepository.saveAndFlush(reservation);
        Long customerId = customer.getId();
        // Get all the reservationList where customer equals to customerId
        defaultReservationShouldBeFound("customerId.equals=" + customerId);

        // Get all the reservationList where customer equals to (customerId + 1)
        defaultReservationShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllReservationsByRoomIsEqualToSomething() throws Exception {
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            reservationRepository.saveAndFlush(reservation);
            room = RoomResourceIT.createEntity(em);
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        em.persist(room);
        em.flush();
        reservation.setRoom(room);
        reservationRepository.saveAndFlush(reservation);
        Long roomId = room.getId();
        // Get all the reservationList where room equals to roomId
        defaultReservationShouldBeFound("roomId.equals=" + roomId);

        // Get all the reservationList where room equals to (roomId + 1)
        defaultReservationShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }

    private void defaultReservationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReservationShouldBeFound(shouldBeFound);
        defaultReservationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(DEFAULT_START_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelledAt").value(hasItem(DEFAULT_CANCELLED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)));

        // Check, that the count call also returns 1
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservation() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .cancelReason(UPDATED_CANCEL_REASON);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReservationToMatchAllProperties(updatedReservation);
    }

    @Test
    @Transactional
    void putNonExistingReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation.startDateTime(UPDATED_START_DATE_TIME);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReservation, reservation),
            getPersistedReservation(reservation)
        );
    }

    @Test
    @Transactional
    void fullUpdateReservationWithPatch() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservation using partial update
        Reservation partialUpdatedReservation = new Reservation();
        partialUpdatedReservation.setId(reservation.getId());

        partialUpdatedReservation
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .cancelReason(UPDATED_CANCEL_REASON);

        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservation))
            )
            .andExpect(status().isOk());

        // Validate the Reservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservationUpdatableFieldsEquals(partialUpdatedReservation, getPersistedReservation(partialUpdatedReservation));
    }

    @Test
    @Transactional
    void patchNonExistingReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservation.setId(longCount.incrementAndGet());

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        insertedReservation = reservationRepository.saveAndFlush(reservation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reservationRepository.count();
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

    protected Reservation getPersistedReservation(Reservation reservation) {
        return reservationRepository.findById(reservation.getId()).orElseThrow();
    }

    protected void assertPersistedReservationToMatchAllProperties(Reservation expectedReservation) {
        assertReservationAllPropertiesEquals(expectedReservation, getPersistedReservation(expectedReservation));
    }

    protected void assertPersistedReservationToMatchUpdatableProperties(Reservation expectedReservation) {
        assertReservationAllUpdatablePropertiesEquals(expectedReservation, getPersistedReservation(expectedReservation));
    }
}
