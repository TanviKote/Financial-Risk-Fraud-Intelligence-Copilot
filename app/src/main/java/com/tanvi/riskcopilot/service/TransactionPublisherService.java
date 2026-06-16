package com.tanvi.riskcopilot.service;

import com.tanvi.riskcopilot.dto.TransactionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionPublisherService {
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final String topic;

    public TransactionPublisherService(KafkaTemplate<String, TransactionEvent> kafkaTemplate,
                                       @Value("${app.kafka.transaction-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(TransactionEvent event) {
        kafkaTemplate.send(topic, event.transactionId(), event);
    }
}
