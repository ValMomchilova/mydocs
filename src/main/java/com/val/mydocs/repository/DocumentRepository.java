package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.Document;
import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    List<Document> findDocumentsByUserOrderByExpiredDate(User user);

    List<Document> findDocumentsByUserAndAndSubjectOrderByExpiredDate(User user, Subject subject);

    List<Document> findDocumentByRenewDateAndAutoRenewAndExpiredDateIsBefore(LocalDate renewDate,
                                                                             Boolean autoRenew,
                                                                             LocalDate expiryDate);

    Document findDocumentByIdAndAndUser(String id, User user);
}
