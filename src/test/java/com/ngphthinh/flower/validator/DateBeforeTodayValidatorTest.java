package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateBeforeTodayValidatorTest {

    DateBeforeTodayValidator dateBeforeTodayValidator;

    @BeforeEach
    void setUp() {
        dateBeforeTodayValidator = new DateBeforeTodayValidator();
        dateBeforeTodayValidator.initialize(new ValidDateBeforeToday() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ValidDateBeforeToday.class;
            }

            @Override
            public String message() {
                return "Date must be before today";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }
        });
    }


    @Test
    void testValidDateBeforeToday() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        boolean isValid = dateBeforeTodayValidator.isValid(validDate, null);
        assertTrue(isValid, "Expected valid date before today to pass validation");
    }

    @Test
    void testInvalidDateToday() {
        LocalDate invalidDate = LocalDate.now();
        boolean isValid = dateBeforeTodayValidator.isValid(invalidDate, null);
        assertTrue(!isValid, "Expected today's date to fail validation");
    }

    @Test
    void testInvalidDateAfterToday() {
        LocalDate invalidDate = LocalDate.now().plusDays(1);
        boolean isValid = dateBeforeTodayValidator.isValid(invalidDate, null);
        assertTrue(!isValid, "Expected date after today to fail validation");
    }

    @Test
    void testNullDate() {
        LocalDate nullDate = null;
        boolean isValid = dateBeforeTodayValidator.isValid(nullDate, null);
        assertTrue(isValid, "Expected null date to be valid");
    }

}
