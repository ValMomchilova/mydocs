package com.val.mydocs.web.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class FromToDateFieldsSequeceValidator implements ConstraintValidator<FromToDateFieldsSequece, Object> {

    private static final Logger log = LoggerFactory.getLogger(FromToDateFieldsSequeceValidator.class);

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FromToDateFieldsSequece constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstFieldName();
        secondFieldName = constraintAnnotation.secondFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        try {
            Class<?> clazz = value.getClass();

            Field firstField = ReflectionUtils.findField(clazz, firstFieldName);
            firstField.setAccessible(true);
            Object first = firstField.get(value);

            Field secondField = ReflectionUtils.findField(clazz, secondFieldName);
            secondField.setAccessible(true);
            Object second = secondField.get(value);

            if (first == null && second == null){
                return true;
            }

            LocalDate fromDate = (LocalDate)first;
            LocalDate toDate = (LocalDate)second;

            if (fromDate.equals(toDate)) {
                return false;
            }

            if (fromDate.isAfter(toDate)) {
                return false;
            }

        } catch (Exception e) {
            log.error("Cannot validate dates sequence!");
            return false;
        }

        return true;
    }
}
