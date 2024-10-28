package org.example.taskmanagementapi.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.taskmanagementapi.annotations.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum,Enum<?>> {
//    private Class<? extends Enum<?>> enumClass;
    private ValidEnum anEnum;
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.anEnum = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum<?>  value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;

        Object[] enumConstants = anEnum.enumClass().getEnumConstants();
        for(Object enumConstant : enumConstants) {
            if (enumConstant.toString().equals(value.toString())) return true;
        }

        return false;
    }
}
