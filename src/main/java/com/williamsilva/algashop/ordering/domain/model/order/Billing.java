package com.williamsilva.algashop.ordering.domain.model.order;

import com.williamsilva.algashop.ordering.domain.model.commons.Address;
import com.williamsilva.algashop.ordering.domain.model.commons.Document;
import com.williamsilva.algashop.ordering.domain.model.commons.Email;
import com.williamsilva.algashop.ordering.domain.model.commons.FullName;
import com.williamsilva.algashop.ordering.domain.model.commons.Phone;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Billing(
        FullName fullName,
        Document document,
        Phone phone,
        Email email,
        Address address
) {

    public Billing {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(email);
        Objects.requireNonNull(address);
    }
}
