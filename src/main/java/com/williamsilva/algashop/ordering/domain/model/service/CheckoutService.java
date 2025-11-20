package com.williamsilva.algashop.ordering.domain.model.service;

import com.williamsilva.algashop.ordering.domain.model.entity.Order;
import com.williamsilva.algashop.ordering.domain.model.entity.PaymentMethod;
import com.williamsilva.algashop.ordering.domain.model.entity.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.williamsilva.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Billing;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Product;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Shipping;

public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod) {

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        for (ShoppingCartItem item : shoppingCart.items()) {
            order.addItem(new Product(item.productId(), item.productName(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }
}
