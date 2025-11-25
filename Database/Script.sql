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

-- =============================================
-- ÍNDICES (INDEXES)
-- =============================================

CREATE INDEX idx_vaca_id_produtor ON vaca(id_produtor);
CREATE INDEX idx_vaca_id_coleira ON vaca(id_coleira);
CREATE INDEX idx_leitura_sensor_id_vaca ON leitura_sensor(id_vaca);
CREATE INDEX idx_alerta_id_vaca ON alerta(id_vaca);
CREATE INDEX idx_administrador_produtor_admin ON administrador_produtor(id_administrador);
CREATE INDEX idx_administrador_produtor_prod ON administrador_produtor(id_produtor);
CREATE INDEX idx_log_administrador_id ON log_administrador(id_administrador);

-- =============================================
-- VIEWS
-- =============================================

CREATE OR REPLACE VIEW vw_vaca_detalhes AS 
SELECT
    v.id_vaca,
    v.identificador,
    v.raca,
    v.estado_reprodutivo,
    p.nome AS produtor,
    c.serial AS coleira,
    c.status AS status_coleira
FROM vaca v 
JOIN produtor p ON v.id_produtor = p.id_produtor
LEFT JOIN coleira c ON v.id_coleira = c.id_coleira;

CREATE OR REPLACE VIEW vw_alertas_recentes AS 
SELECT
    v.identificador,
    a.tipo_alerta,
    a.descricao,
    a.nivel,
    a.gerado_em,
    a.resolvido,
    adm.nome AS resolvido_por
FROM alerta a 
JOIN vaca v ON a.id_vaca = v.id_vaca
LEFT JOIN administrador adm ON a.resolvido_por = adm.id_administrador
WHERE a.gerado_em > (now() - INTERVAL '7 days');

-- NOVA VIEW: Administradores e seus produtores
CREATE OR REPLACE VIEW vw_administrador_produtores AS
SELECT
    a.id_administrador,
    a.nome AS administrador,
    a.nivel_acesso,
    p.id_produtor,
    p.nome AS produtor,
    ap.data_atribuicao
FROM administrador a
LEFT JOIN administrador_produtor ap ON a.id_administrador = ap.id_administrador
LEFT JOIN produtor p ON ap.id_produtor = p.id_produtor;

-- =============================================
-- USUÁRIOS E PERMISSÕES (ROLES & PERMISSIONS)
-- =============================================

DROP ROLE IF EXISTS gestor_produtores;
DROP ROLE IF EXISTS tecnico_suporte;
DROP USER IF EXISTS admin_sistema;
DROP USER IF EXISTS user_produtor; 

CREATE ROLE gestor_produtores;
CREATE ROLE tecnico_suporte;

CREATE USER admin_sistema WITH PASSWORD '23032004';
CREATE USER user_produtor WITH PASSWORD '23032004';

GRANT gestor_produtores TO user_produtor;
GRANT tecnico_suporte TO admin_sistema;

GRANT SELECT ON vaca, leitura_sensor TO gestor_produtores;
GRANT INSERT ON leitura_sensor TO gestor_produtores;
GRANT INSERT ON vaca TO gestor_produtores;
GRANT UPDATE (raca, data_nascimento, estado_reprodutivo, observacoes) ON vaca TO gestor_produtores;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin_sistema;

-- =============================================
-- FUNÇÕES E TRIGGERS (FUNCTIONS & TRIGGERS)
-- =============================================

CREATE OR REPLACE FUNCTION fn_log_alerta()
RETURNS TRIGGER
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN 
    INSERT INTO log_alerta(id_alerta, acao)
    VALUES (COALESCE(NEW.id_alerta, OLD.id_alerta), TG_OP);
   RETURN NEW;
END;
$$;

CREATE TRIGGER tg_log_alerta 
AFTER INSERT OR UPDATE OR DELETE ON alerta
FOR EACH ROW
EXECUTE FUNCTION fn_log_alerta();

CREATE OR REPLACE FUNCTION fn_verifica_temperatura()
RETURNS TRIGGER
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN 
    IF NEW.temperatura > 39 THEN 
        INSERT INTO alerta (id_vaca, tipo_alerta, descricao, nivel)
        VALUES (NEW.id_vaca, 'temperatura alta', 'temperatura acima do normal', 'alto');
    END IF;
    RETURN NEW;
END;
$$;
 
CREATE TRIGGER tg_verifica_temperatura 
AFTER INSERT ON leitura_sensor
FOR EACH ROW
EXECUTE FUNCTION fn_verifica_temperatura();

CREATE OR REPLACE FUNCTION fn_log_acao_administrador()
RETURNS TRIGGER
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN

    IF NEW.resolvido = TRUE AND (OLD.resolvido IS NULL OR OLD.resolvido = FALSE) AND NEW.resolvido_por IS NOT NULL THEN
        INSERT INTO log_administrador (id_administrador, acao, descricao)
        VALUES (
            NEW.resolvido_por,
            'resolveu_alerta',
            'Resolveu alerta ID: ' || NEW.id_alerta || ' - ' || NEW.tipo_alerta
        );
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER tg_log_acao_administrador
AFTER UPDATE ON alerta
FOR EACH ROW
WHEN (NEW.resolvido = TRUE)
EXECUTE FUNCTION fn_log_acao_administrador();

-- =============================================
-- PROCEDURES (Stored Procedures)
-- =============================================

CREATE OR REPLACE PROCEDURE atualiza_estado_reprodutivo(p_id_vaca INT, p_estado VARCHAR)
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN
    UPDATE vaca SET estado_reprodutivo = p_estado WHERE id_vaca = p_id_vaca;
