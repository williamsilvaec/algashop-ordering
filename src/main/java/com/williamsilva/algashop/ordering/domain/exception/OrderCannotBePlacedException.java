package com.williamsilva.algashop.ordering.domain.exception;

import com.williamsilva.algashop.ordering.domain.valueobjects.id.OrderId;

import static com.williamsilva.algashop.ordering.domain.exception.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITENS;

public class OrderCannotBePlacedException extends DomainException {

    public OrderCannotBePlacedException(OrderId id) {
        super(String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITENS, id));
    }
}
