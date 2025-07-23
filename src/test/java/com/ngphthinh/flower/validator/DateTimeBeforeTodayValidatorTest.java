package com.ngphthinh.flower.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DateTimeBeforeTodayValidatorTest {
    DateTimeBeforeTodayValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DateTimeBeforeTodayValidator();
        validator.initialize(new ValidDateBeforeToday() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ValidDateBeforeToday.class;
            }

            @Override
            public String message() {
                return "Date must be after today";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends jakarta.validation.Payload>[] payload() {
                return new Class[0];
            }
        });
    }

    @Test
    void testInValidWithFutureDate() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertFalse(validator.isValid(futureDate, null), "Expected future date to be invalid");
    }

    @Test
    void testInValidWithTodayDate() {
        LocalDateTime todayDate = LocalDateTime.now();
        assertFalse(validator.isValid(todayDate, null), "Expected today's date to be invalid");
    }

    @Test
    void testValidWithPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertTrue(validator.isValid(pastDate, null), "Expected past date to be valid");
    }

    @Test
    void testValidWithNull() {
        LocalDateTime nullDate = null;
        assertTrue(validator.isValid(nullDate, null), "Expected null date to be valid");
    }

}
