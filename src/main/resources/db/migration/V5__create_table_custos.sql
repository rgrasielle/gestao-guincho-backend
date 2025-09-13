CREATE TABLE custo_quilometragem (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    quilometros_rodados DECIMAL(10,2),
    valor_por_km DECIMAL(10,2),
    quantidade_saida DECIMAL(10,2),
    valor_saida DECIMAL(10,2),
    total DECIMAL(10,2),

    CONSTRAINT fk_custo_quilometragem_financeiro
        FOREIGN KEY (financeiro_id)
        REFERENCES financeiro(id)
        ON DELETE CASCADE
);

CREATE TABLE custo_motorista (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    quilometros_rodados DECIMAL(10,2),
    valor_por_km DECIMAL(10,2),
    quantidade_saida DECIMAL(10,2),
    valor_saida DECIMAL(10,2),
    total DECIMAL(10,2),
    CONSTRAINT fk_motorista_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_pedagio (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    quantidade INT,
    valor DECIMAL(10,2),
    total DECIMAL(10,2),
    CONSTRAINT fk_pedagio_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_patins (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    descricao VARCHAR(255) ,
    valor DECIMAL(10,2),
    CONSTRAINT fk_patins_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_hora_parada (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    horas DECIMAL(10,2),
    valor_hora_parada DECIMAL(10,2),
    valor_total DECIMAL(10,2),
    CONSTRAINT fk_hora_parada_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_hora_trabalhada (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    horas DECIMAL(10,2),
    valor_hora_trabalhada DECIMAL(10,2),
    valor_total DECIMAL(10,2),
    CONSTRAINT fk_hora_trabalhada_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_diaria (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    entrada DATE,
    saida DATE,
    estadia INT,
    valor_por_dia DECIMAL(10,2),
    valor_total DECIMAL(10,2),
    CONSTRAINT fk_diaria_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_roda_extra (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    descricao VARCHAR(255),
    valor DECIMAL(10,2),
    CONSTRAINT fk_roda_extra_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_excedente (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    excedentes DECIMAL(10,2),
    observacao TEXT,
    CONSTRAINT fk_excedente_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);


