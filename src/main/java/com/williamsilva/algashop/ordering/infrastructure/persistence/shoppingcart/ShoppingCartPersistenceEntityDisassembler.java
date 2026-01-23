package com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.product.ProductName;
import com.williamsilva.algashop.ordering.domain.model.commons.Quantity;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity source) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(source.getId()))
                .customerId(new CustomerId(source.getCustomerId()))
                .totalAmount(new Money(source.getTotalAmount()))
                .totalItems(new Quantity(source.getTotalItems()))
                .createdAt(source.getCreatedAt())
                .items(toItemsDomainEntities(source.getItems()))
                .version(source.getVersion())
                .build();
    }

    private Set<ShoppingCartItem> toItemsDomainEntities(Set<ShoppingCartItemPersistenceEntity> source) {
        return source.stream().map(this::toItemEntity).collect(Collectors.toSet());
    }

    private ShoppingCartItem toItemEntity(ShoppingCartItemPersistenceEntity source) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(source.getId()))
                .shoppingCartId(new ShoppingCartId(source.getShoppingCartId()))
                .productId(new ProductId(source.getProductId()))
                .productName(new ProductName(source.getName()))
                .price(new Money(source.getPrice()))
                .quantity(new Quantity(source.getQuantity()))
                .available(source.getAvailable())
                .totalAmount(new Money(source.getTotalAmount()))
                .build();
    }
}
