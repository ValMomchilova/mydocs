package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.Document;

public interface DocumentValidationService {
    boolean isValid(Document documentType);
}
