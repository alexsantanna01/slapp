package com.slapp.domain;

import static com.slapp.domain.ReservationTestSamples.*;
import static com.slapp.domain.RoomTestSamples.*;
import static com.slapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void customerTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        reservation.setCustomer(userProfileBack);
        assertThat(reservation.getCustomer()).isEqualTo(userProfileBack);

        reservation.customer(null);
        assertThat(reservation.getCustomer()).isNull();
    }

    @Test
    void roomTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        reservation.setRoom(roomBack);
        assertThat(reservation.getRoom()).isEqualTo(roomBack);

        reservation.room(null);
        assertThat(reservation.getRoom()).isNull();
    }
}
