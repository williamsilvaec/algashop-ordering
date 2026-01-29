package com.williamsilva.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.williamsilva.algashop.ordering.core.application.utility.Mapper;
import com.williamsilva.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.williamsilva.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.williamsilva.algashop.ordering.core.ports.out.shoppingcart.ForObtainingShoppingCarts;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class ForObtainingShoppingCartsJpaRepositoryImpl implements ForObtainingShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository persistenceRepository;
    private final Mapper mapper;

    public ForObtainingShoppingCartsJpaRepositoryImpl(ShoppingCartPersistenceEntityRepository persistenceRepository, Mapper mapper) {
        this.persistenceRepository = persistenceRepository;
        this.mapper = mapper;
    }

    @Override
    public ShoppingCartOutput findById(UUID shoppingCartId) {
        return persistenceRepository.findById(shoppingCartId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(ShoppingCartNotFoundException::new);
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        return persistenceRepository.findByCustomerId(customerId)
                .map(s -> mapper.convert(s, ShoppingCartOutput.class))
                .orElseThrow(ShoppingCartNotFoundException::new);
    }
}
