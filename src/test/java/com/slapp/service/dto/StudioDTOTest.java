package com.slapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudioDTO.class);
        StudioDTO studioDTO1 = new StudioDTO();
        studioDTO1.setId(1L);
        StudioDTO studioDTO2 = new StudioDTO();
        assertThat(studioDTO1).isNotEqualTo(studioDTO2);
        studioDTO2.setId(studioDTO1.getId());
        assertThat(studioDTO1).isEqualTo(studioDTO2);
        studioDTO2.setId(2L);
        assertThat(studioDTO1).isNotEqualTo(studioDTO2);
        studioDTO1.setId(null);
        assertThat(studioDTO1).isNotEqualTo(studioDTO2);
    }
}
