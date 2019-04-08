package com.val.mydocs.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "documents")
public class Document extends BaseEntity {
    private Subject subject;
    private DocumentType documentType;
    private String title;
    private LocalDate date;
    private LocalDate expiredDate;
    private LocalTime expiredTime;
    private String description;
    private LocalDate renewDate;

    public Document() {
    }

    @ManyToOne(targetEntity = Subject.class)
    @JoinColumn(
            name = "subject_id",
            referencedColumnName = "id"
    )
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @ManyToOne(targetEntity = DocumentType.class)
    @JoinColumn(
            name = "document_type_id",
            referencedColumnName = "id"
    )
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(name = "expired_date")
    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Column(name = "expired_time")
    public LocalTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "renew_date")
    public LocalDate getRenewDate() {
        return renewDate;
    }

    public void setRenewDate(LocalDate renewDate) {
        this.renewDate = renewDate;
    }
}
