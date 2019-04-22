package com.val.mydocs.exceptions;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ModelValidationException extends Exception {
    private static final String DEFAULT_MESSAGE = "%s model is not valid: %s";
    
    private String entityName;

    public ModelValidationException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }

    public ModelValidationException(String entityName, Object model) {
        this(entityName, String.format(DEFAULT_MESSAGE, entityName,
                ReflectionToStringBuilder.toString(model, ToStringStyle.SHORT_PREFIX_STYLE)));
    }

    public String getEntityName() {
        return entityName;
    }
}
