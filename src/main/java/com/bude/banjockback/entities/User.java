package com.bude.banjockback.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email obligatoire")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true ,nullable = false)
    private String username;

    private  String imageUrl ;

    private  String profession;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private  Role  role ;

    private String fonction;

    private LocalDateTime dateInscription;



}