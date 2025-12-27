package com.williamsilva.algashop.ordering.domain.model.shoppingcart;

import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartCreatedEvent(
        ShoppingCartId shoppingCartId,
        CustomerId customerId,
        OffsetDateTime createdAt
) {}
