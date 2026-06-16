package com.tanvi.riskcopilot.dto;

import java.math.BigDecimal;
public record RiskDashboardResponse(Integer totalTransactions,
                                    Integer failedTransactions,
                                    Integer highRiskTransactions,
                                    BigDecimal riskScore,
                                    String riskLevel,
                                    String topRiskMerchant) {
}
