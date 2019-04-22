package com.val.mydocs.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {
    private SubjectType subjectType;
    private String name;
    private String description;
    private User user;

    public Subject() {
    }

    @ManyToOne(targetEntity = SubjectType.class)
    @JoinColumn(
            name = "subject_type_id",
            referencedColumnName = "id"
    )
    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Column(name = "name", nullable = false, unique = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
