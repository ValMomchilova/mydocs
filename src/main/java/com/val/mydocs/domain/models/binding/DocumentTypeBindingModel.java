package com.val.mydocs.domain.models.binding;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DocumentTypeBindingModel {
    private String id;
    private String title;
    private String description;

    public DocumentTypeBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    @NotEmpty
    @SafeHtml()
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SafeHtml()
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
