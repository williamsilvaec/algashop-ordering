package com.williamsilva.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.williamsilva.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartProductAdjustmentService;
import com.williamsilva.algashop.ordering.core.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.core.domain.model.product.ProductId;
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
