package com.slapp.domain;

import static com.slapp.domain.CancellationPolicyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CancellationPolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancellationPolicy.class);
        CancellationPolicy cancellationPolicy1 = getCancellationPolicySample1();
        CancellationPolicy cancellationPolicy2 = new CancellationPolicy();
        assertThat(cancellationPolicy1).isNotEqualTo(cancellationPolicy2);

        cancellationPolicy2.setId(cancellationPolicy1.getId());
        assertThat(cancellationPolicy1).isEqualTo(cancellationPolicy2);

        cancellationPolicy2 = getCancellationPolicySample2();
        assertThat(cancellationPolicy1).isNotEqualTo(cancellationPolicy2);
    }
}
