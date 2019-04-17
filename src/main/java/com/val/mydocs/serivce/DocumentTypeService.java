package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;

import java.util.List;

public interface DocumentTypeService {
    DocumentTypeServiceModel addDocumentType(DocumentTypeServiceModel documentTypeServiceModel);

    List<DocumentTypeServiceModel> findAllDocumentTypes();

    DocumentTypeServiceModel findDocumentTypesById(String id);

    DocumentTypeServiceModel editDocumentType(DocumentTypeServiceModel documentTypeServiceModel);

    void deleteDocumentType(String id);
}
