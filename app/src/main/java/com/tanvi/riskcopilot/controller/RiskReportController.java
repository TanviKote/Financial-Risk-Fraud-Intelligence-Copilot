package com.tanvi.riskcopilot.controller;

import com.tanvi.riskcopilot.repository.RiskReportRepository;
import com.tanvi.riskcopilot.service.RiskReportGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk/reports")
public class RiskReportController {
    private final RiskReportRepository repository;
    private final RiskReportGenerationService riskReportGenerationService;

    public RiskReportController(
            RiskReportRepository repository,
            RiskReportGenerationService riskReportGenerationService) {

        this.repository = repository;
        this.riskReportGenerationService = riskReportGenerationService;
    }

    @GetMapping("/latest")
    public ResponseEntity<?> latest() {
        return repository.findTopByOrderByCreatedAtDesc()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> byId(@PathVariable Long reportId) {
        return repository.findById(reportId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/generate")
    public ResponseEntity<?> generate(){
        return ResponseEntity.ok(
                riskReportGenerationService.generateReport());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return ResponseEntity.ok(
                riskReportGenerationService.getDashboard());
    }
}
