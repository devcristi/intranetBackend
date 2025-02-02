//package com.absoluto.demo.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/test",           // Permite accesul la endpoint-ul de test fără autentificare
//                                "/api/users/signup",   // Permite accesul la endpoint-ul de înregistrare fără autentificare
//                                "/api/signup",         // Permite accesul la endpoint-ul de signup fără autentificare
//                                "/api/test-cors",      // Permite accesul la endpoint-ul CORS fără autentificare
//                                "/login"               // Permite accesul la pagina de login fără autentificare
//                        ).permitAll()  // Permite accesul la aceste endpoint-uri fără autentificare
//                        .anyRequest().authenticated()  // Orice altă cerere necesită autentificare
//                )
//                .formLogin(form -> form
////                        .loginPage("/login")  // Specifică pagina de login
////                        .defaultSuccessUrl("/dashboard", true)  // Redirecționează utilizatorul către /dashboard după login reușit
////                        .permitAll()           // Permite accesul la pagina de login
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/dashboard", true)
//                                .permitAll()  // Permite accesul la login fără autentificare
//                )
//                .logout(logout -> logout
//                        .permitAll()           // Permite accesul la logout
//                )
//                .csrf(csrf -> csrf
//                        .disable() // Dezactivează CSRF pentru cererile REST
//                )
//                .cors(cors -> cors.configurationSource(request -> {
//                    var config = new org.springframework.web.cors.CorsConfiguration();
//                    config.addAllowedOrigin("http://localhost:3000");  // Permite cereri din frontend
//                    config.addAllowedMethod("*");  // Permite toate metodele HTTP
//                    config.addAllowedHeader("*");  // Permite toate anteturile
//                    config.setAllowCredentials(true);  // Permite cookie-uri și credențiale
//                    return config;
//                }));  // Configurează CORS pentru a permite accesul din frontend
//
//        return http.build();
//    }
//}
package com.absoluto.demo.security;

import com.absoluto.demo.User;
import com.absoluto.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository; // Repository pentru a obține detaliile complete ale utilizatorului

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configurare CORS
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:3000");
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    config.setAllowCredentials(true);
                    return config;
                }))
                // Dezactivează CSRF pentru API REST
                .csrf(csrf -> csrf.disable())
                // Politica de sesiuni
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                // Reguli de autorizare
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/test",
                                "/api/users/signup",
                                "/api/signup",
                                "/api/test-cors",
                                "/api/users/count",
                                "/api/users",
                                "/login"
                        ).permitAll()
                        .requestMatchers("/api/admin/**", "/api/centre/**").hasRole("ADMIN")
                        .requestMatchers("/api/trainer/**").hasRole("TRAINER")
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().authenticated()
                )
                // Activează HTTP Basic pentru testare (de exemplu, în Postman)
                .httpBasic(Customizer.withDefaults())
                // Configurare formLogin cu success handler personalizat
                .formLogin(form -> form
                        .usernameParameter("email")
                        .loginProcessingUrl("/login")
                        .successHandler((HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            // Obține email-ul din principal
                            String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();

                            // Obține datele complete ale utilizatorului din baza de date
                            Optional<User> userOptional = userRepository.findByEmail(email);
                            if (!userOptional.isPresent()) {
                                response.getWriter().write("{\"message\": \"User not found\"}");
                                return;
                            }
                            User user = userOptional.get();

                            // Verifică dacă user-ul este admin
                            boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");
                            String redirectUrl = isAdmin ? "http://localhost:3000/admin" : "http://localhost:3000/dashboard";

                            // Folosește Jackson ObjectMapper pentru a serializa obiectul user în JSON
                            ObjectMapper mapper = new ObjectMapper();
                            String userJson;
                            try {
                                userJson = mapper.writeValueAsString(user);
                            } catch (IOException e) {
                                userJson = "{}";
                            }
                            // Construiește răspunsul JSON complet
                            String jsonResponse = String.format(
                                    "{\"message\": \"Autentificare reușită\", \"redirectUrl\": \"%s\", \"user\": %s}",
                                    redirectUrl, userJson
                            );
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\": \"Autentificare eșuată!\"}");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )
                // Configurare logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\": \"Logout reușit!\"}");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )
                // Configurează un authenticationEntryPoint pentru cereri neautentificate
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((HttpServletRequest request, HttpServletResponse response,
                                                   org.springframework.security.core.AuthenticationException authException) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
