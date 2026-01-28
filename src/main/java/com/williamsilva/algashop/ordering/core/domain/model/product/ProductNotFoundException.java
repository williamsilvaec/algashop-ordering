package com.williamsilva.algashop.ordering.core.domain.model.product;

import com.williamsilva.algashop.ordering.core.domain.model.DomainEntityNotFoundException;

import static com.williamsilva.algashop.ordering.core.domain.model.ErrorMessages.ERROR_PRODUCT_NOT_FOUND;

public class ProductNotFoundException extends DomainEntityNotFoundException {

    public ProductNotFoundException() {}

    public ProductNotFoundException(ProductId id) {
        super(String.format(ERROR_PRODUCT_NOT_FOUND, id));
    }
}
