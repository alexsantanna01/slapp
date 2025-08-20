package com.slapp.domain;

import static com.slapp.domain.RoomImageTestSamples.*;
import static com.slapp.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomImage.class);
        RoomImage roomImage1 = getRoomImageSample1();
        RoomImage roomImage2 = new RoomImage();
        assertThat(roomImage1).isNotEqualTo(roomImage2);

        roomImage2.setId(roomImage1.getId());
        assertThat(roomImage1).isEqualTo(roomImage2);

        roomImage2 = getRoomImageSample2();
        assertThat(roomImage1).isNotEqualTo(roomImage2);
    }

    @Test
    void roomTest() {
        RoomImage roomImage = getRoomImageRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        roomImage.setRoom(roomBack);
        assertThat(roomImage.getRoom()).isEqualTo(roomBack);

        roomImage.room(null);
        assertThat(roomImage.getRoom()).isNull();
    }
}
