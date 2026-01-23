package com.williamsilva.algashop.ordering.infrastructure.product.client.http;

import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.product.Product;
import com.williamsilva.algashop.ordering.domain.model.product.ProductCatalogService;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import com.williamsilva.algashop.ordering.domain.model.product.ProductName;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductCatalogServerHttpImpl implements ProductCatalogService {

    private final ProductCatalogAPIClient productCatalogAPIClient;

    public ProductCatalogServerHttpImpl(ProductCatalogAPIClient productCatalogAPIClient) {
        this.productCatalogAPIClient = productCatalogAPIClient;
    }

    @Override
    public Optional<Product> ofId(ProductId productId) {
        ProductResponse productResponse = productCatalogAPIClient.getById(productId.value());
        return Optional.of(
                Product.builder()
                        .id(new ProductId(productResponse.getId()))
                        .name(new ProductName(productResponse.getName()))
                        .inStock(productResponse.getInStock())
                        .price(new Money(productResponse.getSalePrice()))
                        .build()
        );
    }
}
