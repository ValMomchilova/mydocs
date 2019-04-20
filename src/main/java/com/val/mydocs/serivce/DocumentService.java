package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.DocumentServiceModel;

import java.util.Collection;
import java.util.List;

public interface DocumentService {
    DocumentServiceModel addDocument(DocumentServiceModel subjectServiceModel, String userName);

    List<DocumentServiceModel> findAllDocumentsOrderByExpiredDate(String username);

    DocumentServiceModel findDocumentsById(String id, String username);

    void deleteDocument(String id, String username);

    DocumentServiceModel editDocument(DocumentServiceModel subjectServiceModel, String username);

    List<Object> findAllBySubject(String subjectId, String username);

    DocumentServiceModel renewDocument(DocumentServiceModel documentServiceModel, String userName);
}
