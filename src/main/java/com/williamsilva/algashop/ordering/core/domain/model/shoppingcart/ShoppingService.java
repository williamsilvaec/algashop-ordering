package com.williamsilva.algashop.ordering.core.domain.model.shoppingcart;

import com.williamsilva.algashop.ordering.core.domain.model.DomainService;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.williamsilva.algashop.ordering.core.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(CustomerId customerId) {
        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        Optional<ShoppingCart> shoppingCartOptional = shoppingCarts.ofCustomer(customerId);

        if (shoppingCartOptional.isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException(customerId);
        }

        return ShoppingCart.startShopping(customerId);
    }
}
