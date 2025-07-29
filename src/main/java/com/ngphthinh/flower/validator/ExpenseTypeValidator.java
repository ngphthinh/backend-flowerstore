package com.ngphthinh.flower.validator;

import com.ngphthinh.flower.enums.ExpenseType;
import jakarta.validation.ConstraintValidator;

public class ExpenseTypeValidator implements ConstraintValidator<ValidExpenseType, String> {

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        String deliveryMethodUpperCase = value != null ? value.toUpperCase() : null;
        return value != null && ExpenseType.isValid(deliveryMethodUpperCase);
    }
}
