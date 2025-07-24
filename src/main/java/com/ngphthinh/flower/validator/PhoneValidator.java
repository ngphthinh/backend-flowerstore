package com.ngphthinh.flower.validator;

import jakarta.validation.ConstraintValidator;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String phone, jakarta.validation.ConstraintValidatorContext context) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Allow empty or null values
        }
        // Regex to validate phone numbers in the format +84xxxxxxxxx or 0xxxxxxxxx
        return phone.matches("^(\\+84|0)\\d{9}$");
    }
}
