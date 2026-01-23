package com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.williamsilva.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.williamsilva.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.williamsilva.algashop.ordering.application.utility.Mapper;
import com.williamsilva.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository persistenceRepository;
    private final Mapper mapper;

    public ShoppingCartQueryServiceImpl(ShoppingCartPersistenceEntityRepository persistenceRepository, Mapper mapper) {
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
