package com.val.mydocs.domain.models.binding;

import java.time.LocalDate;
import java.time.LocalTime;

public class DocumentBindingModel {
    private String id;
    private SubjectBindingModel subject;
    private DocumentTypeBindingModel documentType;
    private String title;
    private LocalDate date;
    private LocalDate expiredDate;
    private LocalTime expiredTime;
    private String description;
    private LocalDate renewDate;

    public DocumentBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubjectBindingModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectBindingModel subject) {
        this.subject = subject;
    }

    public DocumentTypeBindingModel getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeBindingModel documentType) {
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    public LocalTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(LocalDate renewDate) {
        this.renewDate = renewDate;
    }
}
