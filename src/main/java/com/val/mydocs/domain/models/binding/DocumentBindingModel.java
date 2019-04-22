package com.val.mydocs.domain.models.binding;

import com.val.mydocs.web.validators.FromToDateFieldsSequece;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@FromToDateFieldsSequece(firstFieldName = "date", secondFieldName = "expiredDate")
public class DocumentBindingModel {
    private String id;
    private SubjectBindingModel subject;
    private DocumentTypeBindingModel documentType;
    private String title;
    private LocalDate date;
    private LocalDate expiredDate;
    private String description;
    private LocalDate renewDate;
    private Boolean autoRenew;

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

    @NotNull
    public DocumentTypeBindingModel getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeBindingModel documentType) {
        this.documentType = documentType;
    }

    @NotEmpty
    @NotNull
    @Length(max = 30)
    @SafeHtml()
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull(message = "{error.must.not.be.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @NotNull(message = "{error.must.not.be.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    @SafeHtml()
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

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
