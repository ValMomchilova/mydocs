package com.val.mydocs.domain.models.view;

import com.val.mydocs.domain.entities.SubjectType;

public class SubjectAllViewModel {
    private String id;
    private SubjectTypeDetailsViewModel subjectType;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubjectTypeDetailsViewModel getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectTypeDetailsViewModel subjectType) {
        this.subjectType = subjectType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectAllViewModel() {

    }
}
