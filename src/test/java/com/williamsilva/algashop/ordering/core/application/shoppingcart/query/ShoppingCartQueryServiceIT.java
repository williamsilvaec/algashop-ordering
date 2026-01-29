package com.williamsilva.algashop.ordering.core.application.shoppingcart.query;

import com.williamsilva.algashop.ordering.core.application.AbstractApplicationIT;
import com.williamsilva.algashop.ordering.core.domain.model.customer.Customer;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.williamsilva.algashop.ordering.core.domain.model.customer.Customers;
import com.williamsilva.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.williamsilva.algashop.ordering.core.domain.model.shoppingcart.ShoppingCarts;
import com.williamsilva.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.williamsilva.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ShoppingCartQueryServiceIT extends AbstractApplicationIT {

    @Autowired
    private ForQueryingShoppingCarts queryService;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Test
    public void shouldFindById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput output = queryService.findById(shoppingCart.id().value());
        Assertions.assertWith(output,
                o -> Assertions.assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
                o -> Assertions.assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value())
        );
    }

    @Test
    public void shouldFindByCustomerId() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput output = queryService.findByCustomerId(customer.id().value());
        Assertions.assertWith(output,
                o -> Assertions.assertThat(o.getId()).isEqualTo(shoppingCart.id().value()),
                o -> Assertions.assertThat(o.getCustomerId()).isEqualTo(shoppingCart.customerId().value())
        );
    }
}