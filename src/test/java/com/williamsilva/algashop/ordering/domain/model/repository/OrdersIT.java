package com.williamsilva.algashop.ordering.domain.model.repository;

import com.williamsilva.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.model.entity.Order;
import com.williamsilva.algashop.ordering.domain.model.entity.OrderStatus;
import com.williamsilva.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.williamsilva.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.williamsilva.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {

    private Orders orders;
    private Customers customers;

    @Autowired
    public OrdersIT(Orders orders, Customers customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @BeforeEach
    public void setUp() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    public void shouldPersistAndFind() {
        Order originalOrder = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = originalOrder.id();
        orders.add(originalOrder);

        Optional<Order> possibleOrder = orders.ofId(orderId);

        assertThat(possibleOrder).isPresent();

        Order savedOrder = possibleOrder.get();

        assertThat(savedOrder).satisfies(
                s -> assertThat(s.id()).isEqualTo(orderId),
                s -> assertThat(s.customerId()).isEqualTo(originalOrder.customerId()),
                s -> assertThat(s.totalAmount()).isEqualTo(originalOrder.totalAmount()),
                s -> assertThat(s.totalItems()).isEqualTo(originalOrder.totalItems()),
                s -> assertThat(s.placedAt()).isEqualTo(originalOrder.placedAt()),
                s -> assertThat(s.paidAt()).isEqualTo(originalOrder.paidAt()),
                s -> assertThat(s.canceledAt()).isEqualTo(originalOrder.canceledAt()),
                s -> assertThat(s.readyAt()).isEqualTo(originalOrder.readyAt()),
                s -> assertThat(s.status()).isEqualTo(originalOrder.status()),
                s -> assertThat(s.paymentMethod()).isEqualTo(originalOrder.paymentMethod())
        );
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Order order1 = orders.ofId(order.id()).orElseThrow();
        Order order2 = orders.ofId(order.id()).orElseThrow();

        order1.markAsPaid();
        orders.add(order1);

        order2.cancel();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orders.add(order2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();

    }

    @Test
    void shouldCountExistingOrders() {
        assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        orders.add(order1);
        orders.add(order2);

        assertThat(orders.count()).isEqualTo(2);
    }

    @Test
    void shouldReturnIfOrderExists() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        assertThat(orders.exists(order.id())).isTrue();
        assertThat(orders.exists(new OrderId())).isFalse();
    }

    @Test
    void shouldListExistingOrdersByYear() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order order3 = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();
        Order order4 = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        List<Order> listedOrders = orders.placedByCustomerInYear(customerId, Year.now());
        assertThat(listedOrders).containsExactlyInAnyOrder(order1, order2);
        assertThat(listedOrders).hasSize(2);

        listedOrders = orders.placedByCustomerInYear(customerId, Year.now().minusYears(1));
        assertThat(listedOrders).isEmpty();

        listedOrders = orders.placedByCustomerInYear(new CustomerId(), Year.now());
        assertThat(listedOrders).isEmpty();
    }
}