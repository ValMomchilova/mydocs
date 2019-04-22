package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.DocumentTypeRepository;
import com.val.mydocs.serivce.DocumentTypeService;
import com.val.mydocs.serivce.DocumentTypeServiceImpl;
import com.val.mydocs.validation.DocumentTypeValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeServiceImpTests {

    @Mock
    private DocumentTypeRepository mockDocumentTypeRepository;

    @Mock
    private DocumentTypeValidationService mockValidationService;

    private DocumentTypeService documentTypeService;

    @Before()
    public void init(){
        this.documentTypeService = new DocumentTypeServiceImpl(this.mockDocumentTypeRepository,
                new ModelMapper(),
                this.mockValidationService);
    }

    @Test
    public void addDocumentTypeTestSaveValidAndUniqueDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.TRUE);

        when(mockDocumentTypeRepository.findDocumentTypeByTitle(any()))
                .thenReturn(null);

        when(mockDocumentTypeRepository.save(any()))
                .thenReturn(new DocumentType());

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeServiceModel.setTitle("test title");
        documentTypeService.addDocumentType(documentTypeServiceModel);

        verify(mockDocumentTypeRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void addDocumentTypeTestDoNotSaveNotValidDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.FALSE);

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeService.addDocumentType(documentTypeServiceModel);
    }

    @Test(expected = UniqueFieldException.class)
    public void addDocumentTypeTestDoNotSaveNotUniqueDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.TRUE);

        DocumentType documentType = new DocumentType();
        documentType.setId("test");
        when(mockDocumentTypeRepository.findDocumentTypeByTitle(any()))
                .thenReturn(documentType);

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeServiceModel.setTitle("test title");
        documentTypeService.addDocumentType(documentTypeServiceModel);
    }

    @Test
    public void editDocumentTypeTestSaveValidAndUniqueDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.TRUE);

        when(mockDocumentTypeRepository.findDocumentTypeByTitle(any()))
                .thenReturn(null);

        when(mockDocumentTypeRepository.save(any()))
                .thenReturn(new DocumentType());

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeServiceModel.setTitle("test title");
        documentTypeService.editDocumentType(documentTypeServiceModel);

        verify(mockDocumentTypeRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void editDocumentTypeTestDoNotSaveNotValidDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.FALSE);

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeService.editDocumentType(documentTypeServiceModel);
    }

    @Test(expected = UniqueFieldException.class)
    public void editDocumentTypeTestDoNotSaveNotUniqueDocumentType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(DocumentType.class)))
                .thenReturn(Boolean.TRUE);

        DocumentType documentType = new DocumentType();
        documentType.setId("test");
        when(mockDocumentTypeRepository.findDocumentTypeByTitle(any()))
                .thenReturn(documentType);

        DocumentTypeServiceModel documentTypeServiceModel = new DocumentTypeServiceModel();
        documentTypeServiceModel.setTitle("test title");
        documentTypeService.editDocumentType(documentTypeServiceModel);
    }

    @Test
    public void deleteDocumentTypeDeletesDocumentType(){

        this.documentTypeService.deleteDocumentType("testId");

        verify(mockDocumentTypeRepository)
                .deleteById(any());
    }

    @Test
    public void findAllDocumentTypeFindsAll(){

        this.documentTypeService.findAllDocumentTypes();

        verify(mockDocumentTypeRepository)
                .findAll();
    }

    @Test
    public void findDocumentTypesByIdFindsById(){

        this.documentTypeService.findDocumentTypesById("test");

        verify(mockDocumentTypeRepository)
                .findById("test");
    }
}
