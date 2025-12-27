package com.williamsilva.algashop.ordering.infrastructure.listener.order;

import com.williamsilva.algashop.ordering.domain.model.order.OrderCanceledEvent;
import com.williamsilva.algashop.ordering.domain.model.order.OrderPaidEvent;
import com.williamsilva.algashop.ordering.domain.model.order.OrderPlacedEvent;
import com.williamsilva.algashop.ordering.domain.model.order.OrderReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    public void listen(OrderPlacedEvent event) {

    }

    @EventListener
    public void listen(OrderPaidEvent event) {

    }

    @EventListener
    public void listen(OrderReadyEvent event) {

    }

    @EventListener
    public void listen(OrderCanceledEvent event) {

    }

}
