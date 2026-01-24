package com.williamsilva.algashop.ordering.domain.model.shoppingcart;

import com.williamsilva.algashop.ordering.domain.model.customer.Customers;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartsPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartsIT {

    private ShoppingCarts shoppingCarts;
    private Customers customers;

    @Autowired
    public ShoppingCartsIT(ShoppingCarts shoppingCarts, Customers customers) {
        this.shoppingCarts = shoppingCarts;
        this.customers = customers;
    }

    @BeforeEach
    void setUp() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldPersistAndFind() {
        ShoppingCart originalShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartId shoppingCartId = originalShoppingCart.id();
        shoppingCarts.add(originalShoppingCart);

        Optional<ShoppingCart> possibleShoppingCart = shoppingCarts.ofId(shoppingCartId);

        assertThat(possibleShoppingCart).isPresent();

        ShoppingCart savedShoppingCart = possibleShoppingCart.get();

        assertThat(savedShoppingCart).satisfies(
                s -> assertThat(s.id()).isEqualTo(shoppingCartId),
                s -> assertThat(s.customerId()).isEqualTo(originalShoppingCart.customerId()),
                s -> assertThat(s.items().size()).isEqualTo(originalShoppingCart.items().size()),
                s -> assertThat(s.totalAmount()).isEqualTo(originalShoppingCart.totalAmount()),
                s -> assertThat(s.totalItems()).isEqualTo(originalShoppingCart.totalItems())
        );
    }
}