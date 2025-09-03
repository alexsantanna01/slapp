package com.slapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para as estatísticas do proprietário de estúdios
 */
public class OwnerStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalReservations;
    private BigDecimal monthlyRevenue;
    private BigDecimal occupancyRate;

    public OwnerStatsDTO() {}

    public OwnerStatsDTO(Long totalReservations, BigDecimal monthlyRevenue, BigDecimal occupancyRate) {
        this.totalReservations = totalReservations;
        this.monthlyRevenue = monthlyRevenue;
        this.occupancyRate = occupancyRate;
    }

    public Long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(Long totalReservations) {
        this.totalReservations = totalReservations;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(BigDecimal occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    @Override
    public String toString() {
        return (
            "OwnerStatsDTO{" +
            "totalReservations=" +
            totalReservations +
            ", monthlyRevenue=" +
            monthlyRevenue +
            ", occupancyRate=" +
            occupancyRate +
            '}'
        );
    }
}
