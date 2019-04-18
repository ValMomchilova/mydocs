package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.Subject;
import com.val.mydocs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findSubjectsByUser(User user);
    Subject findSubjectByIdAndAndUser(String id, User user);
}
