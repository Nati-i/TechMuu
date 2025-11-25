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
