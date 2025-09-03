package com.slapp.service;

import com.slapp.service.dto.OwnerStatsDTO;

/**
 * Service Interface para gerenciar estatísticas do proprietário de estúdios.
 */
public interface OwnerStatsService {
    /**
     * Calcula as estatísticas do proprietário para o mês atual.
     *
     * @param ownerId the id of the owner
     * @return the owner statistics for current month
     */
    OwnerStatsDTO getOwnerStatsForCurrentMonth(Long ownerId);
}
