package com.tanvi.riskcopilot.repository;

import com.tanvi.riskcopilot.domain.RiskReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RiskReportRepository extends JpaRepository<RiskReportEntity, Long> {
    Optional<RiskReportEntity> findTopByOrderByCreatedAtDesc();
    List<RiskReportEntity> findTop3ByOrderByCreatedAtDesc();
}
