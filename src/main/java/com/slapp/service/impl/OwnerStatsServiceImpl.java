package com.slapp.service.impl;

import com.slapp.repository.ReservationRepository;
import com.slapp.repository.StudioRepository;
import com.slapp.service.OwnerStatsService;
import com.slapp.service.dto.OwnerStatsDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation para gerenciar estatísticas do proprietário de estúdios.
 */
@Service
@Transactional
public class OwnerStatsServiceImpl implements OwnerStatsService {

    private final Logger log = LoggerFactory.getLogger(OwnerStatsServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final StudioRepository studioRepository;

    public OwnerStatsServiceImpl(ReservationRepository reservationRepository, StudioRepository studioRepository) {
        this.reservationRepository = reservationRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerStatsDTO getOwnerStatsForCurrentMonth(Long ownerId) {
        log.debug("Request to get owner stats for current month : {}", ownerId);

        // Calcular início e fim do mês atual
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDayOfNextMonth = currentDate.with(TemporalAdjusters.firstDayOfNextMonth());

        Instant monthStart = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant monthEnd = firstDayOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Buscar estatísticas
        Long totalReservations = reservationRepository.countReservationsByOwnerAndCurrentMonth(ownerId, monthStart, monthEnd);

        BigDecimal monthlyRevenue = reservationRepository.sumRevenueByOwnerAndCurrentMonth(ownerId, monthStart, monthEnd);

        // Calcular taxa de ocupação
        BigDecimal occupancyRate = calculateOccupancyRate(ownerId, monthStart, monthEnd);

        log.debug(
            "Owner stats calculated - Reservations: {}, Revenue: {}, Occupancy: {}%",
            totalReservations,
            monthlyRevenue,
            occupancyRate
        );

        return new OwnerStatsDTO(totalReservations, monthlyRevenue, occupancyRate);
    }

    private BigDecimal calculateOccupancyRate(Long ownerId, Instant monthStart, Instant monthEnd) {
        // Calcular horas reservadas no mês
        Double reservedHours = reservationRepository.sumReservedHoursByOwnerAndCurrentMonth(ownerId, monthStart, monthEnd);

        if (reservedHours == null || reservedHours == 0.0) {
            return BigDecimal.ZERO;
        }

        // Calcular total de horas disponíveis no mês
        Long totalRooms = studioRepository.countActiveRoomsByOwner(ownerId);

        if (totalRooms == null || totalRooms == 0L) {
            return BigDecimal.ZERO;
        }

        // Assumindo 8 horas de operação por dia (pode ser ajustado conforme regras de negócio)
        int hoursPerDay = 8;
        int daysInMonth = LocalDate.now().lengthOfMonth();
        double totalAvailableHours = totalRooms * hoursPerDay * daysInMonth;

        // Calcular taxa de ocupação como porcentagem
        BigDecimal occupancyRate = BigDecimal.valueOf(reservedHours)
            .divide(BigDecimal.valueOf(totalAvailableHours), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);

        return occupancyRate;
    }
}
