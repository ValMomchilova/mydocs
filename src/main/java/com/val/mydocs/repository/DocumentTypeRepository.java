package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {
    public Optional<DocumentType> findDocumentTypeByTitle(String title);
}
