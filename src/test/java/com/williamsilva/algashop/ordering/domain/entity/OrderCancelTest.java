package com.williamsilva.algashop.ordering.domain.entity;

import com.williamsilva.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

public class OrderCancelTest {

    @Test
    void givenDraftOrder_whenCancel_shouldUpdateStatusAndTimestamp() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();
        order.cancel();

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED),
                (o) -> Assertions.assertThat(order.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenPlacedOrder_whenCancel_shouldUpdateStatusAndTimestamp() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        order.cancel();

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED),
                (o) -> Assertions.assertThat(order.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenPaidOrder_whenCancel_shouldUpdateStatusAndTimestamp() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        order.cancel();

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED),
                (o) -> Assertions.assertThat(order.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenReadyOrder_whenCancel_shouldUpdateStatusAndTimestamp() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();
        order.cancel();

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED),
                (o) -> Assertions.assertThat(order.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenCanceledOrder_whenCancel_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();
        OffsetDateTime canceledAt = order.canceledAt();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                (o) -> Assertions.assertThat(o.canceledAt()).isEqualTo(canceledAt)
        );
    }
}
