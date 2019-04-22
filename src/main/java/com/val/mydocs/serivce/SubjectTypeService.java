package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;

import java.util.List;

public interface SubjectTypeService {
    SubjectTypeServiceModel addSubjectType(SubjectTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException, ModelValidationException;

    List<SubjectTypeServiceModel> findAllSubjectTypes();

    SubjectTypeServiceModel findSubjectTypesById(String id);

    SubjectTypeServiceModel editSubjectType (SubjectTypeServiceModel subjectTypeServiceModel) throws UniqueFieldException, ModelValidationException;

    void deleteSubjectType(String id);
}
