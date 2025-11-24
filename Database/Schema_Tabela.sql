-- =============================================
-- ESQUEMA (SCHEMA)
-- =============================================
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
SET search_path TO public;

-- =============================================
-- TABELAS (TABLES)
-- =============================================

CREATE TABLE produtor (
    id_produtor     SERIAL PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    email           VARCHAR(150) UNIQUE NOT NULL,
    senha_hash      VARCHAR(255) NOT NULL,
    telefone        VARCHAR(30),
    criado_em       TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE administrador (
    id_administrador SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    nivel_acesso VARCHAR(50) NOT NULL, 
    senha_hash VARCHAR(255) NOT NULL, 
    criado_em TIMESTAMP DEFAULT current_timestamp 
);


CREATE TABLE administrador_produtor (
    id_administrador INT NOT NULL REFERENCES administrador(id_administrador) ON DELETE CASCADE,
    id_produtor INT NOT NULL REFERENCES produtor(id_produtor) ON DELETE CASCADE,
    data_atribuicao TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY (id_administrador, id_produtor)
);


CREATE TABLE log_administrador (
    id_log BIGSERIAL PRIMARY KEY,
    id_administrador INT NOT NULL REFERENCES administrador(id_administrador) ON DELETE CASCADE,
    acao VARCHAR(100) NOT NULL, 
    descricao TEXT,
    data_hora TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE coleira(
    id_coleira      SERIAL PRIMARY KEY,
    serial          VARCHAR(100) UNIQUE NOT NULL,
    modelo          VARCHAR(80),
    status          VARCHAR(20) DEFAULT 'ativa',
    bateria_percent SMALLINT CHECK (bateria_percent >= 0 AND bateria_percent <= 100),
    ultimo_sync     TIMESTAMP,
    criado_em       TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE vaca (
    id_vaca             SERIAL PRIMARY KEY,
    id_produtor         INT NOT NULL REFERENCES produtor(id_produtor) ON DELETE CASCADE,
    id_coleira          INT REFERENCES coleira(id_coleira) ON DELETE SET NULL,
    identificador       VARCHAR(80) NOT NULL,
    raca                VARCHAR(60),
    data_nascimento     DATE,
    estado_reprodutivo  VARCHAR(20) DEFAULT 'normal',
    observacoes         TEXT,
    criado_em           TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE leitura_sensor (
    id_leitura          BIGSERIAL PRIMARY KEY,
    id_vaca             INT NOT NULL REFERENCES vaca(id_vaca) ON DELETE CASCADE,
    data_hora           TIMESTAMP NOT NULL,
    temperatura         NUMERIC(4,2) NOT NULL,
    atividade           INT NOT NULL,
    tempo_ruminacao_min INT,
    bateria_percent     SMALLINT CHECK (bateria_percent >= 0 AND bateria_percent <= 100),
    origem              VARCHAR(50),
    criado_em           TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE alerta(
    id_alerta           BIGSERIAL PRIMARY KEY,
    id_vaca             INT NOT NULL REFERENCES vaca(id_vaca) ON DELETE CASCADE, 
    tipo_alerta         VARCHAR(80) NOT NULL,
    descricao           TEXT,
    nivel               VARCHAR(20) DEFAULT 'info',
    gerado_em           TIMESTAMP DEFAULT current_timestamp,
    resolvido           BOOLEAN DEFAULT FALSE,
    resolvido_por       INT REFERENCES administrador(id_administrador) ON DELETE SET NULL, 
    resolvido_em        TIMESTAMP 
);

CREATE TABLE log_alerta (
    id_log          SERIAL PRIMARY KEY,
    id_alerta       BIGINT REFERENCES alerta(id_alerta) ON DELETE SET NULL,
    acao            VARCHAR(20),
    data            TIMESTAMP DEFAULT current_timestamp
);
