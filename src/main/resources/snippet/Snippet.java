package snippet;

public class Snippet {
	# =============================================
	# CONFIGURAÇÃO DO SERVIDOR
	# =============================================
	server.port=8080
	spring.application.name=TechMuu
	
	# =============================================
	# CONFIGURAÇÃO DO BANCO DE DADOS (SUPABASE)
	# =============================================
	spring.datasource.url=jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:5432/postgres
	spring.datasource.username=postgres.htmlscrodztrotbpxywih
	spring.datasource.password=linhaperola123.
	spring.datasource.driver-class-name=org.postgresql.Driver
	
	# =============================================
	# JPA / HIBERNATE
	# =============================================
	spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
	spring.jpa.show-sql=true
	spring.jpa.hibernate.ddl-auto=none
	spring.jpa.properties.hibernate.format_sql=true
	
	# =============================================
	# CORS (Permitir requisições do front-end)
	# =============================================
	spring.web.cors.allowed-origins=http://localhost:3000,http://127.0.0.1:5500
	spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
	spring.web.cors.allowed-headers=*
	spring.web.cors.allow-credentials=true
	
	# =============================================
	# SEGURANÇA (Desabilitar temporariamente)
	# =============================================
	spring.security.user.name=admin
	spring.security.user.password=admin123
	
	# =============================================
	# LOGS
	# =============================================
	logging.level.root=INFO
	logging.level.testeMaven.com.techmuu=DEBUG
	logging.level.org.springframework.web=DEBUG
	
	# =============================================
	# JSON (Formato de data)
	# =============================================
	spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
	spring.jackson.time-zone=America/Sao_Paulo
}

