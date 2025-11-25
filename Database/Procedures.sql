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
