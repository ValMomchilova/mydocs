package com.val.mydocs.domain.models.view;

import java.time.LocalDate;
import java.time.LocalTime;

public class DocumentDetailsViewModel {
    private String id;
    private SubjectDetailsViewModel subject;
    private DocumentTypeDetailsViewModel documentType;
    private String title;
    private LocalDate date;
    private LocalDate expiredDate;
    private String description;
    private LocalDate renewDate;

    public DocumentDetailsViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubjectDetailsViewModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectDetailsViewModel subject) {
        this.subject = subject;
    }

    public DocumentTypeDetailsViewModel getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeDetailsViewModel documentType) {
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
}
