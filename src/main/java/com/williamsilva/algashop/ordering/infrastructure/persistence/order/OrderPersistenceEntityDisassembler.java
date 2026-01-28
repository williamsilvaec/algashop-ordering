package com.williamsilva.algashop.ordering.infrastructure.persistence.order;

import com.williamsilva.algashop.ordering.core.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Quantity;
import com.williamsilva.algashop.ordering.core.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.core.domain.model.order.CreditCardId;
import com.williamsilva.algashop.ordering.core.domain.model.order.Order;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderId;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderItem;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderItemId;
import com.williamsilva.algashop.ordering.core.domain.model.order.OrderStatus;
import com.williamsilva.algashop.ordering.core.domain.model.order.PaymentMethod;
import com.williamsilva.algashop.ordering.core.domain.model.product.ProductId;
import com.williamsilva.algashop.ordering.core.domain.model.product.ProductName;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        CreditCardId creditCardId = null;
        if (persistenceEntity.getCreditCardId() != null) {
            creditCardId = new CreditCardId(persistenceEntity.getCreditCardId());
        }

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
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                .items(toDomainEntity(persistenceEntity.getItems()))
                .creditCardId(creditCardId)
                .build();
    }

    private Set<OrderItem> toDomainEntity(Set<OrderItemPersistenceEntity> items) {
        return items.stream().map(this::toDomainEntity).collect(Collectors.toSet());
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
