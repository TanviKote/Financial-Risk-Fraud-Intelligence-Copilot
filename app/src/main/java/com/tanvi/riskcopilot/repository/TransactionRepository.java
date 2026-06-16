package com.tanvi.riskcopilot.repository;

import com.tanvi.riskcopilot.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByEventTimeBetween(
            LocalDateTime start,
            LocalDateTime end);

    long countByStatus(String status);

    long countByRiskFlag(String riskFlag);
}
