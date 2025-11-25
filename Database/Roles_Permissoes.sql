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

