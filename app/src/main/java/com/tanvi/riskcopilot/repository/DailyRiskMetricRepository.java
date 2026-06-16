package com.tanvi.riskcopilot.repository;

import com.tanvi.riskcopilot.domain.DailyRiskMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
public interface DailyRiskMetricRepository
		extends JpaRepository<DailyRiskMetricEntity, Integer> {

	Optional<DailyRiskMetricEntity> findByMetricDate(LocalDate metricDate);

	Optional<DailyRiskMetricEntity> findTopByOrderByMetricDateDesc();
}
