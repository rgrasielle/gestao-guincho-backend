-- Cria enum para disponibilidade se não existir
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'disponibilidade_enum') THEN
      CREATE TYPE disponibilidade_enum AS ENUM ('DISPONIVEL', 'EM_ATENDIMENTO', 'INDISPONIVEL');
   END IF;
END$$;

-- Cria tabela guincho
CREATE TABLE guincho (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(20) NOT NULL UNIQUE,
    tipo VARCHAR(50),
    capacidade DECIMAL(10,2),
    disponibilidade VARCHAR(50) NOT NULL DEFAULT 'DISPONIVEL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
