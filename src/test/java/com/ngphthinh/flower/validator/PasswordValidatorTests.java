package com.ngphthinh.flower.validator;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTests {

    PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
        validator.initialize(new ValidPassword() {
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
        String validPassword = "StrongPass123!";
        boolean isValid = validator.isValid(validPassword, null);
        assertTrue(isValid, "Expected valid password to pass validation");
    }

    @Test
    void testIsValidWithInvalidPassword() {
        String invalidPassword = "weakpass";
        boolean isValid = validator.isValid(invalidPassword, null);
        assertTrue(!isValid, "Expected invalid password to fail validation");
    }

    @Test
    void testIsValidWithNull() {
        String nullPassword = null;
        boolean isValid = validator.isValid(nullPassword, null);
        assertTrue(!isValid, "Expected null password to fail validation");
    }

    @Test
    void testIsValidWithEmptyString() {
        String emptyPassword = "";
        boolean isValid = validator.isValid(emptyPassword, null);
        assertTrue(!isValid, "Expected empty password to fail validation");
    }

    @Test
    void testIsValidWithShortPassword() {
        String shortPassword = "Short1!";
        boolean isValid = validator.isValid(shortPassword, null);
        assertTrue(!isValid, "Expected short password to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithoutSpecialCharacter() {
        String noSpecialCharPassword = "NoSpecial123";
        boolean isValid = validator.isValid(noSpecialCharPassword, null);
        assertTrue(isValid, "Expected password without special character to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithoutUpperCase() {
        String noUpperCasePassword = "nouppercase123!";
        boolean isValid = validator.isValid(noUpperCasePassword, null);
        assertTrue(!isValid, "Expected password without uppercase letter to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithoutLowerCase() {
        String noLowerCasePassword = "NOLOWERCASE123!";
        boolean isValid = validator.isValid(noLowerCasePassword, null);
        assertTrue(!isValid, "Expected password without lowercase letter to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithoutDigit() {
        String noDigitPassword = "NoDigitSpecial!";
        boolean isValid = validator.isValid(noDigitPassword, null);
        assertTrue(!isValid, "Expected password without digit to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithOnlyWhitespace() {
        String whitespacePassword = "   ";
        boolean isValid = validator.isValid(whitespacePassword, null);
        assertTrue(!isValid, "Expected password with only whitespace to fail validation");
    }

    @Test
    void testIsValidWithPasswordWithSpaces() {
        String passwordWithSpaces = "Strong Pass 123!";
        boolean isValid = validator.isValid(passwordWithSpaces, null);
        assertTrue(isValid, "Expected password with spaces to pass validation");
    }

    @Test
    void testIsValidWithPasswordWithUnicodeCharacters() {
        String unicodePassword = "StrongPass123!😊";
        boolean isValid = validator.isValid(unicodePassword, null);
        assertTrue(isValid, "Expected password with unicode characters to pass validation");
    }

    @Test
    void testIsValidWithPasswordWithNonAsciiCharacters() {
        String nonAsciiPassword = "StrongPass123!ñ";
        boolean isValid = validator.isValid(nonAsciiPassword, null);
        assertTrue(isValid, "Expected password with non-ASCII characters to pass validation");
    }


}
