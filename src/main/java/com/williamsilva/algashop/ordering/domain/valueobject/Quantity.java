package com.williamsilva.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity(Integer value) {
        Objects.requireNonNull(value);

        if (value < 0) {
            throw new IllegalArgumentException("Quantity value cannot be negative");
        }

        this.value = value;
    }

    public Quantity add(Quantity quantity) {
        return new Quantity(this.value + quantity.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Quantity o) {
        return this.value.compareTo(o.value);
    }
}
