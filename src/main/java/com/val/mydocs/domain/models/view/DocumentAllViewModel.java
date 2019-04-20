package com.val.mydocs.domain.models.view;

import java.time.LocalDate;
import java.time.LocalTime;

public class DocumentAllViewModel {
    private String id;
    private SubjectDetailsViewModel subject;
    private DocumentTypeDetailsViewModel documentType;
    private String title;
    private LocalDate expiredDate;

    public DocumentAllViewModel() {
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

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }
}
