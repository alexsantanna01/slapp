package com.slapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slapp.domain.enumeration.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Sala de estúdio
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "hourly_rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal hourlyRate;

    @Min(value = 1)
    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "soundproofed")
    private Boolean soundproofed;

    @Column(name = "air_conditioning")
    private Boolean airConditioning;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner", "cancellationPolicy" }, allowSetters = true)
    private Studio studio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Room name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Room description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getHourlyRate() {
        return this.hourlyRate;
    }

    public Room hourlyRate(BigDecimal hourlyRate) {
        this.setHourlyRate(hourlyRate);
        return this;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Room capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getSoundproofed() {
        return this.soundproofed;
    }

    public Room soundproofed(Boolean soundproofed) {
        this.setSoundproofed(soundproofed);
        return this;
    }

    public void setSoundproofed(Boolean soundproofed) {
        this.soundproofed = soundproofed;
    }

    public Boolean getAirConditioning() {
        return this.airConditioning;
    }

    public Room airConditioning(Boolean airConditioning) {
        this.setAirConditioning(airConditioning);
        return this;
    }

    public void setAirConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public RoomType getRoomType() {
        return this.roomType;
    }

    public Room roomType(RoomType roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Room active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Room createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Room updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Studio getStudio() {
        return this.studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public Room studio(Studio studio) {
        this.setStudio(studio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return getId() != null && getId().equals(((Room) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
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
            "}";
    }
}
