package com.williamsilva.algashop.ordering.domain.model.repository;

import com.williamsilva.algashop.ordering.domain.model.entity.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {

    Optional<ShoppingCart> ofCustomer(CustomerId customerId);
}
