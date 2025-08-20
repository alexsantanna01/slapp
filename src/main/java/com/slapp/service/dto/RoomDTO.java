package com.slapp.service.dto;

import com.slapp.domain.enumeration.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.Room} entity.
 */
@Schema(description = "Sala de estúdio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal hourlyRate;

    @Min(value = 1)
    private Integer capacity;

    private Boolean soundproofed;

    private Boolean airConditioning;

    @NotNull
    private RoomType roomType;

    @NotNull
    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private StudioDTO studio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getSoundproofed() {
        return soundproofed;
    }

    public void setSoundproofed(Boolean soundproofed) {
        this.soundproofed = soundproofed;
    }

    public Boolean getAirConditioning() {
        return airConditioning;
    }

    public void setAirConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StudioDTO getStudio() {
        return studio;
    }

    public void setStudio(StudioDTO studio) {
        this.studio = studio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDTO)) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hourlyRate=" + getHourlyRate() +
            ", capacity=" + getCapacity() +
            ", soundproofed='" + getSoundproofed() + "'" +
            ", airConditioning='" + getAirConditioning() + "'" +
            ", roomType='" + getRoomType() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", studio=" + getStudio() +
            "}";
    }
}
