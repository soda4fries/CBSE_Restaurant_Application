package com.cbse.restaurant.domain;

import static com.cbse.restaurant.domain.CustomerTestSamples.*;
import static com.cbse.restaurant.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cbse.restaurant.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void reservationsTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        customer.addReservations(reservationBack);
        assertThat(customer.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getCustomer()).isEqualTo(customer);

        customer.removeReservations(reservationBack);
        assertThat(customer.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getCustomer()).isNull();

        customer.reservations(new HashSet<>(Set.of(reservationBack)));
        assertThat(customer.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getCustomer()).isEqualTo(customer);

        customer.setReservations(new HashSet<>());
        assertThat(customer.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getCustomer()).isNull();
    }
}
