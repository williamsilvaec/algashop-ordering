package com.williamsilva.algashop.ordering.infrastructure.persistence.provider;

import com.williamsilva.algashop.ordering.domain.model.service.ShoppingCartProductAdjustmentService;
import com.williamsilva.algashop.ordering.domain.model.valueobject.Money;
import com.williamsilva.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.williamsilva.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository repository;

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatedPrice) {
        repository.updateItemPrice(productId.value(), updatedPrice.value());
        repository.recalculateTotalsForCartsWithProduct(productId.value());
    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean isAvailable) {
        repository.updateItemAvailability(productId.value(), isAvailable);
    }
}
