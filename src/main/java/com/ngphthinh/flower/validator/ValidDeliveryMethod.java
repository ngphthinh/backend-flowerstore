package com.ngphthinh.flower.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DeliveryMethodValidator.class})
public @interface ValidDeliveryMethod {
    String message() default "Invalid delivery method";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
