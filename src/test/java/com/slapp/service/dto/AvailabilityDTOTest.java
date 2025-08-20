package com.slapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvailabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvailabilityDTO.class);
        AvailabilityDTO availabilityDTO1 = new AvailabilityDTO();
        availabilityDTO1.setId(1L);
        AvailabilityDTO availabilityDTO2 = new AvailabilityDTO();
        assertThat(availabilityDTO1).isNotEqualTo(availabilityDTO2);
        availabilityDTO2.setId(availabilityDTO1.getId());
        assertThat(availabilityDTO1).isEqualTo(availabilityDTO2);
        availabilityDTO2.setId(2L);
        assertThat(availabilityDTO1).isNotEqualTo(availabilityDTO2);
        availabilityDTO1.setId(null);
        assertThat(availabilityDTO1).isNotEqualTo(availabilityDTO2);
    }
}
