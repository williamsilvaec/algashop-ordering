package com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.williamsilva.algashop.ordering.domain.model.customer.CustomerId;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final ShoppingCartPersistenceEntityAssembler assembler;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return repository.findByCustomerId(customerId.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public void remove(ShoppingCart shoppingCart) {
        repository.deleteById(shoppingCart.id().value());
    }

    @Override
    public void remove(ShoppingCartId shoppingCartId) {
        repository.deleteById(shoppingCartId.value());
    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return repository.findById(shoppingCartId.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return repository.existsById(shoppingCartId.value());
    }

    @Override
    @Transactional
    public void add(ShoppingCart aggregateRoot) {
        repository.findById(aggregateRoot.id().value())
                .ifPresentOrElse(
                        (entity -> update(aggregateRoot, entity)),
                        () -> insert(aggregateRoot)
                );

        aggregateRoot.clearDomaintEvents();
    }

    @Override
    public long count() {
        return repository.count();
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity entity) {
        entity = assembler.merge(entity, aggregateRoot);
        entityManager.detach(entity);
        entity = repository.saveAndFlush(entity);
        updateVersion(aggregateRoot, entity);
    }

    private void insert(ShoppingCart aggregateRoot) {
        repository.saveAndFlush(assembler.fromDomain(aggregateRoot));
    }

    @SneakyThrows
    private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity entity) {
        Field field = aggregateRoot.getClass().getDeclaredField("version");
        field.setAccessible(true);
        ReflectionUtils.setField(field, aggregateRoot, entity.getVersion());
        field.setAccessible(false);
    }
}
