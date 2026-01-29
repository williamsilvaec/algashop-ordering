package com.williamsilva.algashop.ordering.core.domain.model.customer;

import com.williamsilva.algashop.ordering.core.domain.model.commons.Email;
import com.williamsilva.algashop.ordering.core.domain.model.commons.FullName;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(CustomerId customerId,
                                      OffsetDateTime registeredAt,
                                      FullName fullName,
                                      Email email) {
}
