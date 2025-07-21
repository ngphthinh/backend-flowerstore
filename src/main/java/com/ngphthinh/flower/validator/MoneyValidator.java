package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.xml.stream.XMLInputFactory;
import java.math.BigDecimal;


public class MoneyValidator implements ConstraintValidator<ValidMoney, BigDecimal> {

    private BigDecimal min;

    @Override
    public void initialize(ValidMoney constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = BigDecimal.valueOf(constraintAnnotation.min());
    }

    @Override
    public boolean isValid(BigDecimal money, ConstraintValidatorContext constraintValidatorContext) {
        return money != null && money.compareTo(min) > 0;
    }

}
