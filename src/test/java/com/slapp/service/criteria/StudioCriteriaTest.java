package com.slapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudioCriteriaTest {

    @Test
    void newStudioCriteriaHasAllFiltersNullTest() {
        var studioCriteria = new StudioCriteria();
        assertThat(studioCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studioCriteriaFluentMethodsCreatesFiltersTest() {
        var studioCriteria = new StudioCriteria();

        setAllFilters(studioCriteria);

        assertThat(studioCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studioCriteriaCopyCreatesNullFilterTest() {
        var studioCriteria = new StudioCriteria();
        var copy = studioCriteria.copy();

        assertThat(studioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studioCriteria)
        );
    }

    @Test
    void studioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studioCriteria = new StudioCriteria();
        setAllFilters(studioCriteria);

        var copy = studioCriteria.copy();

        assertThat(studioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studioCriteria = new StudioCriteria();

        assertThat(studioCriteria).hasToString("StudioCriteria{}");
    }

    private static void setAllFilters(StudioCriteria studioCriteria) {
        studioCriteria.id();
        studioCriteria.name();
        studioCriteria.address();
        studioCriteria.city();
        studioCriteria.state();
        studioCriteria.zipCode();
        studioCriteria.latitude();
        studioCriteria.longitude();
        studioCriteria.phone();
        studioCriteria.email();
        studioCriteria.website();
        studioCriteria.image();
        studioCriteria.active();
        studioCriteria.createdAt();
        studioCriteria.updatedAt();
        studioCriteria.ownerId();
        studioCriteria.cancellationPolicyId();
        studioCriteria.distinct();
    }

    private static Condition<StudioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getState()) &&
                condition.apply(criteria.getZipCode()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getImage()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getCancellationPolicyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudioCriteria> copyFiltersAre(StudioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getState(), copy.getState()) &&
                condition.apply(criteria.getZipCode(), copy.getZipCode()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getImage(), copy.getImage()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getCancellationPolicyId(), copy.getCancellationPolicyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
