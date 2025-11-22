package com.williamsilva.algashop.ordering.domain.model.order;

import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.williamsilva.algashop.ordering.domain.model.product.Product;

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
