package com.slapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.StudioOperatingHours} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudioOperatingHoursDTO implements Serializable {

    private Long id;

    @NotNull
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull
    private Boolean isOpen;

    private Long studioId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudioOperatingHoursDTO)) {
            return false;
        }

        StudioOperatingHoursDTO studioOperatingHoursDTO = (StudioOperatingHoursDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studioOperatingHoursDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "StudioOperatingHoursDTO{" +
            "id=" +
            getId() +
            ", dayOfWeek='" +
            getDayOfWeek() +
            "'" +
            ", startTime='" +
            getStartTime() +
            "'" +
            ", endTime='" +
            getEndTime() +
            "'" +
            ", isOpen='" +
            getIsOpen() +
            "'" +
            ", studioId=" +
            getStudioId() +
            "}"
        );
    }
}
