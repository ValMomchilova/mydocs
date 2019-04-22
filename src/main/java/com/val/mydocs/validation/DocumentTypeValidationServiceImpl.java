package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.DocumentType;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeValidationServiceImpl implements DocumentTypeValidationService {
    public static final int DOCUMENT_TYPE_MAX_VALUE = 30;

    @Override
    public boolean isValid(DocumentType documentType) {
        if (documentType == null){
            return false;
        }
        if(documentType.getTitle() == null
                || documentType.getTitle().isEmpty()
                || documentType.getTitle().length() > DOCUMENT_TYPE_MAX_VALUE){
            return false;
        }
        return true;
    }
}
