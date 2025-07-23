package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateBeforeTodayValidator implements ConstraintValidator<ValidDateBeforeToday, LocalDate> {

    @Override
    public void initialize(ValidDateBeforeToday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        // Null values are considered valid and will be handled by @NotNull or similar annotations.
        if (localDate == null)
            return true;
        LocalDate today = LocalDate.now();
        return localDate.isBefore(today);
    }
}
