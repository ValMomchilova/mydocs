package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findDocumentsByUser(User user);

    List<Document> findDocumentsByUserAndAndSubject(User user, Subject subject);

    Document findDocumentByIdAndAndUser(String id, User user);
}