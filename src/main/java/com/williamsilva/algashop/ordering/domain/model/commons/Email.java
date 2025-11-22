package com.williamsilva.algashop.ordering.domain.model.commons;

import com.williamsilva.algashop.ordering.domain.model.FieldValidations;

public record Email(String value) {

    public Email(String value) {
        FieldValidations.requiresValidEmail(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
