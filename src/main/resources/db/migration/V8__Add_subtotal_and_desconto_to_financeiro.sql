-- Migration para adicionar campos de subtotal e desconto na tabela financeiro

ALTER TABLE financeiro
ADD COLUMN subtotal NUMERIC(10, 2) NOT NULL DEFAULT 0;

ALTER TABLE financeiro
ADD COLUMN desconto NUMERIC(10, 2) NOT NULL DEFAULT 0;