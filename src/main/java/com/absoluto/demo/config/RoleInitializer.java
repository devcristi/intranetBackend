package com.absoluto.demo.config;

import com.absoluto.demo.role.Role;
import com.absoluto.demo.role.RoleType;
import com.absoluto.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleType);
                return roleRepository.save(role);
            });
        }
    }
}
