package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.SubjectTypeServiceModel;

import java.util.List;

public interface SubjectTypeService {
    SubjectTypeServiceModel saveSubjectType(SubjectTypeServiceModel subjectTypeServiceModel);

    List<SubjectTypeServiceModel> findAllSubjectTypes();

    SubjectTypeServiceModel findSubjectTypesById(String id);
}
