package com.absoluto.demo.controller;

import com.absoluto.demo.User;
import com.absoluto.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint pentru a obține lista tuturor trainerilor.
     * Se filtrează utilizatorii care au rolul "ROLE_TRAINER".
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllTrainers() {
        List<User> trainers = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains("ROLE_TRAINER"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainers);
    }

    // Poți adăuga și alte endpoint-uri specifice pentru traineri,
    // de exemplu, update, ștergere sau obținerea detaliilor unui trainer.
}
