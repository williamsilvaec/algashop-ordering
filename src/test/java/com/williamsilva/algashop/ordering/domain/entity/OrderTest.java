package com.williamsilva.algashop.ordering.domain.entity;

import com.williamsilva.algashop.ordering.domain.valueobjects.CustomerId;
import com.williamsilva.algashop.ordering.domain.valueobjects.Money;
import com.williamsilva.algashop.ordering.domain.valueobjects.ProductName;
import com.williamsilva.algashop.ordering.domain.valueobjects.Quantity;
import com.williamsilva.algashop.ordering.domain.valueobjects.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void shouldCreateOrder() {
        Order order = Order.draft(new CustomerId());
    }

    @Test
    void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Mouse pad"),
                new Money("100"),
                new Quantity(1)
        );

        Assertions.assertThat(order.items()).hasSize(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }
}