package com.tanvi.riskcopilot.controller;

import com.tanvi.riskcopilot.domain.DailyRiskMetricEntity;
import com.tanvi.riskcopilot.repository.DailyRiskMetricRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@RestController
@RequestMapping("/risk/metrics")
public class RiskMetricsController {
	private final DailyRiskMetricRepository repository;

	public RiskMetricsController(
			DailyRiskMetricRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/latest")
	public DailyRiskMetricEntity latest() {

		return repository.findAll()
				.stream()
				.max(Comparator.comparing(
						DailyRiskMetricEntity::getMetricDate))
				.orElse(null);
	}
}
