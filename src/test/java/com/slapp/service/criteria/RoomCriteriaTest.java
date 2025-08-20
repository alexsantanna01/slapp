package com.slapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RoomCriteriaTest {

    @Test
    void newRoomCriteriaHasAllFiltersNullTest() {
        var roomCriteria = new RoomCriteria();
        assertThat(roomCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void roomCriteriaFluentMethodsCreatesFiltersTest() {
        var roomCriteria = new RoomCriteria();

        setAllFilters(roomCriteria);

        assertThat(roomCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void roomCriteriaCopyCreatesNullFilterTest() {
        var roomCriteria = new RoomCriteria();
        var copy = roomCriteria.copy();

        assertThat(roomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(roomCriteria)
        );
    }

    @Test
    void roomCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var roomCriteria = new RoomCriteria();
        setAllFilters(roomCriteria);

        var copy = roomCriteria.copy();

        assertThat(roomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(roomCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var roomCriteria = new RoomCriteria();

        assertThat(roomCriteria).hasToString("RoomCriteria{}");
    }

    private static void setAllFilters(RoomCriteria roomCriteria) {
        roomCriteria.id();
        roomCriteria.name();
        roomCriteria.hourlyRate();
        roomCriteria.capacity();
        roomCriteria.soundproofed();
        roomCriteria.airConditioning();
        roomCriteria.roomType();
        roomCriteria.active();
        roomCriteria.createdAt();
        roomCriteria.updatedAt();
        roomCriteria.studioId();
        roomCriteria.distinct();
    }

    private static Condition<RoomCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getHourlyRate()) &&
                condition.apply(criteria.getCapacity()) &&
                condition.apply(criteria.getSoundproofed()) &&
                condition.apply(criteria.getAirConditioning()) &&
                condition.apply(criteria.getRoomType()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getStudioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RoomCriteria> copyFiltersAre(RoomCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getHourlyRate(), copy.getHourlyRate()) &&
                condition.apply(criteria.getCapacity(), copy.getCapacity()) &&
                condition.apply(criteria.getSoundproofed(), copy.getSoundproofed()) &&
                condition.apply(criteria.getAirConditioning(), copy.getAirConditioning()) &&
                condition.apply(criteria.getRoomType(), copy.getRoomType()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getStudioId(), copy.getStudioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
