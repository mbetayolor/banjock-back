package com.bude.banjockback.controler;

import com.bude.banjockback.entities.Publication;
import com.bude.banjockback.entities.User;
import com.bude.banjockback.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicationController {

    @Autowired
    private PublicationService service;

    @PostMapping
    public Publication create(
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam String username
    ) throws IOException {


        return service.create(content, image, username);
    }

    @GetMapping
    public List<Publication> all() {
        return service.getAll();
    }

    @PutMapping("/{id}/like")
    public void like(@PathVariable Long id, Principal principal) {
        service.like(id, principal.getName());
    }
}
