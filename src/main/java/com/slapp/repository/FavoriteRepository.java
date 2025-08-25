package com.slapp.repository;

import com.slapp.domain.Favorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {
    /**
     * Busca todos os favoritos de um usuário
     */
    List<Favorite> findByUserIdOrderByCreatedDateDesc(Long userId);

    /**
     * Verifica se um studio é favorito de um usuário
     */
    boolean existsByUserIdAndStudioId(Long userId, Long studioId);

    /**
     * Busca favorito específico de usuário e studio
     */
    Optional<Favorite> findByUserIdAndStudioId(Long userId, Long studioId);

    /**
     * Remove favorito específico de usuário e studio
     */
    void deleteByUserIdAndStudioId(Long userId, Long studioId);

    /**
     * Conta quantos favoritos um studio possui
     */
    long countByStudioId(Long studioId);

    /**
     * Lista os IDs dos studios favoritados por um usuário
     */
    @Query("SELECT f.studio.id FROM Favorite f WHERE f.user.id = :userId")
    List<Long> findStudioIdsByUserId(@Param("userId") Long userId);
}
