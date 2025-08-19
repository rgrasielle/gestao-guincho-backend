-- Cria tabela motorista
CREATE TABLE motorista (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cnh VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    disponibilidade VARCHAR(50) NOT NULL DEFAULT 'DISPONIVEL'
);
