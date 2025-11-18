package com.williamsilva.algashop.ordering.domain.model.repository;

import com.williamsilva.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.model.entity.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.williamsilva.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.williamsilva.algashop.ordering.infrastructure.persistence.provider.ShoppingCartPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({
        ShoppingCartPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
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
                s -> assertThat(s.createdAt()).isEqualTo(originalShoppingCart.createdAt()),
                s -> assertThat(s.items().size()).isEqualTo(originalShoppingCart.items().size()),
                s -> assertThat(s.totalAmount()).isEqualTo(originalShoppingCart.totalAmount()),
                s -> assertThat(s.totalItems()).isEqualTo(originalShoppingCart.totalItems())
        );
    }
}