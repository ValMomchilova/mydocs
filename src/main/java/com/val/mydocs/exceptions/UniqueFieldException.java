package com.val.mydocs.exceptions;

public class UniqueFieldException extends Exception {
    private String entityName;
    private String fieldName;

    public UniqueFieldException(String entityName, String fieldName, String message) {
        super(message);
        this.entityName = entityName;
        this.fieldName = fieldName;
    }

    public UniqueFieldException(String entityName, String fieldName) {
        this.entityName = entityName;
        this.fieldName = fieldName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
