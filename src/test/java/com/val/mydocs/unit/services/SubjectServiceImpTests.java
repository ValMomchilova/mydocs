package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.SubjectRepository;
import com.val.mydocs.serivce.SubjectService;
import com.val.mydocs.serivce.SubjectServiceImpl;
import com.val.mydocs.serivce.UserService;
import com.val.mydocs.validation.SubjectValidationService;
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
public class SubjectServiceImpTests {

    public static final String TEST_USER_NAME = "test user";
    public static final String TEST_NAME = "test name";
    public static final String TEST_ID = "testId";
    @Mock
    private SubjectRepository mockRepository;

    @Mock
    private SubjectValidationService mockValidationService;

    @Mock
    private UserService mockUserService;

    private SubjectService service;

    @Before()
    public void init(){
        this.service = new SubjectServiceImpl(this.mockRepository,
                this.mockUserService,
                new ModelMapper(),
                this.mockValidationService);

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USER_NAME);
        when(this.mockUserService.findUserByUserName(TEST_USER_NAME))
                .thenReturn(userServiceModel);

        when(this.mockRepository.findSubjectByIdAndAndUser(any(), any()))
                .thenReturn(new Subject());
    }

    @Test
    public void addSubjectTestSaveValidAndUniqueSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.findSubjectByName(any()))
                .thenReturn(null);

        when(mockRepository.save(any()))
                .thenReturn(new Subject());

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName(TEST_NAME);
        this.service.addSubject(SubjectServiceModel, TEST_USER_NAME);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void addSubjectTestDoNotSaveNotValidSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.FALSE);

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        this.service.addSubject(SubjectServiceModel, TEST_USER_NAME);
    }

    @Test(expected = UniqueFieldException.class)
    public void addSubjectTestDoNotSaveNotUniqueSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.TRUE);

        Subject Subject = new Subject();
        Subject.setId("test");
        when(mockRepository.findSubjectByName(any()))
                .thenReturn(Subject);

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName("TEST_NAME");
        this.service.addSubject(SubjectServiceModel, TEST_USER_NAME);
    }

    @Test
    public void editSubjectTestSaveValidAndUniqueSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.findSubjectByName(any()))
                .thenReturn(null);

        when(mockRepository.save(any()))
                .thenReturn(new Subject());

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName("TEST_NAME");
        SubjectServiceModel.setSubjectType(new SubjectTypeServiceModel());
        this.service.editSubject(SubjectServiceModel, TEST_USER_NAME);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void editSubjectTestDoNotSaveNotValidSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.FALSE);

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName("TEST_NAME");
        SubjectServiceModel.setSubjectType(new SubjectTypeServiceModel());
        this.service.editSubject(SubjectServiceModel, TEST_USER_NAME);
    }

    @Test(expected = UniqueFieldException.class)
    public void editSubjectTestDoNotSaveNotUniqueSubject() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(Subject.class)))
                .thenReturn(Boolean.TRUE);

        Subject Subject = new Subject();
        Subject.setId("test");
        when(mockRepository.findSubjectByName(any()))
                .thenReturn(Subject);

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName("TEST_NAME");
        SubjectServiceModel.setSubjectType(new SubjectTypeServiceModel());
        SubjectServiceModel.setName(TEST_NAME);
        this.service.editSubject(SubjectServiceModel, TEST_USER_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void editSubjectThrowsInCaseSubjectNotFoundOrNotBelongsToTheUser() throws UniqueFieldException, ModelValidationException {
        Subject Subject = new Subject();
        Subject.setId("test");
        when(mockRepository.findSubjectByIdAndAndUser(any(), any()))
                .thenReturn(null);

        SubjectServiceModel SubjectServiceModel = new SubjectServiceModel();
        SubjectServiceModel.setName("TEST_NAME");
        SubjectServiceModel.setSubjectType(new SubjectTypeServiceModel());
        SubjectServiceModel.setName(TEST_NAME);
        this.service.editSubject(SubjectServiceModel, TEST_USER_NAME);
    }

    @Test
    public void deleteSubjectDeletesSubject(){

        this.service.deleteSubject(TEST_ID, TEST_USER_NAME);

        verify(mockRepository)
                .deleteById(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteSubjectThrowsInCaseSubjectNotFoundOrNotBelongsToTheUser(){
        when(mockRepository.findSubjectByIdAndAndUser(any(), any()))
                .thenReturn(null);

        this.service.deleteSubject(TEST_ID, TEST_USER_NAME);
    }

    @Test
    public void findAllSubjectFindsAll(){

        this.service.findAllSubjects(TEST_USER_NAME);

        verify(mockRepository)
                .findSubjectsByUser(any());
    }

    @Test
    public void findSubjectsByIdFindsById(){

        this.service.findSubjectsById(TEST_ID, TEST_USER_NAME);

        verify(mockRepository)
                .findSubjectByIdAndAndUser(any(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findSubjectsByIdThrowsInCaseSubjectNotFoundOrNotBelongsToTheUser(){
        when(mockRepository.findSubjectByIdAndAndUser(any(), any()))
                .thenReturn(null);

        this.service.findSubjectsById(TEST_ID, TEST_USER_NAME);
    }

    @Test
    public void findAllSubjectsBySubjectTypeOrderFindsServices(){
        this.service.findAllSubjectsBySubjectTypeOrder(TEST_USER_NAME);

        verify(mockRepository)
                .findSubjectsByUser(any());
    }
}
