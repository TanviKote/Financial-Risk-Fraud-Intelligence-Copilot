package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.domain.DailyRiskMetricEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiRiskAnalysisService {
	private final ChatClient chatClient;

	public AiRiskAnalysisService(ChatClient.Builder builder) {

		this.chatClient = builder.build();
	}

	public String generateSummary(DailyRiskMetricEntity metric, String historicalContext) {
		String prompt = buildPrompt(metric, historicalContext);
		return chatClient.prompt(prompt)
				.call()
				.content();
	}
	private String buildPrompt(DailyRiskMetricEntity metric, String historicalContext) {
		return """
You are a senior fraud analyst.

Analyze ONLY the provided metrics.
Do not invent additional statistics.
Do not assume information not present in the data.

Metrics:

Total Transactions: %d
Failed Transactions: %d
High Risk Transactions: %d
Total Amount: %.2f
Failed Amount: %.2f
Top Risk Merchant: %s
Most Common Failure Reason: %s
Risk Score: %.2f

Historical Fraud Reports:

%s

Compare today's metrics with historical patterns.

Generate:

1. Executive Summary
2. Risk Assessment
3. Business Impact
4. Recommended Actions

Keep response under 150 words.
Use professional financial language.
"""
				.formatted(
						metric.getTotalTransactions(),
						metric.getFailedTransactions(),
						metric.getHighRiskTransactions(),
						metric.getTotalTransactionAmount(),
						metric.getFailedTransactionAmount(),
						metric.getTopRiskMerchant(),
						metric.getMostCommonFailureReason(),
						metric.getRiskScore(),
						historicalContext
				);
	}
}
