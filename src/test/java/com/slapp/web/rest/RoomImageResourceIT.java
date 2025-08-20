package com.slapp.web.rest;

import static com.slapp.domain.RoomImageAsserts.*;
import static com.slapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slapp.IntegrationTest;
import com.slapp.domain.Room;
import com.slapp.domain.RoomImage;
import com.slapp.repository.RoomImageRepository;
import com.slapp.service.dto.RoomImageDTO;
import com.slapp.service.mapper.RoomImageMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link RoomImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ALT_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/room-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Autowired
    private RoomImageMapper roomImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomImageMockMvc;

    private RoomImage roomImage;

    private RoomImage insertedRoomImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomImage createEntity(EntityManager em) {
        RoomImage roomImage = new RoomImage()
            .url(DEFAULT_URL)
            .altText(DEFAULT_ALT_TEXT)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
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
        roomImage.setRoom(room);
        return roomImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomImage createUpdatedEntity(EntityManager em) {
        RoomImage updatedRoomImage = new RoomImage()
            .url(UPDATED_URL)
            .altText(UPDATED_ALT_TEXT)
            .displayOrder(UPDATED_DISPLAY_ORDER)
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
        updatedRoomImage.setRoom(room);
        return updatedRoomImage;
    }

    @BeforeEach
    void initTest() {
        roomImage = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRoomImage != null) {
            roomImageRepository.delete(insertedRoomImage);
            insertedRoomImage = null;
        }
    }

    @Test
    @Transactional
    void createRoomImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);
        var returnedRoomImageDTO = om.readValue(
            restRoomImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomImageDTO.class
        );

        // Validate the RoomImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoomImage = roomImageMapper.toEntity(returnedRoomImageDTO);
        assertRoomImageUpdatableFieldsEquals(returnedRoomImage, getPersistedRoomImage(returnedRoomImage));

        insertedRoomImage = returnedRoomImage;
    }

    @Test
    @Transactional
    void createRoomImageWithExistingId() throws Exception {
        // Create the RoomImage with an existing ID
        roomImage.setId(1L);
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomImage.setUrl(null);

        // Create the RoomImage, which fails.
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        restRoomImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomImage.setActive(null);

        // Create the RoomImage, which fails.
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        restRoomImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomImages() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        // Get all the roomImageList
        restRoomImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].altText").value(hasItem(DEFAULT_ALT_TEXT)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getRoomImage() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        // Get the roomImage
        restRoomImageMockMvc
            .perform(get(ENTITY_API_URL_ID, roomImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.altText").value(DEFAULT_ALT_TEXT))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingRoomImage() throws Exception {
        // Get the roomImage
        restRoomImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomImage() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomImage
        RoomImage updatedRoomImage = roomImageRepository.findById(roomImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoomImage are not directly saved in db
        em.detach(updatedRoomImage);
        updatedRoomImage.url(UPDATED_URL).altText(UPDATED_ALT_TEXT).displayOrder(UPDATED_DISPLAY_ORDER).active(UPDATED_ACTIVE);
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(updatedRoomImage);

        restRoomImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomImageToMatchAllProperties(updatedRoomImage);
    }

    @Test
    @Transactional
    void putNonExistingRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomImageWithPatch() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomImage using partial update
        RoomImage partialUpdatedRoomImage = new RoomImage();
        partialUpdatedRoomImage.setId(roomImage.getId());

        partialUpdatedRoomImage.url(UPDATED_URL);

        restRoomImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomImage))
            )
            .andExpect(status().isOk());

        // Validate the RoomImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRoomImage, roomImage),
            getPersistedRoomImage(roomImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateRoomImageWithPatch() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomImage using partial update
        RoomImage partialUpdatedRoomImage = new RoomImage();
        partialUpdatedRoomImage.setId(roomImage.getId());

        partialUpdatedRoomImage.url(UPDATED_URL).altText(UPDATED_ALT_TEXT).displayOrder(UPDATED_DISPLAY_ORDER).active(UPDATED_ACTIVE);

        restRoomImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomImage))
            )
            .andExpect(status().isOk());

        // Validate the RoomImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomImageUpdatableFieldsEquals(partialUpdatedRoomImage, getPersistedRoomImage(partialUpdatedRoomImage));
    }

    @Test
    @Transactional
    void patchNonExistingRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomImage.setId(longCount.incrementAndGet());

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomImage() throws Exception {
        // Initialize the database
        insertedRoomImage = roomImageRepository.saveAndFlush(roomImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roomImage
        restRoomImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomImageRepository.count();
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

    protected RoomImage getPersistedRoomImage(RoomImage roomImage) {
        return roomImageRepository.findById(roomImage.getId()).orElseThrow();
    }

    protected void assertPersistedRoomImageToMatchAllProperties(RoomImage expectedRoomImage) {
        assertRoomImageAllPropertiesEquals(expectedRoomImage, getPersistedRoomImage(expectedRoomImage));
    }

    protected void assertPersistedRoomImageToMatchUpdatableProperties(RoomImage expectedRoomImage) {
        assertRoomImageAllUpdatablePropertiesEquals(expectedRoomImage, getPersistedRoomImage(expectedRoomImage));
    }
}