END;
$$;

CREATE OR REPLACE PROCEDURE relatorio_producao()
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN
    RAISE NOTICE 'Relatorio de produção executado com sucesso.';
END;
$$;

-- NOVA PROCEDURE: Atribuir produtor a administrador
CREATE OR REPLACE PROCEDURE atribuir_produtor_a_administrador(p_id_admin INT, p_id_produtor INT)
LANGUAGE plpgsql
SET search_path = public, pg_temp
AS $$
BEGIN
    INSERT INTO administrador_produtor (id_administrador, id_produtor)
    VALUES (p_id_admin, p_id_produtor)
    ON CONFLICT (id_administrador, id_produtor) DO NOTHING;
    
    RAISE NOTICE 'Produtor % atribuído ao administrador %', p_id_produtor, p_id_admin;
END;
$$;

-- =================================
-- SCRIPT DE POVOAMENTO
-- =================================
DO $$
DECLARE
    v_id_produtor INT;
    v_id_coleira INT;
    v_bateria INT;
    v_status_coleira VARCHAR;
    v_raca VARCHAR;
    v_estado_reprodutivo VARCHAR;
    v_qtd_vacas INT;
    v_id_admin1 INT;
    v_id_admin2 INT;
    v_id_admin3 INT;
BEGIN
    -- ===== INSERIR ADMINISTRADORES =====
    RAISE NOTICE '=== CRIANDO ADMINISTRADORES ===';
    
    INSERT INTO administrador (nome, email, telefone, nivel_acesso, senha_hash)
    VALUES ('Carlos Silva - Admin Master', 'carlos.silva@techmuu.com', '(11) 98765-4321', 'alto', 'hash_admin_master123')
    RETURNING id_administrador INTO v_id_admin1;
    
    INSERT INTO administrador (nome, email, telefone, nivel_acesso, senha_hash)
    VALUES ('Ana Santos - Suporte Técnico', 'ana.santos@techmuu.com', '(11) 91234-5678', 'medio', 'hash_admin_medio456')
    RETURNING id_administrador INTO v_id_admin2;
    
    INSERT INTO administrador (nome, email, telefone, nivel_acesso, senha_hash)
    VALUES ('Pedro Costa - Assistente', 'pedro.costa@techmuu.com', '(11) 99876-5432', 'baixo', 'hash_admin_baixo789')
    RETURNING id_administrador INTO v_id_admin3;
    
    RAISE NOTICE '✓ 3 Administradores criados';

    -- ===== INSERIR PRODUTORES E VACAS =====
    RAISE NOTICE '=== CRIANDO PRODUTORES E VACAS ===';
    
    FOR i IN 1..10 LOOP
        INSERT INTO produtor (nome, email, senha_hash, telefone)
        VALUES (
            'Produtor ' || (ARRAY['Silva', 'Santos', 'Oliveira', 'Souza', 'Lima'])[floor(random()*5+1)] || ' ' || i,
            'produtor' || i || '@fazenda.demo',
            'hash_falso_senha' || i,
            '49' || (floor(random() * 90000000 + 10000000)::BIGINT)::TEXT
        )
        RETURNING id_produtor INTO v_id_produtor;

        -- Atribuir produtores aos administradores
        IF i <= 4 THEN
            INSERT INTO administrador_produtor (id_administrador, id_produtor)
            VALUES (v_id_admin1, v_id_produtor);
        ELSIF i <= 7 THEN
            INSERT INTO administrador_produtor (id_administrador, id_produtor)
            VALUES (v_id_admin2, v_id_produtor);
        ELSE
            INSERT INTO administrador_produtor (id_administrador, id_produtor)
            VALUES (v_id_admin3, v_id_produtor);
        END IF;

        v_qtd_vacas := floor(random() * 31 + 70)::INT;
        RAISE NOTICE '✓ Criando % vacas para Produtor %', v_qtd_vacas, v_id_produtor;

        FOR j IN 1..v_qtd_vacas LOOP
            IF i >= 9 THEN
                v_bateria := 0;
                v_status_coleira := 'inativa';
            ELSE
                v_bateria := floor(random() * 61 + 40)::INT;
                v_status_coleira := 'ativa';
            END IF;

            INSERT INTO coleira (serial, modelo, status, bateria_percent, ultimo_sync)
            VALUES (
                'SN-' || LPAD(v_id_produtor::text, 3, '0') || '-' || LPAD(j::text, 4, '0') || (ARRAY['A','B','C'])[floor(random()*3+1)],
                'SmartBov V' || (floor(random()*2)+1)::text,
                v_status_coleira,
                v_bateria,
                NOW() - (floor(random() * 300) || ' minutes')::interval
            )
            RETURNING id_coleira INTO v_id_coleira;

            v_raca := (ARRAY['Holandesa', 'Jersey', 'Girolando', 'Nelore'])[floor(random()*4+1)];
            v_estado_reprodutivo := (ARRAY['lactante', 'seca', 'prenhe', 'normal'])[floor(random()*4+1)];

            INSERT INTO vaca (id_produtor, id_coleira, identificador, raca, data_nascimento, estado_reprodutivo)
            VALUES (
                v_id_produtor,
                v_id_coleira,
                'BR-' || (floor(random()*900000+100000)::INT)::TEXT,
                v_raca,
                CURRENT_DATE - (floor(random() * 365 * 8) + 700 || ' days')::interval,
                v_estado_reprodutivo
            );
        END LOOP;
    END LOOP;
    
    RAISE NOTICE '=== POVOAMENTO CONCLUÍDO COM SUCESSO ===';
END $$;
