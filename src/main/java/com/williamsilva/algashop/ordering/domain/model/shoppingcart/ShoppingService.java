package com.williamsilva.algashop.ordering.domain.model.shoppingcart;

import com.williamsilva.algashop.ordering.domain.model.DomainService;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.williamsilva.algashop.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(CustomerId customerId) {
        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }

        Optional<ShoppingCart> shoppingCartOptional = shoppingCarts.ofCustomer(customerId);

        if (shoppingCartOptional.isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException();
        }

        return ShoppingCart.startShopping(customerId);
    }
}
