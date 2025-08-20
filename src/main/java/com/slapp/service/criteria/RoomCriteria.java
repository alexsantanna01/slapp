package com.slapp.service.criteria;

import com.slapp.domain.enumeration.RoomType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.slapp.domain.Room} entity. This class is used
 * in {@link com.slapp.web.rest.RoomResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rooms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RoomType
     */
    public static class RoomTypeFilter extends Filter<RoomType> {

        public RoomTypeFilter() {}

        public RoomTypeFilter(RoomTypeFilter filter) {
            super(filter);
        }

        @Override
        public RoomTypeFilter copy() {
            return new RoomTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter hourlyRate;

    private IntegerFilter capacity;

    private BooleanFilter soundproofed;

    private BooleanFilter airConditioning;

    private RoomTypeFilter roomType;

    private BooleanFilter active;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter studioId;

    private Boolean distinct;

    public RoomCriteria() {}

    public RoomCriteria(RoomCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.hourlyRate = other.optionalHourlyRate().map(BigDecimalFilter::copy).orElse(null);
        this.capacity = other.optionalCapacity().map(IntegerFilter::copy).orElse(null);
        this.soundproofed = other.optionalSoundproofed().map(BooleanFilter::copy).orElse(null);
        this.airConditioning = other.optionalAirConditioning().map(BooleanFilter::copy).orElse(null);
        this.roomType = other.optionalRoomType().map(RoomTypeFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.studioId = other.optionalStudioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RoomCriteria copy() {
        return new RoomCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getHourlyRate() {
        return hourlyRate;
    }

    public Optional<BigDecimalFilter> optionalHourlyRate() {
        return Optional.ofNullable(hourlyRate);
    }

    public BigDecimalFilter hourlyRate() {
        if (hourlyRate == null) {
            setHourlyRate(new BigDecimalFilter());
        }
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimalFilter hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public IntegerFilter getCapacity() {
        return capacity;
    }

    public Optional<IntegerFilter> optionalCapacity() {
        return Optional.ofNullable(capacity);
    }

    public IntegerFilter capacity() {
        if (capacity == null) {
            setCapacity(new IntegerFilter());
        }
        return capacity;
    }

    public void setCapacity(IntegerFilter capacity) {
        this.capacity = capacity;
    }

    public BooleanFilter getSoundproofed() {
        return soundproofed;
    }

    public Optional<BooleanFilter> optionalSoundproofed() {
        return Optional.ofNullable(soundproofed);
    }

    public BooleanFilter soundproofed() {
        if (soundproofed == null) {
            setSoundproofed(new BooleanFilter());
        }
        return soundproofed;
    }

    public void setSoundproofed(BooleanFilter soundproofed) {
        this.soundproofed = soundproofed;
    }

    public BooleanFilter getAirConditioning() {
        return airConditioning;
    }

    public Optional<BooleanFilter> optionalAirConditioning() {
        return Optional.ofNullable(airConditioning);
    }

    public BooleanFilter airConditioning() {
        if (airConditioning == null) {
            setAirConditioning(new BooleanFilter());
        }
        return airConditioning;
    }

    public void setAirConditioning(BooleanFilter airConditioning) {
        this.airConditioning = airConditioning;
    }

    public RoomTypeFilter getRoomType() {
        return roomType;
    }

    public Optional<RoomTypeFilter> optionalRoomType() {
        return Optional.ofNullable(roomType);
    }

    public RoomTypeFilter roomType() {
        if (roomType == null) {
            setRoomType(new RoomTypeFilter());
        }
        return roomType;
    }

    public void setRoomType(RoomTypeFilter roomType) {
        this.roomType = roomType;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getStudioId() {
        return studioId;
    }

    public Optional<LongFilter> optionalStudioId() {
        return Optional.ofNullable(studioId);
    }

    public LongFilter studioId() {
        if (studioId == null) {
            setStudioId(new LongFilter());
        }
        return studioId;
    }

    public void setStudioId(LongFilter studioId) {
        this.studioId = studioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RoomCriteria that = (RoomCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(hourlyRate, that.hourlyRate) &&
            Objects.equals(capacity, that.capacity) &&
            Objects.equals(soundproofed, that.soundproofed) &&
            Objects.equals(airConditioning, that.airConditioning) &&
            Objects.equals(roomType, that.roomType) &&
            Objects.equals(active, that.active) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(studioId, that.studioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            hourlyRate,
            capacity,
            soundproofed,
            airConditioning,
            roomType,
            active,
            createdAt,
            updatedAt,
            studioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalHourlyRate().map(f -> "hourlyRate=" + f + ", ").orElse("") +
            optionalCapacity().map(f -> "capacity=" + f + ", ").orElse("") +
            optionalSoundproofed().map(f -> "soundproofed=" + f + ", ").orElse("") +
            optionalAirConditioning().map(f -> "airConditioning=" + f + ", ").orElse("") +
            optionalRoomType().map(f -> "roomType=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalStudioId().map(f -> "studioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
