package com.williamsilva.algashop.ordering.domain.exception;

import com.williamsilva.algashop.ordering.domain.valueobject.id.OrderId;
import com.williamsilva.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException {

    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }
}
