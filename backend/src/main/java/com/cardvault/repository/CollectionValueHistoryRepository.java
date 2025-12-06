package com.cardvault.repository;

import com.cardvault.model.CollectionValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionValueHistoryRepository extends JpaRepository<CollectionValueHistory, UUID> {

    List<CollectionValueHistory> findByUserIdOrderByRecordedAtAsc(UUID userId);

    @Query("SELECT cvh FROM CollectionValueHistory cvh WHERE cvh.user.id = :userId AND cvh.recordedAt >= :since ORDER BY cvh.recordedAt ASC")
    List<CollectionValueHistory> findByUserIdSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);

    @Query("SELECT cvh FROM CollectionValueHistory cvh WHERE cvh.user.id = :userId ORDER BY cvh.recordedAt DESC LIMIT 1")
    CollectionValueHistory findLatestByUserId(@Param("userId") UUID userId);
}
