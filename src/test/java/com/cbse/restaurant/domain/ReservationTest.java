package com.cbse.restaurant.domain;

import static com.cbse.restaurant.domain.CustomerTestSamples.*;
import static com.cbse.restaurant.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cbse.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void customerTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        reservation.setCustomer(customerBack);
        assertThat(reservation.getCustomer()).isEqualTo(customerBack);

        reservation.customer(null);
        assertThat(reservation.getCustomer()).isNull();
    }
}
