package com.slapp.service.mapper;

import static com.slapp.domain.StudioAsserts.*;
import static com.slapp.domain.StudioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudioMapperTest {

    private StudioMapper studioMapper;

    @BeforeEach
    void setUp() {
        studioMapper = new StudioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudioSample1();
        var actual = studioMapper.toEntity(studioMapper.toDto(expected));
        assertStudioAllPropertiesEquals(expected, actual);
    }
}
