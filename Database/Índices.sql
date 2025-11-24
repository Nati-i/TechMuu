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
