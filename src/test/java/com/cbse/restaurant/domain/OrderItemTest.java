package com.cbse.restaurant.domain;

import static com.cbse.restaurant.domain.MenuItemTestSamples.*;
import static com.cbse.restaurant.domain.OrderItemTestSamples.*;
import static com.cbse.restaurant.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cbse.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void menuItemTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        orderItem.setMenuItem(menuItemBack);
        assertThat(orderItem.getMenuItem()).isEqualTo(menuItemBack);

        orderItem.menuItem(null);
        assertThat(orderItem.getMenuItem()).isNull();
    }

    @Test
    void orderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderItem.setOrder(orderBack);
        assertThat(orderItem.getOrder()).isEqualTo(orderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }
}
