package com.slapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialPriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialPriceDTO.class);
        SpecialPriceDTO specialPriceDTO1 = new SpecialPriceDTO();
        specialPriceDTO1.setId(1L);
        SpecialPriceDTO specialPriceDTO2 = new SpecialPriceDTO();
        assertThat(specialPriceDTO1).isNotEqualTo(specialPriceDTO2);
        specialPriceDTO2.setId(specialPriceDTO1.getId());
        assertThat(specialPriceDTO1).isEqualTo(specialPriceDTO2);
        specialPriceDTO2.setId(2L);
        assertThat(specialPriceDTO1).isNotEqualTo(specialPriceDTO2);
        specialPriceDTO1.setId(null);
        assertThat(specialPriceDTO1).isNotEqualTo(specialPriceDTO2);
    }
}
