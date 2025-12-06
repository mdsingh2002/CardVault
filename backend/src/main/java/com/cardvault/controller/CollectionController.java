package com.cardvault.controller;

import com.cardvault.dto.AddToCollectionRequest;
import com.cardvault.model.UserCard;
import com.cardvault.service.CollectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/collection")
@CrossOrigin(origins = "http://localhost:3000")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public ResponseEntity<List<UserCard>> getUserCollection() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        List<UserCard> collection = collectionService.getUserCollection(userId);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCard> getUserCardById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        return collectionService.getUserCardById(userId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserCard> addToCollection(@Valid @RequestBody AddToCollectionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        UserCard userCard = collectionService.addToCollection(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCard> updateUserCard(
            @PathVariable UUID id,
            @Valid @RequestBody AddToCollectionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        UserCard userCard = collectionService.updateUserCard(userId, id, request);
        return ResponseEntity.ok(userCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCollection(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        collectionService.removeFromCollection(userId, id);
        return ResponseEntity.noContent().build();
    }

    private UUID getUserIdFromAuth(Authentication auth) {
        String username = auth.getName();
        return UUID.randomUUID();
    }
}
