package com.ngphthinh.flower.validator;

import com.ngphthinh.flower.enums.DeliveryMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class DeliveryMethodValidator implements ConstraintValidator<ValidDeliveryMethod, String> {


    @Override
    public void initialize(ValidDeliveryMethod constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String deliveryMethod, ConstraintValidatorContext constraintValidatorContext) {
        String deliveryMethodUpperCase = deliveryMethod != null ? deliveryMethod.toUpperCase() : null;
        return deliveryMethod != null && DeliveryMethod.isValid(deliveryMethodUpperCase);
    }
}
