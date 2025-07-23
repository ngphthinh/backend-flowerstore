package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryMethodValidatorTest {
    private DeliveryMethodValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DeliveryMethodValidator();
        validator.initialize(new ValidDeliveryMethod() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return "";
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
        assertTrue(validator.isValid("PICKUP", null));
        assertTrue(validator.isValid("DELIVERY", null));
    }

    @Test
    void testInvalidDeliveryMethod() {
        assertFalse(validator.isValid("INVALID", null));
    }

    @Test
    void testEmptyString() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    void testWhitespaceString() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    void testNotNull() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void testCaseInsensitive() {
        assertTrue(validator.isValid("pickup", null));
        assertTrue(validator.isValid("delivery", null));
    }

}
