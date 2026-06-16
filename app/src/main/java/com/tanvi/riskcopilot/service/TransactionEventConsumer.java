package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.dto.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {
    private final TransactionIngestionService ingestionService;

    public TransactionEventConsumer(TransactionIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @KafkaListener(topics = "${app.kafka.transaction-topic}", groupId = "risk-copilot-consumer-group")
    public void consume(TransactionEvent event) {
        ingestionService.ingest(event);
    }
}
