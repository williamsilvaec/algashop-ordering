package com.williamsilva.algashop.ordering.domain.factory;

import com.williamsilva.algashop.ordering.domain.entity.Order;
import com.williamsilva.algashop.ordering.domain.entity.PaymentMethod;
import com.williamsilva.algashop.ordering.domain.valueobjects.Billing;
import com.williamsilva.algashop.ordering.domain.valueobjects.CustomerId;
import com.williamsilva.algashop.ordering.domain.valueobjects.Product;
import com.williamsilva.algashop.ordering.domain.valueobjects.Quantity;
import com.williamsilva.algashop.ordering.domain.valueobjects.Shipping;

import java.util.Objects;

public class OrderFactory {

    private OrderFactory() {

    }

    public static Order filled(
            CustomerId customerId,
            Shipping shipping,
            Billing billing,
            PaymentMethod paymentMethod,
            Product product,
            Quantity productQuantity
    ) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(productQuantity);

        Order order = Order.draft(customerId);

        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, productQuantity);

        return order;
    }
}
