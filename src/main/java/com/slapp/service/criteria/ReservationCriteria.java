package com.slapp.service.criteria;

import com.slapp.domain.enumeration.ReservationStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.slapp.domain.Reservation} entity. This class is used
 * in {@link com.slapp.web.rest.ReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReservationStatus
     */
    public static class ReservationStatusFilter extends Filter<ReservationStatus> {

        public ReservationStatusFilter() {}

        public ReservationStatusFilter(ReservationStatusFilter filter) {
            super(filter);
        }

        @Override
        public ReservationStatusFilter copy() {
            return new ReservationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter startDateTime;

    private InstantFilter endDateTime;

    private BigDecimalFilter totalPrice;

    private ReservationStatusFilter status;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private InstantFilter cancelledAt;

    private StringFilter cancelReason;

    private LongFilter customerId;

    private LongFilter roomId;

    private Boolean distinct;

    public ReservationCriteria() {}

    public ReservationCriteria(ReservationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.startDateTime = other.optionalStartDateTime().map(InstantFilter::copy).orElse(null);
        this.endDateTime = other.optionalEndDateTime().map(InstantFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ReservationStatusFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.cancelledAt = other.optionalCancelledAt().map(InstantFilter::copy).orElse(null);
        this.cancelReason = other.optionalCancelReason().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.roomId = other.optionalRoomId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReservationCriteria copy() {
        return new ReservationCriteria(this);
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

    public InstantFilter getStartDateTime() {
        return startDateTime;
    }

    public Optional<InstantFilter> optionalStartDateTime() {
        return Optional.ofNullable(startDateTime);
    }

    public InstantFilter startDateTime() {
        if (startDateTime == null) {
            setStartDateTime(new InstantFilter());
        }
        return startDateTime;
    }

    public void setStartDateTime(InstantFilter startDateTime) {
        this.startDateTime = startDateTime;
    }

    public InstantFilter getEndDateTime() {
        return endDateTime;
    }

    public Optional<InstantFilter> optionalEndDateTime() {
        return Optional.ofNullable(endDateTime);
    }

    public InstantFilter endDateTime() {
        if (endDateTime == null) {
            setEndDateTime(new InstantFilter());
        }
        return endDateTime;
    }

    public void setEndDateTime(InstantFilter endDateTime) {
        this.endDateTime = endDateTime;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationStatusFilter getStatus() {
        return status;
    }

    public Optional<ReservationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ReservationStatusFilter status() {
        if (status == null) {
            setStatus(new ReservationStatusFilter());
        }
        return status;
    }

    public void setStatus(ReservationStatusFilter status) {
        this.status = status;
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

    public InstantFilter getCancelledAt() {
        return cancelledAt;
    }

    public Optional<InstantFilter> optionalCancelledAt() {
        return Optional.ofNullable(cancelledAt);
    }

    public InstantFilter cancelledAt() {
        if (cancelledAt == null) {
            setCancelledAt(new InstantFilter());
        }
        return cancelledAt;
    }

    public void setCancelledAt(InstantFilter cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public StringFilter getCancelReason() {
        return cancelReason;
    }

    public Optional<StringFilter> optionalCancelReason() {
        return Optional.ofNullable(cancelReason);
    }

    public StringFilter cancelReason() {
        if (cancelReason == null) {
            setCancelReason(new StringFilter());
        }
        return cancelReason;
    }

    public void setCancelReason(StringFilter cancelReason) {
        this.cancelReason = cancelReason;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final ReservationCriteria that = (ReservationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDateTime, that.startDateTime) &&
            Objects.equals(endDateTime, that.endDateTime) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(cancelledAt, that.cancelledAt) &&
            Objects.equals(cancelReason, that.cancelReason) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startDateTime,
            endDateTime,
            totalPrice,
            status,
            createdAt,
            updatedAt,
            cancelledAt,
            cancelReason,
            customerId,
            roomId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStartDateTime().map(f -> "startDateTime=" + f + ", ").orElse("") +
            optionalEndDateTime().map(f -> "endDateTime=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalCancelledAt().map(f -> "cancelledAt=" + f + ", ").orElse("") +
            optionalCancelReason().map(f -> "cancelReason=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalRoomId().map(f -> "roomId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
