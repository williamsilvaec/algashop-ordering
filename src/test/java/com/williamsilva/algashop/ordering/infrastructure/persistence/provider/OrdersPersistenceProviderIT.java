package com.williamsilva.algashop.ordering.infrastructure.persistence.provider;

import com.williamsilva.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.williamsilva.algashop.ordering.domain.model.order.Order;
import com.williamsilva.algashop.ordering.domain.model.order.OrderStatus;
import com.williamsilva.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.williamsilva.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityAssembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.williamsilva.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityDisassembler;
import com.williamsilva.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.williamsilva.algashop.ordering.infrastructure.persistence.order.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceProviderIT {

    private OrdersPersistenceProvider persistenceProvider;
    private CustomersPersistenceProvider customersPersistenceProvider;
    private OrderPersistenceEntityRepository entityRepository;

    @Autowired
    OrdersPersistenceProviderIT(OrdersPersistenceProvider persistenceProvider, CustomersPersistenceProvider customersPersistenceProvider, OrderPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.entityRepository = entityRepository;
    }

    @BeforeEach
    public void setUp() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();
        persistenceProvider.add(order);

        var persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceProvider.add(order);

        persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddFindAndNotFailWhenNoTransaction() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        persistenceProvider.add(order);

        Assertions.assertThatNoException().isThrownBy(() -> persistenceProvider.ofId(order.id()));
    }
}