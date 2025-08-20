package com.slapp.domain;

import static com.slapp.domain.EquipmentTestSamples.*;
import static com.slapp.domain.RoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = getEquipmentSample1();
        Equipment equipment2 = new Equipment();
        assertThat(equipment1).isNotEqualTo(equipment2);

        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);

        equipment2 = getEquipmentSample2();
        assertThat(equipment1).isNotEqualTo(equipment2);
    }

    @Test
    void roomTest() {
        Equipment equipment = getEquipmentRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        equipment.setRoom(roomBack);
        assertThat(equipment.getRoom()).isEqualTo(roomBack);

        equipment.room(null);
        assertThat(equipment.getRoom()).isNull();
    }
}
