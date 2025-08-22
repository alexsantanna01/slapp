package com.slapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Horários de funcionamento do estúdio por dia da semana
 */
@Entity
@Table(name = "studio_operating_hours")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudioOperatingHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @NotNull
    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "cancellationPolicy", "operatingHours" }, allowSetters = true)
    private Studio studio;

    public Long getId() {
        return this.id;
    }

    public StudioOperatingHours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public StudioOperatingHours dayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
        return this;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public StudioOperatingHours startTime(LocalTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public StudioOperatingHours endTime(LocalTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsOpen() {
        return this.isOpen;
    }

    public StudioOperatingHours isOpen(Boolean isOpen) {
        this.setIsOpen(isOpen);
        return this;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Studio getStudio() {
        return this.studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public StudioOperatingHours studio(Studio studio) {
        this.setStudio(studio);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudioOperatingHours)) {
            return false;
        }
        return getId() != null && getId().equals(((StudioOperatingHours) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "StudioOperatingHours{" +
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
            "}"
        );
    }
}
