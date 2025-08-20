package com.slapp.service.mapper;

import static com.slapp.domain.AvailabilityAsserts.*;
import static com.slapp.domain.AvailabilityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AvailabilityMapperTest {

    private AvailabilityMapper availabilityMapper;

    @BeforeEach
    void setUp() {
        availabilityMapper = new AvailabilityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAvailabilitySample1();
        var actual = availabilityMapper.toEntity(availabilityMapper.toDto(expected));
        assertAvailabilityAllPropertiesEquals(expected, actual);
    }
}
