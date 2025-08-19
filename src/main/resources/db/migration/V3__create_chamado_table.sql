CREATE TABLE chamado (
  id BIGSERIAL PRIMARY KEY,

  -- Dados do serviço
  seguradora VARCHAR(150),
  sinistro VARCHAR(100),
  data_acionamento DATE,
  hora TIME,
  tipo_servico VARCHAR(50),
  guincho_id BIGINT,
  motorista_id BIGINT,

  -- Origem
  origem_cep VARCHAR(20),
  origem_cidade VARCHAR(100),
  origem_estado VARCHAR(50),
  origem_bairro VARCHAR(100),
  origem_logradouro VARCHAR(150),
  origem_numero VARCHAR(20),

  -- Destino
  destino_cep VARCHAR(20),
  destino_cidade VARCHAR(100),
  destino_estado VARCHAR(50),
  destino_bairro VARCHAR(100),
  destino_logradouro VARCHAR(150),
  destino_numero VARCHAR(20),

  -- Dados do veículo
  veiculo_modelo VARCHAR(100),
  veiculo_ano INT,
  veiculo_cor VARCHAR(50),
  veiculo_placa VARCHAR(20),
  veiculo_observacoes TEXT,

  -- Dados do cliente
  cliente_nome VARCHAR(150),
  cliente_cpf_cnpj VARCHAR(30),
  cliente_telefone VARCHAR(30),
  cliente_email VARCHAR(100),
  cliente_solicitante VARCHAR(150),

  -- Observações gerais
  observacoes TEXT,

  -- Status e datas
  status VARCHAR(30) NOT NULL DEFAULT 'ABERTO',
  data_abertura TIMESTAMPTZ DEFAULT now(),
  data_fechamento TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),

  -- Chaves estrangeiras (FK)
  CONSTRAINT fk_guincho FOREIGN KEY (guincho_id) REFERENCES guincho(id),
  CONSTRAINT fk_motorista FOREIGN KEY (motorista_id) REFERENCES motorista(id)
);
