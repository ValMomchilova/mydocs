package com.val.mydocs.domain.models.binding;

import com.val.mydocs.domain.entities.SubjectType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SubjectBindingModel {
    private String id;
    private SubjectTypeBindingModel subjectType;
    private String name;
    private String description;

    public SubjectBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public SubjectTypeBindingModel getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectTypeBindingModel subjectType) {
        this.subjectType = subjectType;
    }

    @NotNull
    @NotEmpty
    @Length(max = 30)
    @SafeHtml()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SafeHtml()
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
