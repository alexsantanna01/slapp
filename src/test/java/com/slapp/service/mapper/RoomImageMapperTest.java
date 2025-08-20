package com.slapp.service.mapper;

import static com.slapp.domain.RoomImageAsserts.*;
import static com.slapp.domain.RoomImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomImageMapperTest {

    private RoomImageMapper roomImageMapper;

    @BeforeEach
    void setUp() {
        roomImageMapper = new RoomImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoomImageSample1();
        var actual = roomImageMapper.toEntity(roomImageMapper.toDto(expected));
        assertRoomImageAllPropertiesEquals(expected, actual);
    }
}
