package com.williamsilva.algashop.ordering.domain.exception;

import com.williamsilva.algashop.ordering.domain.valueobject.id.ProductId;
import com.williamsilva.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
