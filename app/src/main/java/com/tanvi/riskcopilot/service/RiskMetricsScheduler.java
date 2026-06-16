package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.domain.DailyRiskMetricEntity;
import com.tanvi.riskcopilot.domain.TransactionEntity;
import com.tanvi.riskcopilot.repository.DailyRiskMetricRepository;
import com.tanvi.riskcopilot.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RiskMetricsScheduler{

	private static final Logger log =
			LoggerFactory.getLogger(RiskMetricsScheduler.class);
	private final TransactionRepository transactionRepository;
	private final DailyRiskMetricRepository metricRepository;

	public RiskMetricsScheduler(TransactionRepository transactionRepository,
	                            DailyRiskMetricRepository metricRepository) {
		this.transactionRepository = transactionRepository;
		this.metricRepository = metricRepository;
	}
	/**
	 * Every 60 seconds for development/testing.
	 * Later change to:
	 * Scheduled(cron = "0 0 1 * * *")
	 * for daily execution at 1 AM.
	 */
	@Scheduled(fixedRate = 60000)
	public void generateDailyRiskMetrics() {

		log.info("Starting risk metrics aggregation");

		List<TransactionEntity> transactions =
				transactionRepository.findAll();

		if (transactions.isEmpty()) {
			log.info("No transactions found. Skipping metrics generation.");
			return;
		}

		int totalTransactions = transactions.size();

		List<TransactionEntity> failedTransactions =
				transactions.stream()
						.filter(t -> "FAILED".equalsIgnoreCase(t.getStatus()))
						.toList();

		List<TransactionEntity> highRiskTransactions =
				transactions.stream()
						.filter(t ->
								"HIGH".equalsIgnoreCase(t.getRiskFlag())
										|| "CRITICAL".equalsIgnoreCase(t.getRiskFlag()))
						.toList();

		int failedCount = failedTransactions.size();
		int highRiskCount = highRiskTransactions.size();

		BigDecimal totalAmount =
				transactions.stream()
						.map(TransactionEntity::getAmount)
						.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal failedAmount =
				failedTransactions.stream()
						.map(TransactionEntity::getAmount)
						.reduce(BigDecimal.ZERO, BigDecimal::add);

		String topRiskMerchant =
				highRiskTransactions.stream()
						.collect(Collectors.groupingBy(
								TransactionEntity::getMerchantId,
								Collectors.counting()))
						.entrySet()
						.stream()
						.max(Map.Entry.comparingByValue())
						.map(Map.Entry::getKey)
						.orElse(null);

		String mostCommonFailureReason =
				failedTransactions.stream()
						.filter(t -> t.getFailureReason() != null)
						.collect(Collectors.groupingBy(
								TransactionEntity::getFailureReason,
								Collectors.counting()))
						.entrySet()
						.stream()
						.max(Comparator.comparing(Map.Entry::getValue))
						.map(Map.Entry::getKey)
						.orElse(null);

		double failedRate =
				((double) failedCount / totalTransactions) * 100;

		double highRiskRate =
				((double) highRiskCount / totalTransactions) * 100;

		double riskScore =
				(failedRate * 0.4)
						+ (highRiskRate * 0.4);

		LocalDate today = LocalDate.now();

		DailyRiskMetricEntity metric =
				metricRepository.findByMetricDate(today)
						.orElseGet(DailyRiskMetricEntity::new);

		metric.setMetricDate(today);
		metric.setTotalTransactions(totalTransactions);
		metric.setFailedTransactions(failedCount);
		metric.setHighRiskTransactions(highRiskCount);
		metric.setTotalTransactionAmount(totalAmount);
		metric.setFailedTransactionAmount(failedAmount);
		metric.setTopRiskMerchant(topRiskMerchant);
		metric.setMostCommonFailureReason(mostCommonFailureReason);
		metric.setRiskScore(
				BigDecimal.valueOf(riskScore)
						.setScale(2, RoundingMode.HALF_UP)
		);

		metricRepository.save(metric);

		log.info(
				"Risk metrics generated: total={}, failed={}, highRisk={}, score={}",
				totalTransactions,
				failedCount,
				highRiskCount,
				metric.getRiskScore()
		);
	}
}