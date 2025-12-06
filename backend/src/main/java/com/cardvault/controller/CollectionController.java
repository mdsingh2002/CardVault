package com.cardvault.controller;

import com.cardvault.dto.AddToCollectionRequest;
import com.cardvault.dto.UserCardDto;
import com.cardvault.model.UserCard;
import com.cardvault.repository.UserRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collection")
@CrossOrigin(origins = "http://localhost:3000")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserCardDto>> getUserCollection() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        List<UserCard> collection = collectionService.getUserCollection(userId);
        List<UserCardDto> dtos = collection.stream()
                .map(UserCardDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCardDto> getUserCardById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        return collectionService.getUserCardById(userId, id)
                .map(UserCardDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserCardDto> addToCollection(@Valid @RequestBody AddToCollectionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        UserCard userCard = collectionService.addToCollection(userId, request);
        UserCardDto dto = UserCardDto.fromEntity(userCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCardDto> updateUserCard(
            @PathVariable UUID id,
            @Valid @RequestBody AddToCollectionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        UserCard userCard = collectionService.updateUserCard(userId, id, request);
        UserCardDto dto = UserCardDto.fromEntity(userCard);
        return ResponseEntity.ok(dto);
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();
    }
}
