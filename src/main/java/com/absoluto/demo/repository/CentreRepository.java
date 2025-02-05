package com.absoluto.demo.repository;
import com.absoluto.demo.centre.Centre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentreRepository extends JpaRepository<Centre, Long> {
    
}