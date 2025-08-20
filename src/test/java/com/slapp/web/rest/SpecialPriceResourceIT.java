package com.slapp.web.rest;

import static com.slapp.domain.SpecialPriceAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.slapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Room;
import com.slapp.domain.SpecialPrice;
import com.slapp.domain.enumeration.DayOfWeek;
import com.slapp.repository.SpecialPriceRepository;
import com.slapp.service.dto.SpecialPriceDTO;
import com.slapp.service.mapper.SpecialPriceMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalTime;
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
 * Integration tests for the {@link SpecialPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialPriceResourceIT {

    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    private static final LocalTime DEFAULT_START_TIME = LocalTime.NOON;
    private static final LocalTime UPDATED_START_TIME = LocalTime.MAX.withNano(0);

    private static final LocalTime DEFAULT_END_TIME = LocalTime.NOON;
    private static final LocalTime UPDATED_END_TIME = LocalTime.MAX.withNano(0);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/special-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialPriceRepository specialPriceRepository;

    @Autowired
    private SpecialPriceMapper specialPriceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialPriceMockMvc;

    private SpecialPrice specialPrice;

    private SpecialPrice insertedSpecialPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialPrice createEntity(EntityManager em) {
        SpecialPrice specialPrice = new SpecialPrice()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .price(DEFAULT_PRICE)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        specialPrice.setRoom(room);
        return specialPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialPrice createUpdatedEntity(EntityManager em) {
        SpecialPrice updatedSpecialPrice = new SpecialPrice()
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        updatedSpecialPrice.setRoom(room);
        return updatedSpecialPrice;
    }

    @BeforeEach
    void initTest() {
        specialPrice = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSpecialPrice != null) {
            specialPriceRepository.delete(insertedSpecialPrice);
            insertedSpecialPrice = null;
        }
    }

    @Test
    @Transactional
    void createSpecialPrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);
        var returnedSpecialPriceDTO = om.readValue(
            restSpecialPriceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialPriceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialPriceDTO.class
        );

        // Validate the SpecialPrice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpecialPrice = specialPriceMapper.toEntity(returnedSpecialPriceDTO);
        assertSpecialPriceUpdatableFieldsEquals(returnedSpecialPrice, getPersistedSpecialPrice(returnedSpecialPrice));

        insertedSpecialPrice = returnedSpecialPrice;
    }

    @Test
    @Transactional
    void createSpecialPriceWithExistingId() throws Exception {
        // Create the SpecialPrice with an existing ID
        specialPrice.setId(1L);
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialPriceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialPrice.setPrice(null);

        // Create the SpecialPrice, which fails.
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        restSpecialPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialPriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialPrice.setActive(null);

        // Create the SpecialPrice, which fails.
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        restSpecialPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialPriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialPrices() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        // Get all the specialPriceList
        restSpecialPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getSpecialPrice() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        // Get the specialPrice
        restSpecialPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, specialPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialPrice.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingSpecialPrice() throws Exception {
        // Get the specialPrice
        restSpecialPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialPrice() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialPrice
        SpecialPrice updatedSpecialPrice = specialPriceRepository.findById(specialPrice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialPrice are not directly saved in db
        em.detach(updatedSpecialPrice);
        updatedSpecialPrice
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(updatedSpecialPrice);

        restSpecialPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialPriceDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialPriceToMatchAllProperties(updatedSpecialPrice);
    }

    @Test
    @Transactional
    void putNonExistingSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialPriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialPriceWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialPrice using partial update
        SpecialPrice partialUpdatedSpecialPrice = new SpecialPrice();
        partialUpdatedSpecialPrice.setId(specialPrice.getId());

        partialUpdatedSpecialPrice.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME).price(UPDATED_PRICE).active(UPDATED_ACTIVE);

        restSpecialPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialPrice))
            )
            .andExpect(status().isOk());

        // Validate the SpecialPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialPriceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialPrice, specialPrice),
            getPersistedSpecialPrice(specialPrice)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialPriceWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialPrice using partial update
        SpecialPrice partialUpdatedSpecialPrice = new SpecialPrice();
        partialUpdatedSpecialPrice.setId(specialPrice.getId());

        partialUpdatedSpecialPrice
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .price(UPDATED_PRICE)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);

        restSpecialPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialPrice))
            )
            .andExpect(status().isOk());

        // Validate the SpecialPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialPriceUpdatableFieldsEquals(partialUpdatedSpecialPrice, getPersistedSpecialPrice(partialUpdatedSpecialPrice));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialPriceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialPrice.setId(longCount.incrementAndGet());

        // Create the SpecialPrice
        SpecialPriceDTO specialPriceDTO = specialPriceMapper.toDto(specialPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialPriceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialPriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialPrice() throws Exception {
        // Initialize the database
        insertedSpecialPrice = specialPriceRepository.saveAndFlush(specialPrice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialPrice
        restSpecialPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialPriceRepository.count();
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

    protected SpecialPrice getPersistedSpecialPrice(SpecialPrice specialPrice) {
        return specialPriceRepository.findById(specialPrice.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialPriceToMatchAllProperties(SpecialPrice expectedSpecialPrice) {
        assertSpecialPriceAllPropertiesEquals(expectedSpecialPrice, getPersistedSpecialPrice(expectedSpecialPrice));
    }

    protected void assertPersistedSpecialPriceToMatchUpdatableProperties(SpecialPrice expectedSpecialPrice) {
        assertSpecialPriceAllUpdatablePropertiesEquals(expectedSpecialPrice, getPersistedSpecialPrice(expectedSpecialPrice));
    }
}
