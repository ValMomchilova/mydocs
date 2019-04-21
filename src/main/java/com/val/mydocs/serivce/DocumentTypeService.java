package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.exceptions.UniqueFieldException;

import java.util.List;

public interface DocumentTypeService {
    DocumentTypeServiceModel addDocumentType(DocumentTypeServiceModel documentTypeServiceModel) throws UniqueFieldException;

    List<DocumentTypeServiceModel> findAllDocumentTypes();

    DocumentTypeServiceModel findDocumentTypesById(String id);

    DocumentTypeServiceModel editDocumentType(DocumentTypeServiceModel documentTypeServiceModel) throws UniqueFieldException;

    void deleteDocumentType(String id);
}
