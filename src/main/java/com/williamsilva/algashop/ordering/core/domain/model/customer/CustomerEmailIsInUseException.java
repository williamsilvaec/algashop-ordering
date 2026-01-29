package com.williamsilva.algashop.ordering.core.domain.model.customer;

import com.williamsilva.algashop.ordering.core.domain.model.DomainException;
import com.williamsilva.algashop.ordering.core.domain.model.ErrorMessages;

public class CustomerEmailIsInUseException extends DomainException {
    public CustomerEmailIsInUseException(CustomerId customerId) {
        super(ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE);
    }
}
