package com.williamsilva.algashop.ordering.domain.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidations {

    private FieldValidations() {
    }

    public static void requiresValidEmail(String email) {
        requiresValidEmail(email, null);
    }

    public static void requiresValidEmail(String email, String errorMessage) {
        Objects.requireNonNull(email, errorMessage);
        if (email.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requiresNonBlank(String value) {
        requiresNonBlank(value, null);
    }

    public static void requiresNonBlank(String value, String errorMessage) {
        Objects.requireNonNull(value, errorMessage);

        if (value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
