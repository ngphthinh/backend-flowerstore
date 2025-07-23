package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;


class UpperCaseValidatorTests {
    UpperCaseValidator validator ;

    @BeforeEach
    void setUp() {
        validator = new UpperCaseValidator();
        validator.initialize(new ValidUpperCase(){
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
      String validString = "HELLO";

      boolean isValid = validator.isValid(validString, null);

      assertTrue(isValid, "Expected valid string to pass validation");
    }

    @Test
    void testIsValidWithInvalidString() {
        String invalidString = "Hello";

        boolean isValid = validator.isValid(invalidString, null);

        assertFalse(isValid, "Expected invalid string to fail validation");
    }

    @Test
    void testIsValidWithNull() {
        String nullString = null;

        boolean isValid = validator.isValid(nullString, null);

        assertTrue(isValid, "Expected null to pass validation");
    }

    @Test
    void testIsValidWithEmptyString() {
        String emptyString = "";

        boolean isValid = validator.isValid(emptyString, null);

        assertTrue(isValid, "Expected empty string to pass validation");
    }

    @Test
    void testIsValidWithWhitespace() {
        String whitespaceString = "   ";

        boolean isValid = validator.isValid(whitespaceString, null);

        assertTrue(isValid, "Expected whitespace string to pass validation");
    }


}
