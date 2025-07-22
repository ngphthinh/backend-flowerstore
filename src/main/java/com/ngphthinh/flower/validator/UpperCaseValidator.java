package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;

public class UpperCaseValidator implements ConstraintValidator<ValidUpperCase, String> {

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Null or empty values are considered valid
        }
        return value.equals(value.toUpperCase());
    }
}
