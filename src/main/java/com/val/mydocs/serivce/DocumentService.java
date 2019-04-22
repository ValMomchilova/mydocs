package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.DocumentServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;

import java.util.Collection;
import java.util.List;

public interface DocumentService {
    DocumentServiceModel addDocument(DocumentServiceModel subjectServiceModel, String userName) throws ModelValidationException;

    List<DocumentServiceModel> findAllDocumentsOrderByExpiredDate(String username);

    DocumentServiceModel findDocumentsById(String id, String username);

    void deleteDocument(String id, String username);

    DocumentServiceModel editDocument(DocumentServiceModel subjectServiceModel, String username) throws ModelValidationException;

    List<Object> findAllBySubjectOrderByExpiredDate(String subjectId, String username);

    DocumentServiceModel renewDocument(DocumentServiceModel documentServiceModel, String userName) throws ModelValidationException;

    void AutoRenewDocuments() throws ModelValidationException;
}
