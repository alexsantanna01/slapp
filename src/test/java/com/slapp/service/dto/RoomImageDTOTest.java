package com.slapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomImageDTO.class);
        RoomImageDTO roomImageDTO1 = new RoomImageDTO();
        roomImageDTO1.setId(1L);
        RoomImageDTO roomImageDTO2 = new RoomImageDTO();
        assertThat(roomImageDTO1).isNotEqualTo(roomImageDTO2);
        roomImageDTO2.setId(roomImageDTO1.getId());
        assertThat(roomImageDTO1).isEqualTo(roomImageDTO2);
        roomImageDTO2.setId(2L);
        assertThat(roomImageDTO1).isNotEqualTo(roomImageDTO2);
        roomImageDTO1.setId(null);
        assertThat(roomImageDTO1).isNotEqualTo(roomImageDTO2);
    }
}
