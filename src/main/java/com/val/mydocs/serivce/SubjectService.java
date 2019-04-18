package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.SubjectServiceModel;

import java.util.List;

public interface SubjectService {
    SubjectServiceModel addSubject(SubjectServiceModel subjectServiceModel, String userName);

    List<SubjectServiceModel> findAllSubjects();

    SubjectServiceModel findSubjectsById(String id);

    SubjectServiceModel editSubject(SubjectServiceModel subjectServiceModel);

    void deleteSubject(String id);

    List<SubjectServiceModel> findAllSubjects(String username);

    SubjectServiceModel findSubjectsById(String id, String username);

    void deleteSubject(String id, String username);

    SubjectServiceModel editSubject(SubjectServiceModel subjectServiceModel, String username);
}
