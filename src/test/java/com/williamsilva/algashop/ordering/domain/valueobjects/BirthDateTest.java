package com.williamsilva.algashop.ordering.domain.valueobjects;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class BirthDateTest {

    @Test
    void testBirthDate() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1989, Month.JANUARY, 1));
        Integer age = birthDate.age();

        Assertions.assertThat(birthDate.value()).isEqualTo(LocalDate.of(1989, Month.JANUARY, 1));
        Assertions.assertThat(age).isEqualTo(1);
    }

    @Test
    void shouldNotAddValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.now().plusYears(1)));
    }
}