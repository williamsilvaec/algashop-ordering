package com.williamsilva.algashop.ordering.core.domain.model;

import java.util.List;

public interface DomainEventSource {

    List<Object> domainEvents();
    void clearDomaintEvents();
}
