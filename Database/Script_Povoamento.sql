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
