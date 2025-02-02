package com.absoluto.demo.controller;

import com.absoluto.demo.DTO.UserSignUpDto;
import com.absoluto.demo.User;
import com.absoluto.demo.service.UserService;
import com.absoluto.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Validated UserSignUpDto userSignUpDto) {
        User user = userService.registerUser(userSignUpDto);
        return ResponseEntity.ok("User created successfully with ID: " + user.getId());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // ✅ Returnăm doar datele necesare
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("firstName", user.getFirstName());
        userInfo.put("lastName", user.getLastName());
        userInfo.put("email", user.getEmail());
        userInfo.put("roles", user.getRoles());

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userService.getUserCount());
    }

    @PutMapping("/{id}/assign-role")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Long id, @RequestParam String role) {
        userService.assignRoleToUser(id, role);
        return ResponseEntity.ok("Rolul " + role + " a fost atribuit utilizatorului " + id);
    }

    @PutMapping("/{id}/remove-role")
    public ResponseEntity<String> removeRoleFromUser(@PathVariable Long id, @RequestParam String role) {
        userService.removeRoleFromUser(id, role);
        return ResponseEntity.ok("Rolul " + role + " a fost eliminat de la utilizatorul " + id);
    }
}
