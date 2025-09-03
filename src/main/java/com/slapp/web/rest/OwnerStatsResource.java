package com.slapp.web.rest;

import com.slapp.service.OwnerStatsService;
import com.slapp.service.dto.OwnerStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller para gerenciar estatísticas do proprietário de estúdios.
 */
@RestController
@RequestMapping("/api/owner-stats")
public class OwnerStatsResource {

    private final Logger log = LoggerFactory.getLogger(OwnerStatsResource.class);

    private final OwnerStatsService ownerStatsService;

    public OwnerStatsResource(OwnerStatsService ownerStatsService) {
        this.ownerStatsService = ownerStatsService;
    }

    /**
     * {@code GET  /owner-stats/{ownerId}/current-month} : busca as estatísticas do proprietário para o mês atual.
     *
     * @param ownerId the id of the owner to retrieve stats for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the owner stats,
     * or with status {@code 404 (Not Found)} if the owner is not found.
     */
    @GetMapping("/{ownerId}/current-month")
    public ResponseEntity<OwnerStatsDTO> getOwnerStatsForCurrentMonth(@PathVariable Long ownerId) {
        log.debug("REST request to get owner stats for current month : {}", ownerId);

        OwnerStatsDTO ownerStats = ownerStatsService.getOwnerStatsForCurrentMonth(ownerId);
        return ResponseEntity.ok().body(ownerStats);
    }
}
