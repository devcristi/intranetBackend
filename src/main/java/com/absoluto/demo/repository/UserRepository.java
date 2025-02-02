package com.absoluto.demo.repository;

import com.absoluto.demo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Metodă pentru a găsi un utilizator după email
    Optional<User> findByEmail(String email);

    // Verificare existența unui utilizator după email
    boolean existsByEmail(String email);

    long count();
}