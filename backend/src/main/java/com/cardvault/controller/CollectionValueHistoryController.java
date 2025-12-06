package com.cardvault.controller;

import com.cardvault.dto.CollectionValueHistoryDto;
import com.cardvault.model.CollectionValueHistory;
import com.cardvault.repository.UserRepository;
import com.cardvault.service.CollectionValueHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collection-history")
@CrossOrigin(origins = "http://localhost:3000")
public class CollectionValueHistoryController {

    @Autowired
    private CollectionValueHistoryService historyService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Record a snapshot of the current collection value
     */
    @PostMapping("/snapshot")
    public ResponseEntity<CollectionValueHistoryDto> recordSnapshot() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);

        CollectionValueHistory snapshot = historyService.recordSnapshot(userId);
        CollectionValueHistoryDto dto = CollectionValueHistoryDto.fromEntity(snapshot);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Get all collection value history
     */
    @GetMapping
    public ResponseEntity<List<CollectionValueHistoryDto>> getHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);

        List<CollectionValueHistory> history = historyService.getHistory(userId);
        List<CollectionValueHistoryDto> dtos = history.stream()
                .map(CollectionValueHistoryDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Get collection value history for the last N days
     */
    @GetMapping("/last-days/{days}")
    public ResponseEntity<List<CollectionValueHistoryDto>> getHistoryForLastDays(@PathVariable int days) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);

        List<CollectionValueHistory> history = historyService.getHistoryForLastDays(userId, days);
        List<CollectionValueHistoryDto> dtos = history.stream()
                .map(CollectionValueHistoryDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Get collection value history since a specific date
     */
    @GetMapping("/since")
    public ResponseEntity<List<CollectionValueHistoryDto>> getHistorySince(@RequestParam String since) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);

        LocalDateTime sinceDate = LocalDateTime.parse(since);
        List<CollectionValueHistory> history = historyService.getHistorySince(userId, sinceDate);
        List<CollectionValueHistoryDto> dtos = history.stream()
                .map(CollectionValueHistoryDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    private UUID getUserIdFromAuth(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();
    }
}
