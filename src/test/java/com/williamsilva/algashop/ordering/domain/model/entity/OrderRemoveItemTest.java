package com.williamsilva.algashop.ordering.domain.model.entity;

import com.williamsilva.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.williamsilva.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemException;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Money;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Quantity;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderRemoveItemTest {

    @Test
    void givenDraftOrder_whenRemoveItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProduct().build(),
                new Quantity(2)
        );

        OrderItem orderItem1 = order.items().iterator().next();

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(3)
        );

        order.removeItem(orderItem1.id());

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("600.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    void givenDraftOrder_whenTryToRemoveNoExistingItem_shouldGenerateException() {
        Order draftOrder = OrderTestDataBuilder.anOrder().build();

        Assertions.assertThatThrownBy(() -> draftOrder.removeItem(new OrderItemId()))
                .isInstanceOf(OrderDoesNotContainOrderItemException.class);

        Assertions.assertWith(draftOrder,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("6210.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    void givenPlacedOrder_whenTryToRemoveItem_shouldGenerateException() {
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatThrownBy(() -> placedOrder.removeItem(new OrderItemId()))
                .isInstanceOf(OrderCannotBeEditedException.class);

        Assertions.assertWith(placedOrder,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("6210.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }
}
