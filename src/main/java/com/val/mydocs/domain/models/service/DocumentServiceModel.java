package com.val.mydocs.domain.models.service;

import java.time.LocalDate;

public class DocumentServiceModel {
    private String id;
    private SubjectServiceModel subject;
    private DocumentTypeServiceModel documentType;
    private String title;
    private LocalDate date;
    private LocalDate expiredDate;
    private String description;
    private LocalDate renewDate;
    private UserServiceModel user;
    private Boolean autoRenew;

    public DocumentServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubjectServiceModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectServiceModel subject) {
        this.subject = subject;
    }

    public DocumentTypeServiceModel getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeServiceModel documentType) {
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

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
