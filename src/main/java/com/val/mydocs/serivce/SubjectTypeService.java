package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;

import java.util.List;

public interface SubjectTypeService {
    SubjectTypeServiceModel addSubjectType(SubjectTypeServiceModel subjectTypeServiceModel);

    List<SubjectTypeServiceModel> findAllSubjectTypes();

    SubjectTypeServiceModel findSubjectTypesById(String id);

    SubjectTypeServiceModel editSubjectType (SubjectTypeServiceModel subjectTypeServiceModel);

    void deleteSubjectType(String id);
}
