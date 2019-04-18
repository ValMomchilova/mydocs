package com.val.mydocs.domain.models.view;

import com.val.mydocs.domain.entities.SubjectType;

public class SubjectDetailsViewModel {
    private String id;
    private SubjectTypeDetailsViewModel subjectType;
    private String name;
    private String description;

    public SubjectDetailsViewModel() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
