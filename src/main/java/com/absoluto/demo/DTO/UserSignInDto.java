package com.absoluto.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserSignInDto {

    @Email(message = "Email-ul nu este valid")
    @NotBlank(message = "Email-ul este obligatoriu")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    private String password;

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
}
