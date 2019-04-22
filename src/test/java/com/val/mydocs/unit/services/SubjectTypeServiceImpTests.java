package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.SubjectType;
import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.SubjectTypeRepository;
import com.val.mydocs.serivce.SubjectTypeService;
import com.val.mydocs.serivce.SubjectTypeServiceImpl;

import com.val.mydocs.validation.SubjectTypeValidationService;
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
public class SubjectTypeServiceImpTests {

    @Mock
    private SubjectTypeRepository mockRepository;

    @Mock
    private SubjectTypeValidationService mockValidationService;

    private SubjectTypeService service;

    @Before()
    public void init(){
        this.service = new SubjectTypeServiceImpl(this.mockRepository,
                new ModelMapper(),
                this.mockValidationService);
    }

    @Test
    public void addSubjectTypeTestSaveValidAndUniqueSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.findSubjectTypeByTitle(any()))
                .thenReturn(null);

        when(mockRepository.save(any()))
                .thenReturn(new SubjectType());

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        SubjectTypeServiceModel.setTitle("test title");
        this.service.addSubjectType(SubjectTypeServiceModel);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void addSubjectTypeTestDoNotSaveNotValidSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.FALSE);

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        this.service.addSubjectType(SubjectTypeServiceModel);
    }

    @Test(expected = UniqueFieldException.class)
    public void addSubjectTypeTestDoNotSaveNotUniqueSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.TRUE);

        SubjectType SubjectType = new SubjectType();
        SubjectType.setId("test");
        when(mockRepository.findSubjectTypeByTitle(any()))
                .thenReturn(SubjectType);

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        SubjectTypeServiceModel.setTitle("test title");
        this.service.addSubjectType(SubjectTypeServiceModel);
    }

    @Test
    public void editSubjectTypeTestSaveValidAndUniqueSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.findSubjectTypeByTitle(any()))
                .thenReturn(null);

        when(mockRepository.save(any()))
                .thenReturn(new SubjectType());

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        SubjectTypeServiceModel.setTitle("test title");
        this.service.editSubjectType(SubjectTypeServiceModel);

        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void editSubjectTypeTestDoNotSaveNotValidSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.FALSE);

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        this.service.editSubjectType(SubjectTypeServiceModel);
    }

    @Test(expected = UniqueFieldException.class)
    public void editSubjectTypeTestDoNotSaveNotUniqueSubjectType() throws UniqueFieldException, ModelValidationException {
        when(mockValidationService.isValid(any(SubjectType.class)))
                .thenReturn(Boolean.TRUE);

        SubjectType SubjectType = new SubjectType();
        SubjectType.setId("test");
        when(mockRepository.findSubjectTypeByTitle(any()))
                .thenReturn(SubjectType);

        SubjectTypeServiceModel SubjectTypeServiceModel = new SubjectTypeServiceModel();
        SubjectTypeServiceModel.setTitle("test title");
        this.service.editSubjectType(SubjectTypeServiceModel);
    }

    @Test
    public void deleteSubjectTypeDeletesSubjectType(){

        this.service.deleteSubjectType("testId");

        verify(mockRepository)
                .deleteById(any());
    }

    @Test
    public void findAllSubjectTypeFindsAll(){

        this.service.findAllSubjectTypes();

        verify(mockRepository)
                .findAll();
    }

    @Test
    public void findSubjectTypesByIdFindsById(){

        this.service.findSubjectTypesById("test");

        verify(mockRepository)
                .findById("test");
    }
}
