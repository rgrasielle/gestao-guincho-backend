CREATE TABLE valores_fixos (
    id BIGSERIAL PRIMARY KEY,
    valor_quilometragem_por_km DECIMAL(10,2),
    valor_quilometragem_saida DECIMAL(10,2),
    valor_motorista_por_km DECIMAL(10,2),
    valor_motorista_saida DECIMAL(10,2),
    valor_hora_parada DECIMAL(10,2),
    valor_hora_trabalhada DECIMAL(10,2),
    valor_diaria DECIMAL(10,2)
);