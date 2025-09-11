package com.williamsilva.algashop.ordering.domain.entity;

import com.williamsilva.algashop.ordering.domain.valueobjects.Money;
import com.williamsilva.algashop.ordering.domain.valueobjects.Product;
import com.williamsilva.algashop.ordering.domain.valueobjects.ProductName;
import com.williamsilva.algashop.ordering.domain.valueobjects.id.ProductId;

public class ProductTestDataBuilder {

    private ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("Notebook X11"))
                .price(new Money("3000"));
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Desktop FX9000"))
                .price(new Money("5000"))
                .inStock(false);
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("4GB RAM"))
                .price(new Money("200"))
                .inStock(true);
    }

    public static Product.ProductBuilder aProductAltMousePad() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("Mouse Pad"))
                .price(new Money("100"))
                .inStock(true);
    }
}
