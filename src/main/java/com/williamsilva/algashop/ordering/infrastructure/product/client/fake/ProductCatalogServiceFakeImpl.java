package com.williamsilva.algashop.ordering.infrastructure.product.client.fake;

import com.williamsilva.algashop.ordering.domain.model.product.ProductCatalogService;
import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.product.Product;
import com.williamsilva.algashop.ordering.domain.model.product.ProductName;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
public class ProductCatalogServiceFakeImpl implements ProductCatalogService {

    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder().id(productId)
                .inStock(true)
                .name(new ProductName("Notebook"))
                .price(new Money("3000"))
                .build();

        return Optional.of(product);
    }
}
