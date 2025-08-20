package com.slapp.service.mapper;

import static com.slapp.domain.EquipmentAsserts.*;
import static com.slapp.domain.EquipmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipmentMapperTest {

    private EquipmentMapper equipmentMapper;

    @BeforeEach
    void setUp() {
        equipmentMapper = new EquipmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEquipmentSample1();
        var actual = equipmentMapper.toEntity(equipmentMapper.toDto(expected));
        assertEquipmentAllPropertiesEquals(expected, actual);
    }
}
