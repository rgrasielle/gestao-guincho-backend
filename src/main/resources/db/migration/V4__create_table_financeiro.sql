CREATE TABLE financeiro (
    id BIGSERIAL PRIMARY KEY,
    chamado_id BIGINT NOT NULL,
    total NUMERIC(10,2) DEFAULT 0,
    observacao TEXT,
    CONSTRAINT fk_financeiro_chamado FOREIGN KEY (chamado_id) REFERENCES chamado (id)
);
