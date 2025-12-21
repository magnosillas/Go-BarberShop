-- ==========================================
-- 🐘 GoBarber Database Initialization Script
-- ==========================================

-- Criar o schema se não existir
CREATE SCHEMA IF NOT EXISTS gobarber;

-- Conceder permissões ao usuário
GRANT ALL PRIVILEGES ON SCHEMA gobarber TO gobarber;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA gobarber TO gobarber;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA gobarber TO gobarber;

-- Definir schema padrão para novas conexões
ALTER DATABASE gobarber SET search_path TO gobarber, public;

-- Mensagem de confirmação
DO $$
BEGIN
    RAISE NOTICE '✅ GoBarber database initialized successfully!';
    RAISE NOTICE '📂 Schema: gobarber';
    RAISE NOTICE '👤 User: gobarber';
END $$;
