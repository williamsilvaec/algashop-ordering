package com.williamsilva.algashop.ordering.domain.model.order;

import com.williamsilva.algashop.ordering.domain.model.commons.Document;
import com.williamsilva.algashop.ordering.domain.model.commons.FullName;
import com.williamsilva.algashop.ordering.domain.model.commons.Phone;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {

    public Recipient {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
    }
}
