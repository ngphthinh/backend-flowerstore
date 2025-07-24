package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneValidatorTests {
    PhoneValidator validator ;

    @BeforeEach
    void setUp() {
        validator = new PhoneValidator();
        validator.initialize(new ValidPhone() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
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
        String validPhone = "+84123456789";
        String validPhone2 = "0123456789";
        boolean isValid = validator.isValid(validPhone, null);
        boolean isValid2 = validator.isValid(validPhone2, null);
        assertTrue(isValid, "Expected valid phone number to pass validation");
        assertTrue(isValid2, "Expected valid phone number to pass validation");
    }

    @Test
    void testIsValidWithInvalidPhone() {
        String invalidPhone = "12345";
        boolean isValid = validator.isValid(invalidPhone, null);
        assertTrue(!isValid, "Expected invalid phone number to fail validation");
    }

    @Test
    void testIsValidWithNull() {
        String nullPhone = null;
        boolean isValid = validator.isValid(nullPhone, null);
        assertTrue(isValid, "Expected null phone number to pass validation");
    }

    @Test
    void testIsValidWithEmptyString() {
        String emptyPhone = "";
        boolean isValid = validator.isValid(emptyPhone, null);
        assertTrue(isValid, "Expected empty phone number to pass validation");
    }

    @Test
    void testIsValidWithWhitespace() {
        String whitespacePhone = "   ";
        boolean isValid = validator.isValid(whitespacePhone, null);
        assertTrue(isValid, "Expected whitespace phone number to pass validation");
    }


}
