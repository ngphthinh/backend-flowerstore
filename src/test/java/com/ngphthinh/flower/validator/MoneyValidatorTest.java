package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValidatorTest {
    private MoneyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MoneyValidator();
        validator.initialize(new ValidMoney() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ValidMoney.class;
            }

            @Override
            public String message() {
                return "Invalid money value";
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

    @Test
    void testIsValid() {
        BigDecimal validMoney = new BigDecimal("100.00");
        assertTrue(validator.isValid(validMoney, null), "Expected valid money string to pass validation");
    }

    @Test
    void testIsValidWithInvalidMoney() {
        BigDecimal invalidMoney = new BigDecimal("-10.00");
        assertFalse(validator.isValid(invalidMoney, null), "Expected invalid money string to fail validation");
    }

    @Test
    void testIsValidWithNull() {
        BigDecimal nullMoney = null;
        assertFalse(validator.isValid(nullMoney, null), "Expected null money to be invalid");
    }

    @Test
    void testIsValidWithZero() {
        BigDecimal zeroMoney = new BigDecimal("0.00");
        assertTrue(validator.isValid(zeroMoney, null), "Expected zero money to be valid");
    }


}
