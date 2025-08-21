package com.slapp.service.scheduler;

import com.slapp.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for scheduled reservation tasks.
 */
@Service
public class ReservationSchedulerService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationSchedulerService.class);

    private final ReservationService reservationService;

    public ReservationSchedulerService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Auto-confirm pending reservations after 30 minutes.
     * Runs every 5 minutes.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5 minutes in milliseconds
    public void autoConfirmExpiredPendingReservations() {
        LOG.debug("Running auto-confirmation task for expired pending reservations");
        try {
            int confirmedCount = reservationService.autoConfirmExpiredPendingReservations();
            if (confirmedCount > 0) {
                LOG.info("Auto-confirmed {} expired pending reservations", confirmedCount);
            }
        } catch (Exception e) {
            LOG.error("Error during auto-confirmation of pending reservations", e);
        }
    }
}
