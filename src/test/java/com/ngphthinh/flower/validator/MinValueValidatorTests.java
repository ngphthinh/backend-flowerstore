package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinValueValidatorTests {


    private MinValueValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MinValueValidator();
        validator.initialize(new MinValue() {

            @Override
            public int min() {
                return 10;
            }

            @Override
            public String message() {
                return "Invalid minimum value";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return MinValue.class;
            }
        });
    }


    @Test
    void testValidValue() {
        assertTrue(validator.isValid(15, null));
    }

    @Test
    void testInvalidValue() {
        assertFalse(validator.isValid(5, null));
    }

    @Test
    void testNullValue() {
        assertTrue(validator.isValid(null, null)); // null is valid unless @NotNull present
    }
}
