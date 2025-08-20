package com.slapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EquipmentCriteriaTest {

    @Test
    void newEquipmentCriteriaHasAllFiltersNullTest() {
        var equipmentCriteria = new EquipmentCriteria();
        assertThat(equipmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void equipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var equipmentCriteria = new EquipmentCriteria();

        setAllFilters(equipmentCriteria);

        assertThat(equipmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void equipmentCriteriaCopyCreatesNullFilterTest() {
        var equipmentCriteria = new EquipmentCriteria();
        var copy = equipmentCriteria.copy();

        assertThat(equipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(equipmentCriteria)
        );
    }

    @Test
    void equipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var equipmentCriteria = new EquipmentCriteria();
        setAllFilters(equipmentCriteria);

        var copy = equipmentCriteria.copy();

        assertThat(equipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(equipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var equipmentCriteria = new EquipmentCriteria();

        assertThat(equipmentCriteria).hasToString("EquipmentCriteria{}");
    }

    private static void setAllFilters(EquipmentCriteria equipmentCriteria) {
        equipmentCriteria.id();
        equipmentCriteria.name();
        equipmentCriteria.brand();
        equipmentCriteria.model();
        equipmentCriteria.available();
        equipmentCriteria.equipmentType();
        equipmentCriteria.createdAt();
        equipmentCriteria.updatedAt();
        equipmentCriteria.roomId();
        equipmentCriteria.distinct();
    }

    private static Condition<EquipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getAvailable()) &&
                condition.apply(criteria.getEquipmentType()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getRoomId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EquipmentCriteria> copyFiltersAre(EquipmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getAvailable(), copy.getAvailable()) &&
                condition.apply(criteria.getEquipmentType(), copy.getEquipmentType()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getRoomId(), copy.getRoomId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
