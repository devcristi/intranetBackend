package com.absoluto.demo;

import com.absoluto.demo.centre.Centre;
import com.absoluto.demo.role.RolesConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import java.util.HashSet;
import java.util.Set;
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter
@Setter
@Table(name = "users_absoluto")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    private String firstName;

    @NotBlank(message = "Prenumele este obligatoriu")
    private String lastName;

    @Email(message = "Email-ul nu este valid")
    @NotBlank(message = "Email-ul este obligatoriu")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere")
    private String password;

    // Rolurile sunt stocate folosind un converter (sau orice altă metodă preferată)
    @Convert(converter = RolesConverter.class)
    @Column(name = "roles", columnDefinition = "TEXT")
    private Set<String> roles = new HashSet<>();

    // Relație bidirecțională ManyToMany cu Centre
    // Această mapare inversează relația definită în entitatea Centre (prin "assignedUsers")
    @ManyToMany(mappedBy = "assignedUsers")
    private Set<Centre> centres = new HashSet<>();

    // Metodă pentru adăugarea unui rol
    public void addRole(String role) {
        this.roles.add(role);
    }

    // Metodă pentru eliminarea unui rol
    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Centre> getCentres() {
        return centres;
    }

    public void setCentres(Set<Centre> centres) {
        this.centres = centres;
    }
}
