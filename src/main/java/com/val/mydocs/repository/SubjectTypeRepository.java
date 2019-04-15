package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, String> {
}
