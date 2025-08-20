package com.slapp.service.mapper;

import static com.slapp.domain.RoomAsserts.*;
import static com.slapp.domain.RoomTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomMapperTest {

    private RoomMapper roomMapper;

    @BeforeEach
    void setUp() {
        roomMapper = new RoomMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoomSample1();
        var actual = roomMapper.toEntity(roomMapper.toDto(expected));
        assertRoomAllPropertiesEquals(expected, actual);
    }
}
