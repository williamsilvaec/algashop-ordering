package com.williamsilva.algashop.ordering.infrastructure.persistence.order;

import com.williamsilva.algashop.ordering.domain.model.order.Order;
import com.williamsilva.algashop.ordering.domain.model.order.Orders;
import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.order.OrderId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceRepository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = persistenceRepository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return persistenceRepository.existsById(orderId.value().toLong());
    }

    @Override
    @Transactional
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.id().value().toLong();

        persistenceRepository.findById(orderId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(aggregateRoot, persistenceEntity),
                        ()-> insert(aggregateRoot)
                );

        aggregateRoot.clearDomaintEvents();
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {
        return persistenceRepository.placedByCustomerInYear(customerId.value(), year.getValue())
                .stream()
                .map(disassembler::toDomainEntity)
                .toList();
    }

    @Override
    public long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
        return persistenceRepository.salesQuantityByCustomerInYear(customerId.value(), year.getValue());
    }

    @Override
    public Money totalSoldForCustomer(CustomerId customerId) {
        BigDecimal totalSoldByCustomer = persistenceRepository.totalSoldByCustomer(customerId.value());
        return new Money(totalSoldByCustomer);
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        Field field = aggregateRoot.getClass().getDeclaredField("version");
        field.setAccessible(true);
        ReflectionUtils.setField(field, aggregateRoot, persistenceEntity.getVersion());
        field.setAccessible(false);
    }
}
