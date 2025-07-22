package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;

public class MinValueValidator implements ConstraintValidator<MinValue, Number> {

    private double min;

    @Override
    public void initialize(MinValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Number value, jakarta.validation.ConstraintValidatorContext context) {

        // Null values are considered valid and will be handled by @NotNull or similar annotations.
        if (value == null) {
            return true;
        }

        return value.doubleValue() >= min;
    }
}
