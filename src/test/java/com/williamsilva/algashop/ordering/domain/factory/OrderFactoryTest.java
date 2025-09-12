package com.williamsilva.algashop.ordering.domain.factory;


import com.williamsilva.algashop.ordering.domain.entity.Order;
import com.williamsilva.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.entity.PaymentMethod;
import com.williamsilva.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.valueobjects.Billing;
import com.williamsilva.algashop.ordering.domain.valueobjects.CustomerId;
import com.williamsilva.algashop.ordering.domain.valueobjects.Product;
import com.williamsilva.algashop.ordering.domain.valueobjects.Quantity;
import com.williamsilva.algashop.ordering.domain.valueobjects.Shipping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

    @Test
    public void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();

        Product product = ProductTestDataBuilder.aProduct().build();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();

        Order order = OrderFactory.filled(
                customerId, shipping, billing, paymentMethod, product, quantity
        );

        Assertions.assertWith(order,
                o-> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o-> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o-> Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o-> Assertions.assertThat(o.items()).isNotEmpty(),
                o-> Assertions.assertThat(o.customerId()).isNotNull(),
                o-> Assertions.assertThat(o.isDraft()).isTrue()
        );

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();

    }
}