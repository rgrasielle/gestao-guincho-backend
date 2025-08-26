CREATE TABLE custo_quilometragem (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    quilometros_rodados DECIMAL(10,2) NOT NULL,
    valor_por_km DECIMAL(10,2) NOT NULL,
    km_saida DECIMAL(10,2) NOT NULL,
    valor_saida DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_custo_quilometragem_financeiro
        FOREIGN KEY (financeiro_id)
        REFERENCES financeiro(id)
        ON DELETE CASCADE
);

CREATE TABLE custo_motorista (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    quilometros_rodados DECIMAL(10,2) NOT NULL,
    valor_por_km DECIMAL(10,2) NOT NULL,
    km_saida DECIMAL(10,2) NOT NULL,
    valor_saida DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_motorista_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_pedagio (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    sinistro VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_pedagio_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_patins (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    sinistro VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_patins_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_hora_parada (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    horas DECIMAL(10,2) NOT NULL,
    valor_hora_parada DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_hora_parada_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_hora_trabalhada (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    horas DECIMAL(10,2) NOT NULL,
    valor_hora_trabalhada DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_hora_trabalhada_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_diaria (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    sinistro VARCHAR(255) NOT NULL,
    entrada DATE NOT NULL,
    saida DATE NOT NULL,
    estadia INT NOT NULL,
    valor_por_dia DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_diaria_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_roda_extra (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    sinistro VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_roda_extra_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);

CREATE TABLE custo_excedente (
    id BIGSERIAL PRIMARY KEY,
    financeiro_id BIGINT NOT NULL,
    excedentes DECIMAL(10,2) NOT NULL,
    observacao TEXT,
    CONSTRAINT fk_excedente_financeiro FOREIGN KEY (financeiro_id)
        REFERENCES financeiro (id)
        ON DELETE CASCADE
);


