package com.val.mydocs.validation;

import com.val.mydocs.domain.entities.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentValidationServiceImpl implements DocumentValidationService {
    public static final int DOCUMENT_MAX_VALUE = 30;

    @Override
    public boolean isValid(Document documentType) {
        if (documentType == null){
            return false;
        }
        if(documentType.getTitle() == null
                || documentType.getTitle().isEmpty()
                || documentType.getTitle().length() > DOCUMENT_MAX_VALUE){
            return false;
        }
        return true;
    }
}
