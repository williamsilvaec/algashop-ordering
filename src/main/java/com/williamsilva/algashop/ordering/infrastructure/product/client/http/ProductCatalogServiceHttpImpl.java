package com.williamsilva.algashop.ordering.infrastructure.product.client.http;

import com.williamsilva.algashop.ordering.domain.model.commons.Money;
import com.williamsilva.algashop.ordering.domain.model.product.Product;
import com.williamsilva.algashop.ordering.domain.model.product.ProductCatalogService;
import com.williamsilva.algashop.ordering.domain.model.product.ProductId;
import com.williamsilva.algashop.ordering.domain.model.product.ProductName;
import com.williamsilva.algashop.ordering.presentation.BadGatewayException;
import com.williamsilva.algashop.ordering.presentation.GatewayTimeoutException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Component
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {

    private final ProductCatalogAPIClient productCatalogAPIClient;

    public ProductCatalogServiceHttpImpl(ProductCatalogAPIClient productCatalogAPIClient) {
        this.productCatalogAPIClient = productCatalogAPIClient;
    }

    @Override
    public Optional<Product> ofId(ProductId productId) {
        ProductResponse productResponse;

        try {
            productResponse = productCatalogAPIClient.getById(productId.value());
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Product Catalog API Timeout", e);
        } catch (HttpClientErrorException e) {
            throw new BadGatewayException("Product Catalog API Bad Gateway", e);
        }

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
