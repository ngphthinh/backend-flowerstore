package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateAfterTodayValidator implements ConstraintValidator<ValidDateAfterToday, LocalDateTime> {

    @Override
    public void initialize(ValidDateAfterToday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {

        if (localDateTime == null) return true;
        LocalDateTime today = LocalDateTime.now();
        return localDateTime.isBefore(today);
    }
}
