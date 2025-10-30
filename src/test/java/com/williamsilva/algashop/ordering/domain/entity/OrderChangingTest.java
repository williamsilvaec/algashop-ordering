package com.williamsilva.algashop.ordering.domain.entity;

import com.williamsilva.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.williamsilva.algashop.ordering.domain.valueobject.Billing;
import com.williamsilva.algashop.ordering.domain.valueobject.Product;
import com.williamsilva.algashop.ordering.domain.valueobject.Quantity;
import com.williamsilva.algashop.ordering.domain.valueobject.Shipping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderChangingTest {

    @Test
    void givenDraftOrder_whenChangeIsPerformed_shouldNotThrowException() {
        Order draftOrder = OrderTestDataBuilder.anOrder().build();

        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();
        Quantity quantity = new Quantity(1);

        OrderItem orderItem = draftOrder.items().iterator().next();

        Assertions.assertThatCode(() -> draftOrder.addItem(product, quantity)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> draftOrder.changePaymentMethod(paymentMethod)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> draftOrder.changeShipping(shipping)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> draftOrder.changeBilling(billing)).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> draftOrder.changeItemQuantity(orderItem.id(), quantity)).doesNotThrowAnyException();
    }

    @Test
    void givenPlacedOrder_whenChangeBillingIsCalled_shouldThrowOrderCannotBeEditedException() {
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Billing billing = OrderTestDataBuilder.aBilling();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> placedOrder.changeBilling(billing));
    }

    @Test
    void givenPlacedOrder_whenChangeShippingIsCalled_shouldThrowOrderCannotBeEditedException() {
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Shipping shipping = OrderTestDataBuilder.aShipping();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> placedOrder.changeShipping(shipping));
    }

    @Test
    void givenPlacedOrder_whenChangeItemQuantityIsCalled_shouldThrowOrderCannotBeEditedException() {
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        OrderItem orderItem = placedOrder.items().iterator().next();
        Quantity quantity = new Quantity(1);

        Assertions.assertThatThrownBy(() -> placedOrder.changeItemQuantity(orderItem.id(), quantity))
                .isInstanceOf(OrderCannotBeEditedException.class);
    }

    @Test
    void givenPlacedOrder_whenChangePaymentMethodIsCalled_shouldThrowOrderCannotBeEditedException() {
        Order placedOrder = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() -> placedOrder.changePaymentMethod(paymentMethod))
                .isInstanceOf(OrderCannotBeEditedException.class);
    }
}
