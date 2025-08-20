package com.slapp.domain;

import static com.slapp.domain.ReservationTestSamples.*;
import static com.slapp.domain.ReviewTestSamples.*;
import static com.slapp.domain.StudioTestSamples.*;
import static com.slapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.slapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void customerTest() {
        Review review = getReviewRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        review.setCustomer(userProfileBack);
        assertThat(review.getCustomer()).isEqualTo(userProfileBack);

        review.customer(null);
        assertThat(review.getCustomer()).isNull();
    }

    @Test
    void studioTest() {
        Review review = getReviewRandomSampleGenerator();
        Studio studioBack = getStudioRandomSampleGenerator();

        review.setStudio(studioBack);
        assertThat(review.getStudio()).isEqualTo(studioBack);

        review.studio(null);
        assertThat(review.getStudio()).isNull();
    }

    @Test
    void reservationTest() {
        Review review = getReviewRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        review.setReservation(reservationBack);
        assertThat(review.getReservation()).isEqualTo(reservationBack);

        review.reservation(null);
        assertThat(review.getReservation()).isNull();
    }
}
