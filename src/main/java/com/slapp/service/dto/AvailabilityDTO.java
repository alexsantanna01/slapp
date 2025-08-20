package com.slapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.Availability} entity.
 */
@Schema(description = "Disponibilidade especial (bloqueios ou liberações)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvailabilityDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startDateTime;

    @NotNull
    private Instant endDateTime;

    @NotNull
    private Boolean available;

    @Size(max = 200)
    private String reason;

    private Instant createdAt;

    @NotNull
    private RoomDTO room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailabilityDTO)) {
            return false;
        }

        AvailabilityDTO availabilityDTO = (AvailabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, availabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvailabilityDTO{" +
            "id=" + getId() +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            ", available='" + getAvailable() + "'" +
            ", reason='" + getReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", room=" + getRoom() +
            "}";
    }
}
