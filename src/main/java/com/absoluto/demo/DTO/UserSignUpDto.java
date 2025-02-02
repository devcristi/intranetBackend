package com.absoluto.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserSignUpDto {

    @NotBlank(message = "Numele este obligatoriu")
    private String firstName;

    @NotBlank(message = "Prenumele este obligatoriu")
    private String lastName;

    @Email(message = "Email-ul nu este valid")
    @NotBlank(message = "Email-ul este obligatoriu")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere")
    private String password;

    @NotBlank(message = "Confirmarea parolei este obligatorie")
    private String confirmPassword;

    // Getter și setter pentru firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter și setter pentru lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter și setter pentru email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter și setter pentru password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter și setter pentru confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
