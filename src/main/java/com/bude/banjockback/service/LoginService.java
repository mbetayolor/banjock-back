package com.bude.banjockback.service;

import com.bude.banjockback.entities.LoginRequest;
import com.bude.banjockback.entities.User;
import com.bude.banjockback.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(LoginRequest request) {

        // 1️⃣ chercher l'utilisateur
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Email ou mot de passe incorrect"));

        // 2️⃣ vérifier le mot de passe
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // 3️⃣ sécurité : ne jamais renvoyer le mot de passe
        user.setPassword(null);

        return user;
    }
}
