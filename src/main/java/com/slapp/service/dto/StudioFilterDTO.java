package com.slapp.service.dto;

import java.math.BigDecimal;

public class StudioFilterDTO {

    private String name;
    private String city;
    private String roomType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // Construtor padrão
    public StudioFilterDTO() {}

    // Construtor completo
    public StudioFilterDTO(String name, String city, String roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        this.name = name;
        this.city = city;
        this.roomType = roomType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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

        public StudioFilterDTO build() {
            return new StudioFilterDTO(name, city, roomType, minPrice, maxPrice);
        }
    }
}
