package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.exceptions.UniqueFieldException;

import java.util.List;

public interface SubjectService {
    SubjectServiceModel addSubject(SubjectServiceModel subjectServiceModel, String userName) throws UniqueFieldException;

    List<SubjectServiceModel> findAllSubjects(String username);

    List<SubjectServiceModel> findAllSubjectsBySubjectTypeOrder(String username);

    SubjectServiceModel findSubjectsById(String id, String username);

    void deleteSubject(String id, String username);

    SubjectServiceModel editSubject(SubjectServiceModel subjectServiceModel, String username) throws UniqueFieldException;
}
