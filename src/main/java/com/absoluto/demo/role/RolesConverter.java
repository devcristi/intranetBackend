package com.absoluto.demo.role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RolesConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_CHAR = ",";

    // Convertim Set-ul într-un String pentru a fi stocat în baza de date
    @Override
    public String convertToDatabaseColumn(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream().collect(Collectors.joining(SPLIT_CHAR));
    }

    // Convertim String-ul din baza de date în Set-ul de roluri
    @Override
    public Set<String> convertToEntityAttribute(String rolesString) {
        if (rolesString == null || rolesString.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(rolesString.split(SPLIT_CHAR))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}