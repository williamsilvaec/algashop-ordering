package com.williamsilva.algashop.ordering.core.application.order;

import com.williamsilva.algashop.ordering.core.domain.model.order.Order;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderId;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.williamsilva.algashop.ordering.core.domain.model.order.Orders;
import com.williamsilva.algashop.ordering.core.ports.in.order.ForManagingOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService implements ForManagingOrders {

    private final Orders orders;

    @Transactional
    @Override
    public void cancel(String rawOrderId) {
        Order order = findOrder(rawOrderId);
        order.cancel();
        orders.add(order);
    }

    @Transactional
    @Override
    public void markAsPaid(String rawOrderId) {
        Order order = findOrder(rawOrderId);
        order.markAsPaid();
        orders.add(order);
    }

    @Transactional
    @Override
    public void markAsReady(String rawOrderId) {
        Order order = findOrder(rawOrderId);
        order.markAsReady();
        orders.add(order);
    }

    private Order findOrder(String rawOrderId) {
        OrderId orderId = new OrderId(rawOrderId);
        return orders.ofId(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

}
