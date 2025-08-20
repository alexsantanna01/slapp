package com.slapp.service.dto;

import com.slapp.domain.enumeration.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.SpecialPrice} entity.
 */
@Schema(description = "Preços especiais por horário ou dia da semana")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialPriceDTO implements Serializable {

    private Long id;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean active;

    @NotNull
    private RoomDTO room;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof SpecialPriceDTO)) {
            return false;
        }

        SpecialPriceDTO specialPriceDTO = (SpecialPriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialPriceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialPriceDTO{" +
            "id=" + getId() +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            ", room=" + getRoom() +
            "}";
    }
}
