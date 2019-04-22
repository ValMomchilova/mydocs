package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.DocumentType;

public interface DocumentTypeValidationService {
    boolean isValid(DocumentType documentType);
}
