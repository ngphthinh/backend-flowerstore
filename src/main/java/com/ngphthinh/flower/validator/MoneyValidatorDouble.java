package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class MoneyValidatorDouble implements ConstraintValidator<ValidMoney, Double> {

    private int min;

    @Override
    public void initialize(ValidMoney constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Double money, ConstraintValidatorContext constraintValidatorContext) {
        return money != null && money >= min;
    }
}
