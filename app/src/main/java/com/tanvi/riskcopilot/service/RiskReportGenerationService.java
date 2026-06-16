package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.domain.DailyRiskMetricEntity;
import com.tanvi.riskcopilot.domain.RiskReportEntity;
import com.tanvi.riskcopilot.dto.RiskDashboardResponse;
import com.tanvi.riskcopilot.repository.DailyRiskMetricRepository;
import com.tanvi.riskcopilot.repository.RiskReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RiskReportGenerationService {
	private static final Logger log =
			LoggerFactory.getLogger(RiskReportGenerationService.class);

	private final DailyRiskMetricRepository metricRepository;
	private final RiskReportRepository reportRepository;

	private final AiRiskAnalysisService aiRiskAnalysisService;

	public RiskReportGenerationService (
			DailyRiskMetricRepository metricRepository,
			RiskReportRepository reportRepository,
			AiRiskAnalysisService aiRiskAnalysisService){
		this.metricRepository = metricRepository;
		this.reportRepository = reportRepository;
		this.aiRiskAnalysisService = aiRiskAnalysisService;
	}

	public RiskReportEntity generateReport(){
		DailyRiskMetricEntity metric = metricRepository.findByMetricDate(LocalDate.now())
						.orElseThrow(()->
								new RuntimeException("No Metrics found for Today"));
		
		String riskContext = buildRiskContext(metric);

		List<RiskReportEntity> history = reportRepository.findTop3ByOrderByCreatedAtDesc();

		StringBuilder historicalContext = new StringBuilder();

		for (RiskReportEntity historicalReport : history) {

			if (historicalReport.getLlmSummary() == null) {
				continue;
			}
			historicalContext.append(
							"Historical Report:\n")
					.append(historicalReport.getLlmSummary())
					.append("\n\n");
		}

		String summary;
		try{
			summary = aiRiskAnalysisService.generateSummary(metric, historicalContext.toString());
		} catch (Exception ex){
			summary = generateFallbackSummary(metric);
			log.warn(
					"AI summary generation failed. Using fallback summary.", ex);
		}

		String recommendation = generateRecommendation(metric);

		RiskReportEntity report = new RiskReportEntity();

		report.setReportDate(LocalDate.now());
		report.setReportType("DAILY_FRAUD_RISK");
		report.setRiskContext(riskContext);
		report.setLlmSummary(summary);
		report.setRecommendation(recommendation);

		return reportRepository.save(report);
	}
	private String generateFallbackSummary(DailyRiskMetricEntity metric) {
		return String.format(
				"""
				Daily fraud monitoring identified %d failed transactions
				and %d high-risk transactions. Merchant %s showed the
				highest concentration of risky activity. Overall risk
				score for the day was %s.
				""",
				metric.getFailedTransactions(),
				metric.getHighRiskTransactions(),
				metric.getTopRiskMerchant(),
				metric.getRiskScore()
		);
	}

	private String buildRiskContext(DailyRiskMetricEntity metric) {
		return String.format(
				"""
				Total Transactions: %d
				Failed Transactions: %d
				High Risk Transactions: %d
				Total Amount: %s
				Failed Amount: %s
				Top Risk Merchant: %s
				Most Common Failure Reason: %s
				Risk Score: %s
				""",
				metric.getTotalTransactions(),
				metric.getFailedTransactions(),
				metric.getHighRiskTransactions(),
				metric.getTotalTransactionAmount(),
				metric.getFailedTransactionAmount(),
				metric.getTopRiskMerchant(),
				metric.getMostCommonFailureReason(),
				metric.getRiskScore()
		);
	}
	private String generateRecommendation(DailyRiskMetricEntity metric) {
		if (metric.getRiskScore().doubleValue() >= 75) {
			return "Immediate fraud investigation recommended.";
		}

		if (metric.getRiskScore().doubleValue() >= 50) {
			return "Increase transaction monitoring and merchant review.";
		}

		return "Continue normal monitoring.";
	}

	public RiskDashboardResponse getDashboard(){
		DailyRiskMetricEntity metric =
				metricRepository.findTopByOrderByMetricDateDesc()
						.orElseThrow(() ->
								new RuntimeException("No risk metrics found"));
		return new RiskDashboardResponse(
				metric.getTotalTransactions(),
				metric.getFailedTransactions(),
				metric.getHighRiskTransactions(),
				metric.getRiskScore(),
				determineRiskLevel(metric.getRiskScore().doubleValue()),
				metric.getTopRiskMerchant()
		);
	}
	private String determineRiskLevel(double score) {

		if (score >= 76) {
			return "CRITICAL";
		}

		if (score >= 51) {
			return "HIGH";
		}

		if (score >= 21) {
			return "MEDIUM";
		}

		return "LOW";
	}
}
