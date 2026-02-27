package com.bude.banjockback.service;

import com.bude.banjockback.entities.Publication;
import com.bude.banjockback.entities.User;
import com.bude.banjockback.repository.PublicationRepository;
import com.bude.banjockback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository repo;

    @Autowired
    private UserRepository userRepo;

    public Publication create(String content, MultipartFile file, String username) throws IOException {

        User user = userRepo.findByUsername(username).orElseThrow();
        Publication pub = new Publication();
        pub.setContent(content);
        pub.setCreatedAt(LocalDateTime.now());
        pub.setUser(user);
//        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            // 🔥 nom unique
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // 🔥 URL correcte
            pub.setImageUrl("http://localhost:8090/uploads/" + fileName);
        }

        return repo.save(pub);
    }

    public List<Publication> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void like(Long id, String username) {
        Publication pub = repo.findById(id).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow();

        if (pub.getLikes().contains(user))
            pub.getLikes().remove(user);
        else
            pub.getLikes().add(user);

        repo.save(pub);
    }
}

