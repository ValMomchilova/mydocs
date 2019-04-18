package com.val.mydocs.domain.models.service;
import com.val.mydocs.domain.entities.SubjectType;

public class SubjectServiceModel {
    private String id;
    private SubjectTypeServiceModel subjectType;
    private String name;
    private String description;
    private UserServiceModel user;

    public SubjectServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubjectTypeServiceModel getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectTypeServiceModel subjectType) {
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

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }
}
