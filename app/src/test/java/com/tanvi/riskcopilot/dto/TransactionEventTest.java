package com.tanvi.riskcopilot.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEventTest {
    @Test
    void highRiskFailedTransactionIsDetected() {
        TransactionEvent event = new TransactionEvent(
                "TX1", "C1", "M1", BigDecimal.TEN, "USD", "FAILED", "HIGH",
                "MULTIPLE_RETRY_ATTEMPTS", LocalDateTime.now()
        );

        assertTrue(event.isFailed());
        assertTrue(event.isHighRisk());
    }
}
