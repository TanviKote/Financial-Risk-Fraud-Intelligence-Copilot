package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.domain.TransactionEntity;
import com.tanvi.riskcopilot.dto.TransactionEvent;
import com.tanvi.riskcopilot.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class TransactionIngestionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionIngestionService.class);
    private final TransactionRepository repository;

    public TransactionIngestionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void ingest(TransactionEvent event) {
        if (repository.existsById(event.transactionId())) {
            log.info("Duplicate transaction ignored: {}", event.transactionId());
            return;
        }
        try {
            repository.save(new TransactionEntity(
                    event.transactionId(), event.customerId(), event.merchantId(), event.amount(),
                    event.currency(), event.status(), event.riskFlag(), event.failureReason(), event.eventTime()
            ));
            log.info("Stored transaction event: {}", event.transactionId());
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate or invalid transaction rejected: {}", event.transactionId(), ex);
        }
    }
}
