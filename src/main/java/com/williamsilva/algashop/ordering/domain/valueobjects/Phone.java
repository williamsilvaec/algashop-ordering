package com.williamsilva.algashop.ordering.domain.valueobjects;

import java.util.Objects;

public record Phone(String value) {

    public Phone(String value) {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.value = value.trim();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
