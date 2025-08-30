package com.williamsilva.algashop.ordering.domain.valueobjects;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class MoneyTest {

    @Test
    void shouldGenerateWithValue() {
        Money money = new Money("50");

        Assertions.assertThat(money.value()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    void mustPerformOperationCorrectly() {
        Quantity quantity = new Quantity(10);
        Money money = new Money("50");

        Assertions.assertThat(money.multiply(quantity)).isEqualTo(new Money("500"));
        Assertions.assertThat(money.add(new Money("50"))).isEqualTo(new Money("100"));
        Assertions.assertThat(money.divide(new Money("10"))).isEqualTo(new Money("5"));
        Assertions.assertThat(money.toString()).isEqualTo("50.00");
    }
}