package com.slapp.service.impl;

import com.slapp.domain.Favorite;
import com.slapp.domain.Studio;
import com.slapp.domain.User;
import com.slapp.repository.FavoriteRepository;
import com.slapp.repository.StudioRepository;
import com.slapp.repository.UserRepository;
import com.slapp.service.FavoriteService;
import com.slapp.service.dto.FavoriteDTO;
import com.slapp.service.mapper.FavoriteMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Favorite}.
 */
@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private static final Logger LOG = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;

    public FavoriteServiceImpl(
        FavoriteRepository favoriteRepository,
        FavoriteMapper favoriteMapper,
        UserRepository userRepository,
        StudioRepository studioRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    public FavoriteDTO save(FavoriteDTO favoriteDTO) {
        LOG.debug("Request to save Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public FavoriteDTO update(FavoriteDTO favoriteDTO) {
        LOG.debug("Request to update Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public Optional<FavoriteDTO> partialUpdate(FavoriteDTO favoriteDTO) {
        LOG.debug("Request to partially update Favorite : {}", favoriteDTO);

        return favoriteRepository
            .findById(favoriteDTO.getId())
            .map(existingFavorite -> {
                favoriteMapper.partialUpdate(existingFavorite, favoriteDTO);
                return existingFavorite;
            })
            .map(favoriteRepository::save)
            .map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Favorites");
        return favoriteRepository.findAll(pageable).map(favoriteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteDTO> findOne(Long id) {
        LOG.debug("Request to get Favorite : {}", id);
        return favoriteRepository.findById(id).map(favoriteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Favorite : {}", id);
        favoriteRepository.deleteById(id);
    }

    @Override
    public FavoriteDTO addToFavorites(Long userId, Long studioId) {
        LOG.debug("Request to add studio {} to favorites of user {}", studioId, userId);

        // Verifica se já existe
        if (favoriteRepository.existsByUserIdAndStudioId(userId, studioId)) {
            throw new RuntimeException("Studio já está nos favoritos");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new RuntimeException("Studio não encontrado"));

        Favorite favorite = new Favorite(user, studio);
        favorite = favoriteRepository.save(favorite);

        return favoriteMapper.toDto(favorite);
    }

    @Override
    public void removeFromFavorites(Long userId, Long studioId) {
        LOG.debug("Request to remove studio {} from favorites of user {}", studioId, userId);
        favoriteRepository.deleteByUserIdAndStudioId(userId, studioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, Long studioId) {
        return favoriteRepository.existsByUserIdAndStudioId(userId, studioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteDTO> getUserFavorites(Long userId) {
        LOG.debug("Request to get favorites of user {}", userId);
        return favoriteRepository.findByUserIdOrderByCreatedDateDesc(userId).stream().map(favoriteMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getUserFavoriteStudioIds(Long userId) {
        LOG.debug("Request to get favorite studio IDs of user {}", userId);
        return favoriteRepository.findStudioIdsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getStudioFavoriteCount(Long studioId) {
        LOG.debug("Request to get favorite count of studio {}", studioId);
        return favoriteRepository.countByStudioId(studioId);
    }
}
