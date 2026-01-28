package com.williamsilva.algashop.ordering.core.domain.model.customer;

import com.williamsilva.algashop.ordering.core.domain.model.DomainException;
import static com.williamsilva.algashop.ordering.core.domain.model.ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE;

public class CustomerEmailIsInUseException extends DomainException {

    public CustomerEmailIsInUseException() {
        super(ERROR_CUSTOMER_EMAIL_IS_IN_USE);
    }
}
