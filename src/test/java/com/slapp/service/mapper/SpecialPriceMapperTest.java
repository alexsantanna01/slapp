package com.slapp.service.mapper;

import static com.slapp.domain.SpecialPriceAsserts.*;
import static com.slapp.domain.SpecialPriceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialPriceMapperTest {

    private SpecialPriceMapper specialPriceMapper;

    @BeforeEach
    void setUp() {
        specialPriceMapper = new SpecialPriceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpecialPriceSample1();
        var actual = specialPriceMapper.toEntity(specialPriceMapper.toDto(expected));
        assertSpecialPriceAllPropertiesEquals(expected, actual);
    }
}
