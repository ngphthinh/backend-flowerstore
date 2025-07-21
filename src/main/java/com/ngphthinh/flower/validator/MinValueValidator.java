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
        return value != null && value.doubleValue() >= min;
    }
}
