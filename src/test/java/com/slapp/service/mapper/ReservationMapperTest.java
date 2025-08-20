package com.slapp.service.mapper;

import static com.slapp.domain.ReservationAsserts.*;
import static com.slapp.domain.ReservationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationMapperTest {

    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        reservationMapper = new ReservationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReservationSample1();
        var actual = reservationMapper.toEntity(reservationMapper.toDto(expected));
        assertReservationAllPropertiesEquals(expected, actual);
    }
}
