package com.slapp.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StudioFilterDTO {

    private String name;
    private String city;
    private String roomType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDateTime availabilityStartDateTime;
    private LocalDateTime availabilityEndDateTime;

    // Construtor completo
    public StudioFilterDTO(
        String name,
        String city,
        String roomType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        LocalDateTime availabilityStartDateTime,
        LocalDateTime availabilityEndDateTime
    ) {
        this.name = name;
        this.city = city;
        this.roomType = roomType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.availabilityStartDateTime = availabilityStartDateTime;
        this.availabilityEndDateTime = availabilityEndDateTime;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getRoomType() {
        return roomType;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public LocalDateTime getAvailabilityStartDateTime() {
        return availabilityStartDateTime;
    }

    public LocalDateTime getAvailabilityEndDateTime() {
        return availabilityEndDateTime;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setAvailabilityStartDateTime(LocalDateTime availabilityStartDateTime) {
        this.availabilityStartDateTime = availabilityStartDateTime;
    }

    public void setAvailabilityEndDateTime(LocalDateTime availabilityEndDateTime) {
        this.availabilityEndDateTime = availabilityEndDateTime;
    }

    // Métodos de validação
    public boolean hasNameFilter() {
        return name != null && !name.trim().isEmpty();
    }

    public boolean hasCityFilter() {
        return city != null && !city.trim().isEmpty();
    }

    public boolean hasRoomTypeFilter() {
        return roomType != null && !roomType.trim().isEmpty();
    }

    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }

    public boolean hasAvailabilityFilter() {
        return availabilityStartDateTime != null && availabilityEndDateTime != null;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String city;
        private String roomType;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private LocalDateTime availabilityStartDateTime;
        private LocalDateTime availabilityEndDateTime;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder minPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder availabilityStartDateTime(LocalDateTime availabilityStartDateTime) {
            this.availabilityStartDateTime = availabilityStartDateTime;
            return this;
        }

        public Builder availabilityEndDateTime(LocalDateTime availabilityEndDateTime) {
            this.availabilityEndDateTime = availabilityEndDateTime;
            return this;
        }

        public StudioFilterDTO build() {
            return new StudioFilterDTO(name, city, roomType, minPrice, maxPrice, availabilityStartDateTime, availabilityEndDateTime);
        }
    }
}
