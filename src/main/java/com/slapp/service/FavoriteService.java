package com.slapp.service;

import com.slapp.service.dto.FavoriteDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.slapp.domain.Favorite}.
 */
public interface FavoriteService {
    /**
     * Save a favorite.
     *
     * @param favoriteDTO the entity to save.
     * @return the persisted entity.
     */
    FavoriteDTO save(FavoriteDTO favoriteDTO);

    /**
     * Updates a favorite.
     *
     * @param favoriteDTO the entity to update.
     * @return the persisted entity.
     */
    FavoriteDTO update(FavoriteDTO favoriteDTO);

    /**
     * Partially updates a favorite.
     *
     * @param favoriteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FavoriteDTO> partialUpdate(FavoriteDTO favoriteDTO);

    /**
     * Get all the favorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavoriteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" favorite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoriteDTO> findOne(Long id);

    /**
     * Delete the "id" favorite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Adiciona um studio aos favoritos do usuário
     */
    FavoriteDTO addToFavorites(Long userId, Long studioId);

    /**
     * Remove um studio dos favoritos do usuário
     */
    void removeFromFavorites(Long userId, Long studioId);

    /**
     * Verifica se um studio é favorito do usuário
     */
    boolean isFavorite(Long userId, Long studioId);

    /**
     * Lista todos os favoritos de um usuário
     */
    List<FavoriteDTO> getUserFavorites(Long userId);

    /**
     * Lista os IDs dos studios favoritos de um usuário
     */
    List<Long> getUserFavoriteStudioIds(Long userId);

    /**
     * Conta quantos favoritos um studio possui
     */
    long getStudioFavoriteCount(Long studioId);
}
