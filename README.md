# Financial Risk & Fraud Intelligence Copilot

A lightweight financial risk analytics and AI-powered decision support platform built using Java 17, Spring Boot, Kafka, PostgreSQL, Spring AI, Ollama, Qwen, and Docker. The system processes transaction events, calculates fraud risk metrics, generates AI-powered fraud intelligence reports, incorporates historical risk reports through a lightweight RAG workflow, and exposes dashboard APIs for real-time fraud risk monitoring and decision support.

---

# Why This Project Exists

Financial institutions process large volumes of transactions every day. Risk and fraud teams need to quickly identify:

* Suspicious transactions
* Repeated payment failures
* High-risk merchants
* Fraud trends
* Operational payment issues

Manual analysis can be slow and inconsistent. This project demonstrates how event-driven systems and AI can automate fraud intelligence workflows using a lightweight architecture.

---

# Resume Title

**Financial Risk & Fraud Intelligence Copilot (Kafka + PostgreSQL + AI + Lightweight RAG)**

---

# Technology Stack

| Technology       | Usage                                     |
| ---------------- | ----------------------------------------- |
| Java 17          | Records, Streams, Text Blocks             |
| Spring Boot      | REST APIs, Scheduling, Validation, JPA    |
| Kafka            | Event-driven transaction ingestion        |
| PostgreSQL       | Transaction and risk data storage         |
| Spring Scheduler | Automated daily risk metric generation    |
| Spring AI        | AI integration layer                      |
| Ollama           | Local AI model execution                  |
| Qwen 2.5         | Fraud risk analysis and report generation |
| Docker Compose   | Infrastructure setup                      |
| GitHub Actions   | Build validation                          |

---

# Architecture

```text
Transaction Producer
        |
        v
Kafka Topic
(transaction-events)
        |
        v
Kafka Consumer
        |
        v
PostgreSQL
(transactions)
        |
        v
Spring Scheduler
        |
        v
daily_risk_metrics
        |
        v
Historical Risk Reports
        +
Current Risk Metrics
        |
        v
Ollama + Qwen
        |
        v
risk_reports
```

---

# Core Features

## Transaction Event Processing

* Kafka-based transaction ingestion
* Event validation
* Duplicate transaction protection
* Persistent storage in PostgreSQL

## Daily Risk Metrics

Automatically calculates:

* Total transactions
* Failed transactions
* High-risk transactions
* Failed transaction amount
* Top risk merchant
* Most common failure reason
* Overall fraud risk score

## AI Fraud Intelligence

Generates:

* Executive Summary
* Risk Assessment
* Business Impact
* Recommended Actions

using current fraud metrics and historical reports.

## Lightweight RAG

Instead of vector search, the system retrieves recent fraud reports from PostgreSQL and includes them in the AI prompt as historical context.

This provides context-aware fraud analysis while keeping the architecture simple and lightweight.

---

# Project Scope

```text
1 Kafka Topic
3 Core PostgreSQL Tables
1 Spring Scheduler
1 AI Workflow
1 Lightweight RAG Workflow

No Frontend
No Kubernetes
No Terraform
No Airflow
```

---

# Database Tables

```text
transactions
daily_risk_metrics
risk_reports
```

---

# Quick Start

## Start Infrastructure

```bash
docker compose -f docker/docker-compose.yml up -d
```

Starts:

* PostgreSQL
* Kafka
* Zookeeper

---

## Start Ollama

```bash
ollama run qwen2.5:0.5b
```

---

## Start Spring Boot

```bash
cd app
mvn spring-boot:run
```

Application:

```text
http://localhost:8080
```

---

## Publish Transaction Event

```bash
curl -X POST http://localhost:8080/transactions/publish
```

---

## Generate Risk Metrics

Automatically generated through Spring Scheduler.

---

## Generate AI Risk Report

```bash
curl http://localhost:8080/risk/reports/generate
```

---

## Retrieve Latest Report

```bash
curl http://localhost:8080/risk/reports/latest
```

---

# Risk Score Formula

```text
(Failed Transaction Rate × 40)
+
(High Risk Transaction Rate × 40)
+
(Merchant Concentration × 20)
```

Risk Levels:

```text
0 - 20     Low
21 - 50    Medium
51 - 75    High
76 - 100   Critical
```

---
## Dashboard Endpoint

```bash
curl GET /risk/reports/dashboard
```
```text
Provides a consolidated fraud risk overview including:

• Total Transactions
• Failed Transactions
• High Risk Transactions
• Risk Score
• Risk Level
• Top Risk Merchant
```

---

# Future Enhancements

* pgvector-based similarity search
* Embedding-based RAG
* Fraud trend dashboard
* Merchant risk scoring
* Real-time alerting
* Multi-model AI support
