package com.bude.banjockback.service;//package com.bude.banjockback.service;
import com.bude.banjockback.entities.Role;
import com.bude.banjockback.entities.User;
import com.bude.banjockback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User registerUser(
            String email,
            String password,
            String username,
            MultipartFile image
    ) throws IOException {

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ADMIN);
        user.setDateInscription(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {

            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            // 🔥 nom unique
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // 🔥 URL correcte
            user.setImageUrl("http://localhost:8090/uploads/" + fileName);
        }

        return userRepository.save(user);
    }


    public Iterable<User> findAll() {
      return   userRepository.findAll();

    }
    public User update(Long id, User newUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        user.setUsername(newUser.getUsername());
        user.setRole(newUser.getRole());
        user.setEmail(newUser.getEmail());
        user.setFonction(newUser.getFonction());
        user.setImageUrl(newUser.getImageUrl());
        user.setProfession(newUser.getProfession());

        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
