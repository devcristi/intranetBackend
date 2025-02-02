package com.absoluto.demo.security;

import com.absoluto.demo.repository.UserRepository;
import com.absoluto.demo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Verific utilizatorul cu email-ul primit: '" + email + "'");

        if (email == null || email.trim().isEmpty()) {
            System.out.println("🚨 EROARE: Email-ul primit este gol!");
            throw new UsernameNotFoundException("Email-ul nu poate fi gol");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            System.out.println("❌ Utilizatorul nu a fost găsit pentru email-ul: '" + email + "'");
            throw new UsernameNotFoundException("Utilizatorul nu a fost găsit cu emailul: " + email);
        }

        User user = optionalUser.get();
        System.out.println("✅ Utilizator găsit: " + user.getEmail() + " | Parola: " + user.getPassword());

        // Mapăm rolurile din entitate la GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}