package com.absoluto.demo.service;

import com.absoluto.demo.DTO.UserSignUpDto;
import com.absoluto.demo.User;
import com.absoluto.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Înregistrare utilizator cu rol implicit "STUDENT"
    public User registerUser(UserSignUpDto userSignUpDto) {
        if (!userSignUpDto.getPassword().equals(userSignUpDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Parolele nu coincid");
        }

        User user = new User();
        user.setFirstName(userSignUpDto.getFirstName());
        user.setLastName(userSignUpDto.getLastName());
        user.setEmail(userSignUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(userSignUpDto.getPassword()));

        // ✅ Setăm rolul implicit la "ROLE_STUDENT"
        user.addRole("ROLE_STUDENT");

        return userRepository.save(user);
    }

    // ✅ Atribuie un rol utilizatorului
    public void assignRoleToUser(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.addRole(role); // ✅ Adăugăm rolul
        userRepository.save(user);
    }

    // ✅ Elimină un rol din contul utilizatorului
    public void removeRoleFromUser(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.removeRole(role); // ✅ Eliminăm rolul
        userRepository.save(user);
    }

    // ✅ Returnează numărul total de utilizatori
    public long getUserCount() {
        return userRepository.count();
    }
}
