package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String name);

    User findUserByEmail(String email);
}
