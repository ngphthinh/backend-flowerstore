package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;


class MoneyValidatorDoubleTests {
    private MoneyValidatorDouble validatorDouble;

    @BeforeEach
    void setUp() {
        validatorDouble = new MoneyValidatorDouble();
        validatorDouble.initialize(new ValidMoney() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return "";
            }

            @Override
            public int min() {
                return 0;
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

    // Add test methods here to validate the functionality of MoneyValidatorDouble
    @Test
    void testIsValid() {
        Double validValue = 100.50;
        assertTrue(validatorDouble.isValid(validValue, null), "Expected valid money value to return true");

        Double invalidValue = -10.00;
        assertFalse(validatorDouble.isValid(invalidValue, null), "Expected invalid money value to return false");
    }

    @Test
    void testIsValidWithZero() {
        Double zeroValue = 0.00;
        assertTrue(validatorDouble.isValid(zeroValue, null), "Expected zero value to be valid");
    }

    @Test
    void testInValidWithNull() {
        Double nullValue = null;
        assertFalse(validatorDouble.isValid(nullValue, null), "Expected null value to be invalid");
    }

    @Test
    void testIsValidWithNegativeValue() {
        Double negativeValue = -5.00;
        assertFalse(validatorDouble.isValid(negativeValue, null), "Expected negative value to be invalid");
    }

    @Test
    void testIsValidWithLargeValue() {
        Double largeValue = 1_000_000.00;
        assertTrue(validatorDouble.isValid(largeValue, null), "Expected large valid money value to return true");
    }



}
