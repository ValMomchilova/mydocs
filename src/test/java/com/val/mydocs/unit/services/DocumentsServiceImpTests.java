package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.domain.entities.DocumentType;
import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.models.service.DocumentServiceModel;
import com.val.mydocs.domain.models.service.DocumentTypeServiceModel;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.DocumentRepository;
import com.val.mydocs.serivce.*;
import com.val.mydocs.validation.DocumentValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class DocumentsServiceImpTests {

    public static final String TEST_USER_NAME = "test user";
    public static final String TEST_NAME = "test name";
    public static final String TEST_ID = "testId";
    @Mock
    private DocumentRepository mockRepository;

    @Mock
    private DocumentValidationService mockValidationService;

    @Mock
    private UserService mockUserService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private DateTimeService dateTimeService;

    private DocumentService service;

    @Before()
    public void init(){
        this.service = new DocumentServiceImpl(this.mockRepository,
                this.mockUserService,
                this.subjectService,
                new ModelMapper(),
                this.dateTimeService,
                this.mockValidationService);

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USER_NAME);
        when(this.mockUserService.findUserByUserName(any()))
                .thenReturn(userServiceModel);

        when(this.mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(new Document());
    }

    @Test
    public void addDocumentTestSaveValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.save(any()))
                .thenReturn(new Document());

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle(TEST_NAME);
        this.service.addDocument(DocumentServiceModel, TEST_USER_NAME);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void addDocumentTestDoNotSaveNotValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.FALSE);

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        this.service.addDocument(DocumentServiceModel, TEST_USER_NAME);
    }

    @Test
    public void editDocumentTestSaveValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.save(any()))
                .thenReturn(new Document());

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle("TEST_NAME");
        DocumentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        this.service.editDocument(DocumentServiceModel, TEST_USER_NAME);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void editDocumentTestDoNotSaveNotValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.FALSE);

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle("TEST_NAME");
        DocumentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        this.service.editDocument(DocumentServiceModel, TEST_USER_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editDocumentThrowsInCaseDocumentNotFoundOrNotBelongsToTheUser() throws UniqueFieldException, ModelValidationException {
        Document Document = new Document();
        Document.setId("test");
        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(null);

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle("TEST_NAME");
        DocumentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        DocumentServiceModel.setTitle(TEST_NAME);
        this.service.editDocument(DocumentServiceModel, TEST_USER_NAME);
    }

    @Test
    public void deleteDocumentDeletesDocument(){

        this.service.deleteDocument(TEST_ID, TEST_USER_NAME);

        verify(mockRepository)
                .deleteById(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteDocumentThrowsInCaseDocumentNotFoundOrNotBelongsToTheUser(){
        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(null);

        this.service.deleteDocument(TEST_ID, TEST_USER_NAME);
    }

    @Test
    public void findAllDocumentsOrderByExpiredDateFindsAll(){

        this.service.findAllDocumentsOrderByExpiredDate(TEST_USER_NAME);

        verify(mockRepository)
                .findDocumentsByUserOrderByExpiredDate(any());
    }

    @Test
    public void findAllBySubjectOrderByExpiredDateFindsAllBySubjectId(){
        when(subjectService.findSubjectsById(any(), any()))
                .thenReturn(new SubjectServiceModel());

        this.service.findAllBySubjectOrderByExpiredDate("subjectId", TEST_USER_NAME);

        verify(mockRepository)
                .findDocumentsByUserAndAndSubjectOrderByExpiredDate(any(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllBySubjectOrderByExpiredDateThrowsIfSubjectNotBelongsToTheUser(){
        when(subjectService.findSubjectsById(any(), any()))
                .thenReturn(null);

        this.service.findAllBySubjectOrderByExpiredDate("subjectId", TEST_USER_NAME);
    }

    @Test
    public void findDocumentsByIdFindsById(){

        this.service.findDocumentsById(TEST_ID, TEST_USER_NAME);

        verify(mockRepository)
                .findDocumentByIdAndAndUser(any(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findDocumentsByIdThrowsInCaseDocumentNotFoundOrNotBelongsToTheUser(){
        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(null);

        this.service.findDocumentsById(TEST_ID, TEST_USER_NAME);
    }

    @Test
    public void renewDocumentTestRenewValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.save(any()))
                .thenReturn(new Document());

        String description = "description";
        String title = "document";
        LocalDate date = LocalDate.of(2018, 4, 1);
        LocalDate expiredDate = LocalDate.of(2019, 4, 1);

        Document document = new Document();
        document.setTitle("TEST_NAME");
        document.setDocumentType(new DocumentType());
        document.setDescription(description);
        document.setTitle(title);
        document.setDate(date);
        document.setExpiredDate(expiredDate);
        document.setSubject(new Subject());
        document.setUser(new User());

        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(document);

        DocumentServiceModel documentServiceModel = new DocumentServiceModel();
        documentServiceModel.setTitle("TEST_NAME");
        documentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        documentServiceModel.setDescription(description);
        documentServiceModel.setTitle(title);
        documentServiceModel.setDate(date);
        documentServiceModel.setExpiredDate(expiredDate);
        documentServiceModel.setSubject(new SubjectServiceModel());
        documentServiceModel.setUser(new UserServiceModel());

        this.service.renewDocument(documentServiceModel, TEST_USER_NAME);

        verify(mockRepository, times(2))
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void renewDocumentTestDoNotRenewNotValidDocument() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.FALSE);

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle("TEST_NAME");
        DocumentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        this.service.renewDocument(DocumentServiceModel, TEST_USER_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void renewDocumentThrowsInCaseDocumentNotFoundOrNotBelongsToTheUser() throws UniqueFieldException, ModelValidationException {
        Document Document = new Document();
        Document.setId("test");
        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(null);

        DocumentServiceModel DocumentServiceModel = new DocumentServiceModel();
        DocumentServiceModel.setTitle("TEST_NAME");
        DocumentServiceModel.setDocumentType(new DocumentTypeServiceModel());
        DocumentServiceModel.setTitle(TEST_NAME);
        this.service.renewDocument(DocumentServiceModel, TEST_USER_NAME);
    }

    @Test
    public void AutoRenewDocumentsRenewDocumentsIfAny() throws ModelValidationException {
        when(this.dateTimeService.getCurrentDate())
                .thenReturn(LocalDate.of(2019, 4, 2));

        when(mockValidationService.isValid(any(Document.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.save(any()))
                .thenReturn(new Document());

        String description = "description";
        String title = "document";
        LocalDate date = LocalDate.of(2018, 4, 1);
        LocalDate expiredDate = LocalDate.of(2019, 4, 1);

        List<Document> documentsList = new ArrayList();

        for (int i = 0; i < 3; i++) {
            Document document = new Document();
            document.setTitle("TEST_NAME" + i);
            document.setDocumentType(new DocumentType());
            document.setDescription(description);
            document.setTitle(title);
            document.setDate(date);
            document.setExpiredDate(expiredDate);
            document.setSubject(new Subject());
            document.setUser(new User());

            documentsList.add(document);
        }

        Document document = new Document();
        document.setTitle("TEST_NAME");
        document.setDocumentType(new DocumentType());
        document.setDescription(description);
        document.setTitle(title);
        document.setDate(date);
        document.setExpiredDate(expiredDate);
        document.setSubject(new Subject());
        document.setUser(new User());

        when(mockRepository.findDocumentByIdAndAndUser(any(), any()))
                .thenReturn(document);

        when(this.mockRepository.findDocumentByRenewDateAndAutoRenewAndExpiredDateIsBefore(any(), any(), any()))
                .thenReturn(documentsList);


        this.service.autoRenewDocuments();

        verify(mockRepository, times(2*3))
                .save(any());
    }
}
