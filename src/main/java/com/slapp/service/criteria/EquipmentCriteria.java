package com.slapp.service.criteria;

import com.slapp.domain.enumeration.EquipmentType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.slapp.domain.Equipment} entity. This class is used
 * in {@link com.slapp.web.rest.EquipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EquipmentType
     */
    public static class EquipmentTypeFilter extends Filter<EquipmentType> {

        public EquipmentTypeFilter() {}

        public EquipmentTypeFilter(EquipmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public EquipmentTypeFilter copy() {
            return new EquipmentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter brand;

    private StringFilter model;

    private BooleanFilter available;

    private EquipmentTypeFilter equipmentType;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter roomId;

    private Boolean distinct;

    public EquipmentCriteria() {}

    public EquipmentCriteria(EquipmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(StringFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.available = other.optionalAvailable().map(BooleanFilter::copy).orElse(null);
        this.equipmentType = other.optionalEquipmentType().map(EquipmentTypeFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.roomId = other.optionalRoomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EquipmentCriteria copy() {
        return new EquipmentCriteria(this);
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

    public StringFilter getBrand() {
        return brand;
    }

    public Optional<StringFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public StringFilter brand() {
        if (brand == null) {
            setBrand(new StringFilter());
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public BooleanFilter getAvailable() {
        return available;
    }

    public Optional<BooleanFilter> optionalAvailable() {
        return Optional.ofNullable(available);
    }

    public BooleanFilter available() {
        if (available == null) {
            setAvailable(new BooleanFilter());
        }
        return available;
    }

    public void setAvailable(BooleanFilter available) {
        this.available = available;
    }

    public EquipmentTypeFilter getEquipmentType() {
        return equipmentType;
    }

    public Optional<EquipmentTypeFilter> optionalEquipmentType() {
        return Optional.ofNullable(equipmentType);
    }

    public EquipmentTypeFilter equipmentType() {
        if (equipmentType == null) {
            setEquipmentType(new EquipmentTypeFilter());
        }
        return equipmentType;
    }

    public void setEquipmentType(EquipmentTypeFilter equipmentType) {
        this.equipmentType = equipmentType;
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

    public LongFilter getRoomId() {
        return roomId;
    }

    public Optional<LongFilter> optionalRoomId() {
        return Optional.ofNullable(roomId);
    }

    public LongFilter roomId() {
        if (roomId == null) {
            setRoomId(new LongFilter());
        }
        return roomId;
    }

    public void setRoomId(LongFilter roomId) {
        this.roomId = roomId;
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
        final EquipmentCriteria that = (EquipmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(available, that.available) &&
            Objects.equals(equipmentType, that.equipmentType) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, model, available, equipmentType, createdAt, updatedAt, roomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalAvailable().map(f -> "available=" + f + ", ").orElse("") +
            optionalEquipmentType().map(f -> "equipmentType=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalRoomId().map(f -> "roomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
