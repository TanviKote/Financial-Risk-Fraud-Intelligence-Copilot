package com.tanvi.riskcopilot.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Java 17 record: immutable event contract with low boilerplate.
 */
public record TransactionEvent(
        @NotBlank String transactionId,
        @NotBlank String customerId,
        @NotBlank String merchantId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        String currency,
        @NotBlank String status,
        String riskFlag,
        String failureReason,
        @NotNull LocalDateTime eventTime
) {
    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }

    public boolean isHighRisk() {
        return "HIGH".equalsIgnoreCase(riskFlag) || "CRITICAL".equalsIgnoreCase(riskFlag);
    }
}
