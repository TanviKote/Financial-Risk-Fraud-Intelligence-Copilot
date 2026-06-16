package com.tanvi.riskcopilot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "daily_risk_metrics")
public class DailyRiskMetricEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "metric_id")
	private Integer metricId;

	@Column(name = "metric_date", unique = true)
	private LocalDate metricDate;

	@Column(name = "total_transactions")
	private Integer totalTransactions;

	@Column(name = "failed_transactions")
	private Integer failedTransactions;

	@Column(name = "high_risk_transactions")
	private Integer highRiskTransactions;

	@Column(name = "total_transaction_amount")
	private BigDecimal totalTransactionAmount;

	@Column(name = "failed_transaction_amount")
	private BigDecimal failedTransactionAmount;

	@Column(name = "top_risk_merchant")
	private String topRiskMerchant;

	@Column(name = "most_common_failure_reason")
	private String mostCommonFailureReason;

	@Column(name = "risk_score")
	private BigDecimal riskScore;

	@CreationTimestamp
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	public DailyRiskMetricEntity() {}


}