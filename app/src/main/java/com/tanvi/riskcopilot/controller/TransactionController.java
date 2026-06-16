package com.tanvi.riskcopilot.controller;

import com.tanvi.riskcopilot.dto.TransactionEvent;
import com.tanvi.riskcopilot.repository.TransactionRepository;
import com.tanvi.riskcopilot.service.TransactionPublisherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionPublisherService publisherService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionPublisherService publisherService, TransactionRepository transactionRepository) {
        this.publisherService = publisherService;
        this.transactionRepository = transactionRepository;
    }
    @PostMapping("/publish")
    public ResponseEntity<Map<String, String>> publish(@Valid @RequestBody TransactionEvent event) {
        publisherService.publish(event);
        return ResponseEntity.accepted().body(Map.of("status", "PUBLISHED", "transactionId", event.transactionId()));
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> get(@PathVariable String transactionId) {
        return transactionRepository.findById(transactionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
