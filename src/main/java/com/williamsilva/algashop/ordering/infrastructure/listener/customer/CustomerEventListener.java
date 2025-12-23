package com.williamsilva.algashop.ordering.infrastructure.listener.customer;

import com.williamsilva.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventListener {

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent listen 1");
    }

    @EventListener
    public void listenSecundary(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent listen 2");
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info("CustomerArchivedEvent listen 1");
    }
}
