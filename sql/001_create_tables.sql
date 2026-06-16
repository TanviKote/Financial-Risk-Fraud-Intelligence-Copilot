CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    merchant_id VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(10) DEFAULT 'USD',
    status VARCHAR(30) NOT NULL CHECK (status IN ('APPROVED', 'FAILED', 'REVIEW')),
    risk_flag VARCHAR(30) CHECK (risk_flag IS NULL OR risk_flag IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    failure_reason VARCHAR(100),
    event_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transactions_event_time ON transactions(event_time);
CREATE INDEX IF NOT EXISTS idx_transactions_merchant_id ON transactions(merchant_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status_risk ON transactions(status, risk_flag);

CREATE TABLE IF NOT EXISTS daily_risk_metrics (
    metric_id SERIAL PRIMARY KEY,
    metric_date DATE NOT NULL UNIQUE,
    total_transactions INT DEFAULT 0,
    failed_transactions INT DEFAULT 0,
    high_risk_transactions INT DEFAULT 0,
    total_transaction_amount NUMERIC(14,2) DEFAULT 0,
    failed_transaction_amount NUMERIC(14,2) DEFAULT 0,
    top_risk_merchant VARCHAR(50),
    most_common_failure_reason VARCHAR(100),
    risk_score NUMERIC(5,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS risk_reports (
    report_id SERIAL PRIMARY KEY,
    report_date DATE NOT NULL,
    report_type VARCHAR(50),
    risk_context TEXT,
    llm_summary TEXT,
    recommendation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS risk_report_embeddings (
    embedding_id SERIAL PRIMARY KEY,
    report_id INT REFERENCES risk_reports(report_id) ON DELETE CASCADE,
    embedding VECTOR(1536),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_risk_reports_report_date ON risk_reports(report_date);
-- Optional after enough data exists:
-- CREATE INDEX risk_report_embedding_idx ON risk_report_embeddings USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
