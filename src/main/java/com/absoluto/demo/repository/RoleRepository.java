package com.absoluto.demo.repository;

import com.absoluto.demo.role.Role;
import com.absoluto.demo.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
