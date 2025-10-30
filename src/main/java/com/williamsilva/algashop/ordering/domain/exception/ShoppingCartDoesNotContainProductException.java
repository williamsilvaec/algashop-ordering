package com.williamsilva.algashop.ordering.domain.exception;

import com.williamsilva.algashop.ordering.domain.valueobject.id.ProductId;
import com.williamsilva.algashop.ordering.domain.valueobject.id.ShoppingCartId;

public class ShoppingCartDoesNotContainProductException extends DomainException {

    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
