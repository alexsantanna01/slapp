package com.slapp.domain;

import static com.slapp.domain.CancellationPolicyTestSamples.*;
import static com.slapp.domain.StudioTestSamples.*;
import static com.slapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Studio.class);
        Studio studio1 = getStudioSample1();
        Studio studio2 = new Studio();
        assertThat(studio1).isNotEqualTo(studio2);

        studio2.setId(studio1.getId());
        assertThat(studio1).isEqualTo(studio2);

        studio2 = getStudioSample2();
        assertThat(studio1).isNotEqualTo(studio2);
    }

    @Test
    void ownerTest() {
        Studio studio = getStudioRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        studio.setOwner(userProfileBack);
        assertThat(studio.getOwner()).isEqualTo(userProfileBack);

        studio.owner(null);
        assertThat(studio.getOwner()).isNull();
    }

    @Test
    void cancellationPolicyTest() {
        Studio studio = getStudioRandomSampleGenerator();
        CancellationPolicy cancellationPolicyBack = getCancellationPolicyRandomSampleGenerator();

        studio.setCancellationPolicy(cancellationPolicyBack);
        assertThat(studio.getCancellationPolicy()).isEqualTo(cancellationPolicyBack);

        studio.cancellationPolicy(null);
        assertThat(studio.getCancellationPolicy()).isNull();
    }
}
