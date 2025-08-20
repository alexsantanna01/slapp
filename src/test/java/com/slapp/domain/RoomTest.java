package com.slapp.domain;

import static com.slapp.domain.RoomTestSamples.*;
import static com.slapp.domain.StudioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Room.class);
        Room room1 = getRoomSample1();
        Room room2 = new Room();
        assertThat(room1).isNotEqualTo(room2);

        room2.setId(room1.getId());
        assertThat(room1).isEqualTo(room2);

        room2 = getRoomSample2();
        assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    void studioTest() {
        Room room = getRoomRandomSampleGenerator();
        Studio studioBack = getStudioRandomSampleGenerator();

        room.setStudio(studioBack);
        assertThat(room.getStudio()).isEqualTo(studioBack);

        room.studio(null);
        assertThat(room.getStudio()).isNull();
    }
}
