package com.williamsilva.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public record BirthDate(LocalDate value) {

    public BirthDate(LocalDate value) {
        Objects.requireNonNull(value);

        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public Integer age() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
