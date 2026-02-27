package com.bude.banjockback.controler;

import com.bude.banjockback.entities.LoginRequest;
import com.bude.banjockback.entities.User;
import com.bude.banjockback.service.LoginService;
import com.bude.banjockback.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Controller

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private  final  LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          PasswordEncoder passwordEncoder ,
                          LoginService loginService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.loginService= loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Validated
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        if (userService.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur : Cet email est déjà utilisé !");
        }
        if (userService.existsByUsername(username)) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur : Ce user existe deja !");
        }


        User savedUser = userService.registerUser(email, password, username, image);
        savedUser.setPassword(null); // ne pas retourner le mot de passe

        return ResponseEntity.ok(savedUser);
    }


    // ✅ LISTE DES USERS
    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        User user = loginService.login(request);
        // 2️⃣ Crée l'Authentication Spring Security pour la session
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3️⃣ Crée la session et le cookie JSESSIONID
        httpRequest.getSession(true);

        // 4️⃣ Retourne l'utilisateur au frontend (facultatif)
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @RequestBody User user) {

        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
