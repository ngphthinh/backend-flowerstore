package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateTimeBeforeTodayValidator implements ConstraintValidator<ValidDateBeforeToday, LocalDateTime> {

    @Override
    public void initialize(ValidDateBeforeToday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        // Null values are considered valid and will be handled by @NotNull or similar annotations.
        if (localDateTime == null)
            return true;
        LocalDateTime today = LocalDateTime.now();
        return localDateTime.isBefore(today);
    }
}
