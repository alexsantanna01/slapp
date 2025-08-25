package com.slapp.web.rest;

import com.slapp.repository.FavoriteRepository;
import com.slapp.security.SecurityUtils;
import com.slapp.service.FavoriteService;
import com.slapp.service.dto.FavoriteDTO;
import com.slapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.slapp.domain.Favorite}.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteResource {

    private static final Logger LOG = LoggerFactory.getLogger(FavoriteResource.class);

    private static final String ENTITY_NAME = "favorite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteService favoriteService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteResource(FavoriteService favoriteService, FavoriteRepository favoriteRepository) {
        this.favoriteService = favoriteService;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * {@code POST  /favorites} : Create a new favorite.
     *
     * @param favoriteDTO the favoriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoriteDTO, or with status {@code 400 (Bad Request)} if the favorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FavoriteDTO> createFavorite(@Valid @RequestBody FavoriteDTO favoriteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Favorite : {}", favoriteDTO);
        if (favoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new favorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        favoriteDTO = favoriteService.save(favoriteDTO);
        return ResponseEntity.created(new URI("/api/favorites/" + favoriteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, favoriteDTO.getId().toString()))
            .body(favoriteDTO);
    }

    /**
     * {@code PUT  /favorites/:id} : Updates an existing favorite.
     *
     * @param id the id of the favoriteDTO to save.
     * @param favoriteDTO the favoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FavoriteDTO> updateFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FavoriteDTO favoriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Favorite : {}, {}", id, favoriteDTO);
        if (favoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        favoriteDTO = favoriteService.update(favoriteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteDTO.getId().toString()))
            .body(favoriteDTO);
    }

    /**
     * {@code PATCH  /favorites/:id} : Partial updates given fields of an existing favorite, field will ignore if it is null
     *
     * @param id the id of the favoriteDTO to save.
     * @param favoriteDTO the favoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the favoriteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the favoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FavoriteDTO> partialUpdateFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FavoriteDTO favoriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Favorite partially : {}, {}", id, favoriteDTO);
        if (favoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FavoriteDTO> result = favoriteService.partialUpdate(favoriteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /favorites} : get all the favorites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favorites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Favorites");
        Page<FavoriteDTO> page = favoriteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /favorites/:id} : get the "id" favorite.
     *
     * @param id the id of the favoriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDTO> getFavorite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Favorite : {}", id);
        Optional<FavoriteDTO> favoriteDTO = favoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoriteDTO);
    }

    /**
     * {@code DELETE  /favorites/:id} : delete the "id" favorite.
     *
     * @param id the id of the favoriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Favorite : {}", id);
        favoriteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST /favorites/studio/:studioId} : Adiciona um studio aos favoritos
     */
    @PostMapping("/studio/{studioId}")
    public ResponseEntity<FavoriteDTO> addToFavorites(@PathVariable("studioId") Long studioId) {
        LOG.debug("REST request to add studio {} to favorites", studioId);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Usuário não autenticado"));

        // Em uma implementação real, você precisaria buscar o userId pelo login
        // Por simplicidade, assumimos que temos uma forma de obter o userId
        Long userId = getCurrentUserId(); // Implementar este método

        FavoriteDTO favorite = favoriteService.addToFavorites(userId, studioId);
        return ResponseEntity.ok(favorite);
    }

    /**
     * {@code DELETE /favorites/studio/:studioId} : Remove um studio dos favoritos
     */
    @DeleteMapping("/studio/{studioId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable("studioId") Long studioId) {
        LOG.debug("REST request to remove studio {} from favorites", studioId);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Usuário não autenticado"));

        Long userId = getCurrentUserId(); // Implementar este método

        favoriteService.removeFromFavorites(userId, studioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET /favorites/studio/:studioId/is-favorite} : Verifica se um studio é favorito
     */
    @GetMapping("/studio/{studioId}/is-favorite")
    public ResponseEntity<Boolean> isFavorite(@PathVariable("studioId") Long studioId) {
        LOG.debug("REST request to check if studio {} is favorite", studioId);

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isEmpty()) {
            return ResponseEntity.ok(false);
        }

        Long userId = getCurrentUserId(); // Implementar este método

        boolean isFavorite = favoriteService.isFavorite(userId, studioId);
        return ResponseEntity.ok(isFavorite);
    }

    /**
     * {@code GET /favorites/my} : Lista os favoritos do usuário atual
     */
    @GetMapping("/my")
    public ResponseEntity<List<FavoriteDTO>> getMyFavorites() {
        LOG.debug("REST request to get current user favorites");

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Usuário não autenticado"));

        Long userId = getCurrentUserId(); // Implementar este método

        List<FavoriteDTO> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * {@code GET /favorites/my/studio-ids} : Lista os IDs dos studios favoritos do usuário
     */
    @GetMapping("/my/studio-ids")
    public ResponseEntity<List<Long>> getMyFavoriteStudioIds() {
        LOG.debug("REST request to get current user favorite studio IDs");

        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        Long userId = getCurrentUserId(); // Implementar este método

        List<Long> studioIds = favoriteService.getUserFavoriteStudioIds(userId);
        return ResponseEntity.ok(studioIds);
    }

    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId().orElseThrow(() -> new RuntimeException("Não foi possível obter o ID do usuário atual"));
    }
}
