package com.slapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CancellationPolicyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancellationPolicyDTO.class);
        CancellationPolicyDTO cancellationPolicyDTO1 = new CancellationPolicyDTO();
        cancellationPolicyDTO1.setId(1L);
        CancellationPolicyDTO cancellationPolicyDTO2 = new CancellationPolicyDTO();
        assertThat(cancellationPolicyDTO1).isNotEqualTo(cancellationPolicyDTO2);
        cancellationPolicyDTO2.setId(cancellationPolicyDTO1.getId());
        assertThat(cancellationPolicyDTO1).isEqualTo(cancellationPolicyDTO2);
        cancellationPolicyDTO2.setId(2L);
        assertThat(cancellationPolicyDTO1).isNotEqualTo(cancellationPolicyDTO2);
        cancellationPolicyDTO1.setId(null);
        assertThat(cancellationPolicyDTO1).isNotEqualTo(cancellationPolicyDTO2);
    }
}
