package com.slapp.domain;

import static com.slapp.domain.AvailabilityTestSamples.*;
import static com.slapp.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvailabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Availability.class);
        Availability availability1 = getAvailabilitySample1();
        Availability availability2 = new Availability();
        assertThat(availability1).isNotEqualTo(availability2);

        availability2.setId(availability1.getId());
        assertThat(availability1).isEqualTo(availability2);

        availability2 = getAvailabilitySample2();
        assertThat(availability1).isNotEqualTo(availability2);
    }

    @Test
    void roomTest() {
        Availability availability = getAvailabilityRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        availability.setRoom(roomBack);
        assertThat(availability.getRoom()).isEqualTo(roomBack);

        availability.room(null);
        assertThat(availability.getRoom()).isNull();
    }
}
