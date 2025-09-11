package com.williamsilva.algashop.ordering.domain.entity;


import com.williamsilva.algashop.ordering.domain.valueobjects.Quantity;
import com.williamsilva.algashop.ordering.domain.valueobjects.id.OrderId;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerate() {
        OrderItem.brandNew()
                .product(ProductTestDataBuilder.aProduct().build())
                .quantity(new Quantity(1))
                .orderId(new OrderId())
                .build();
    }
}