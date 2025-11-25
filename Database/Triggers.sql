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
