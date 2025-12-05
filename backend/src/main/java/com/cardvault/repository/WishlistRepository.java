package com.cardvault.repository;

import com.cardvault.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    List<Wishlist> findByUserId(UUID userId);

    List<Wishlist> findByUserIdOrderByPriorityDesc(UUID userId);

    Optional<Wishlist> findByUserIdAndCardId(UUID userId, UUID cardId);

    boolean existsByUserIdAndCardId(UUID userId, UUID cardId);

    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.user.id = :userId")
    Long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.priority >= :minPriority ORDER BY w.priority DESC")
    List<Wishlist> findHighPriorityItems(@Param("userId") UUID userId, @Param("minPriority") Integer minPriority);
}
