package com.williamsilva.algashop.ordering.infrastructure.listener.customer;

import com.williamsilva.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.williamsilva.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {

    private final CustomerNotificationApplicationService notificationApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent listen 1");
        var input = new NotifyNewRegistrationInput(event.customerId().value(),
                event.fullName().firstName(), event.email().value());

        notificationApplicationService.notifyNewRegistration(input);
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info("CustomerArchivedEvent listen 1");
    }
}
