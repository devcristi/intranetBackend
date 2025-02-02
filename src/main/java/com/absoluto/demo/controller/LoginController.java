package com.absoluto.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "redirect:http://localhost:3000/dashboard"; // Redirecționează către frontend-ul React
    }
}
