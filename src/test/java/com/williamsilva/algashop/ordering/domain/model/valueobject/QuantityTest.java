package com.williamsilva.algashop.ordering.domain.model.valueobject;

import com.williamsilva.algashop.ordering.domain.model.valueobject.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void shouldGenerateWithValue() {
        Assertions.assertThat(new Quantity(10).value()).isEqualTo(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-1));

        Assertions.assertThat(Quantity.ZERO.value()).isEqualTo(0);
    }

}