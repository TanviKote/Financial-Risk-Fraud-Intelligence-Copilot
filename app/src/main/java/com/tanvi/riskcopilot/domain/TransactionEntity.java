package com.tanvi.riskcopilot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String currency;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "risk_flag", length = 30)
    private String riskFlag;

    @Column(name = "failure_reason", length = 100)
    private String failureReason;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    protected TransactionEntity() {}

    public TransactionEntity(String transactionId, String customerId, String merchantId, BigDecimal amount,
                             String currency, String status, String riskFlag, String failureReason,
                             LocalDateTime eventTime) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency == null ? "USD" : currency;
        this.status = status;
        this.riskFlag = riskFlag;
        this.failureReason = failureReason;
        this.eventTime = eventTime;
    }
}
