package com.williamsilva.algashop.ordering.infrastructure.persistence.order;

import com.williamsilva.algashop.ordering.domain.model.order.Order;
import com.williamsilva.algashop.ordering.domain.model.order.OrderItem;
import com.williamsilva.algashop.ordering.domain.model.order.OrderStatus;
import com.williamsilva.algashop.ordering.domain.model.order.PaymentMethod;
import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.product.ProductName;
import com.williamsilva.algashop.ordering.domain.model.commons.Quantity;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.order.OrderId;
import com.williamsilva.algashop.ordering.domain.model.order.OrderItemId;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .items(toDomainEntity(persistenceEntity.getItems()))
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Set<OrderItem> toDomainEntity(Set<OrderItemPersistenceEntity> OrderItemPersistenceEntity) {
        return OrderItemPersistenceEntity.stream().map(this::toDomainEntity).collect(Collectors.toSet());
    }

    private OrderItem toDomainEntity(OrderItemPersistenceEntity orderItemPersistenceEntity) {
        return OrderItem.existing()
                .id(new OrderItemId(orderItemPersistenceEntity.getId()))
                .orderId(new OrderId(orderItemPersistenceEntity.getOrderId()))
                .productId(new ProductId(orderItemPersistenceEntity.getProductId()))
                .productName(new ProductName(orderItemPersistenceEntity.getProductName()))
                .price(new Money(orderItemPersistenceEntity.getPrice()))
                .quantity(new Quantity(orderItemPersistenceEntity.getQuantity()))
                .totalAmount(new Money(orderItemPersistenceEntity.getTotalAmount()))
                .build();
    }
}
