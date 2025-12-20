package com.williamsilva.algashop.ordering.domain.model;

import java.util.List;

public interface DomainEventSource {

    List<Object> domainEvent();
    void clearDomaintEvents();
}
