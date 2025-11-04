package com.williamsilva.algashop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        if (value.signum() == -1) {
            throw new IllegalArgumentException();
        }

        this.value = value.setScale(2, roundingMode);
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }

        return new Money(this.value.multiply(new BigDecimal(quantity.value())));
    }

    public Money add(Money money) {
        return new Money(this.value.add(money.value()));
    }

    public Money divide(Money money) {
        return new Money(this.value.divide(money.value(), roundingMode));
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value);
    }
}
