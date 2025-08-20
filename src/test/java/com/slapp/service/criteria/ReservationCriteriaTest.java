package com.slapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReservationCriteriaTest {

    @Test
    void newReservationCriteriaHasAllFiltersNullTest() {
        var reservationCriteria = new ReservationCriteria();
        assertThat(reservationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reservationCriteriaFluentMethodsCreatesFiltersTest() {
        var reservationCriteria = new ReservationCriteria();

        setAllFilters(reservationCriteria);

        assertThat(reservationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reservationCriteriaCopyCreatesNullFilterTest() {
        var reservationCriteria = new ReservationCriteria();
        var copy = reservationCriteria.copy();

        assertThat(reservationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reservationCriteria)
        );
    }

    @Test
    void reservationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reservationCriteria = new ReservationCriteria();
        setAllFilters(reservationCriteria);

        var copy = reservationCriteria.copy();

        assertThat(reservationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reservationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reservationCriteria = new ReservationCriteria();

        assertThat(reservationCriteria).hasToString("ReservationCriteria{}");
    }

    private static void setAllFilters(ReservationCriteria reservationCriteria) {
        reservationCriteria.id();
        reservationCriteria.startDateTime();
        reservationCriteria.endDateTime();
        reservationCriteria.totalPrice();
        reservationCriteria.status();
        reservationCriteria.createdAt();
        reservationCriteria.updatedAt();
        reservationCriteria.cancelledAt();
        reservationCriteria.cancelReason();
        reservationCriteria.customerId();
        reservationCriteria.roomId();
        reservationCriteria.distinct();
    }

    private static Condition<ReservationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDateTime()) &&
                condition.apply(criteria.getEndDateTime()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCancelledAt()) &&
                condition.apply(criteria.getCancelReason()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getRoomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReservationCriteria> copyFiltersAre(ReservationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDateTime(), copy.getStartDateTime()) &&
                condition.apply(criteria.getEndDateTime(), copy.getEndDateTime()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCancelledAt(), copy.getCancelledAt()) &&
                condition.apply(criteria.getCancelReason(), copy.getCancelReason()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getRoomId(), copy.getRoomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
