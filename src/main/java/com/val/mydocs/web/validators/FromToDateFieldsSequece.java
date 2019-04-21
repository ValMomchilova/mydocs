package com.val.mydocs.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { FromToDateFieldsSequeceValidator.class })
public @interface FromToDateFieldsSequece {

    String message() default "{dates.sequence.not.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Name of the first field that will be compared.
     *
     @return name
     */
    String firstFieldName();

    /**
     * Name of the second field that will be compared.
     *
     @return name
     */
    String secondFieldName();

    @Target({ TYPE, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @interface List {
        FromToDateFieldsSequece[] value();
    }
}

