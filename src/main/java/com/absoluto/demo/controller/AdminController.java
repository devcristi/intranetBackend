package com.absoluto.demo.controller;

import com.absoluto.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalUsers = userRepository.count(); // 📌 Număr total de utilizatori
        return ResponseEntity.ok(new StatsResponse(totalUsers));
    }

    // DTO pentru răspuns
    private record StatsResponse(long totalUsers) {}
}
