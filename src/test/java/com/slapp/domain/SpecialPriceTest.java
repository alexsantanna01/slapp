package com.slapp.domain;

import static com.slapp.domain.RoomTestSamples.*;
import static com.slapp.domain.SpecialPriceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialPrice.class);
        SpecialPrice specialPrice1 = getSpecialPriceSample1();
        SpecialPrice specialPrice2 = new SpecialPrice();
        assertThat(specialPrice1).isNotEqualTo(specialPrice2);

        specialPrice2.setId(specialPrice1.getId());
        assertThat(specialPrice1).isEqualTo(specialPrice2);

        specialPrice2 = getSpecialPriceSample2();
        assertThat(specialPrice1).isNotEqualTo(specialPrice2);
    }

    @Test
    void roomTest() {
        SpecialPrice specialPrice = getSpecialPriceRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        specialPrice.setRoom(roomBack);
        assertThat(specialPrice.getRoom()).isEqualTo(roomBack);

        specialPrice.room(null);
        assertThat(specialPrice.getRoom()).isNull();
    }
}
