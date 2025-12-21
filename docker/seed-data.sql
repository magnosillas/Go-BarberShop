-- ==========================================
-- 🌱 GoBarber Database Seeder
-- ==========================================
-- Este script popula o banco de dados com dados de exemplo
-- Execute após as migrations do Liquibase
-- ==========================================

SET search_path TO gobarber;

-- ==========================================
-- 🔧 LIMPAR DADOS EXISTENTES (opcional)
-- ==========================================
-- Descomente as linhas abaixo se quiser resetar os dados
-- TRUNCATE appointment_service, appointment, barber_x_service, service_x_product CASCADE;
-- TRUNCATE secretary, barber, employee, role CASCADE;
-- TRUNCATE product_stock, product, service, address, sale CASCADE;

-- ==========================================
-- 🏠 ENDEREÇOS (ADDRESS)
-- ==========================================
INSERT INTO address (id_adress, street, number, neighborhood, city, state, cep) VALUES
(1, 'Rua das Flores', 123, 'Centro', 'Recife', 'PE', '5001000'),
(2, 'Av. Boa Viagem', 456, 'Boa Viagem', 'Recife', 'PE', '5102010'),
(3, 'Rua do Sol', 789, 'Santo Antônio', 'Recife', 'PE', '5001020'),
(4, 'Av. Conselheiro Aguiar', 1010, 'Boa Viagem', 'Recife', 'PE', '5102102'),
(5, 'Rua da Aurora', 200, 'Boa Vista', 'Recife', 'PE', '5005000'),
(6, 'Av. Caxangá', 3500, 'Iputinga', 'Recife', 'PE', '5067000'),
(7, 'Rua do Hospício', 88, 'Boa Vista', 'Recife', 'PE', '5006008'),
(8, 'Av. Domingos Ferreira', 1500, 'Boa Viagem', 'Recife', 'PE', '5101105'),
(9, 'Rua Gervásio Pires', 300, 'Boa Vista', 'Recife', 'PE', '5005007'),
(10, 'Av. Norte', 2000, 'Casa Amarela', 'Recife', 'PE', '5207000')
ON CONFLICT (id_adress) DO NOTHING;

-- ==========================================
-- 👤 ROLES (já inseridos via migration, mas garantindo)
-- ==========================================
INSERT INTO role (name_role) 
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ROLE_ADMIN');
INSERT INTO role (name_role) 
SELECT 'ROLE_BARBER' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ROLE_BARBER');
INSERT INTO role (name_role) 
SELECT 'ROLE_SECRETARY' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ROLE_SECRETARY');
INSERT INTO role (name_role) 
SELECT 'ROLE_CLIENT' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ROLE_CLIENT');

-- ==========================================
-- 👤 USUÁRIOS (EMPLOYEE)
-- ==========================================
-- IMPORTANTE: Hash BCrypt válido para senha "password"
-- Use 'password' como senha para login em todos os usuários
INSERT INTO employee (id_user, email, password, id_role) VALUES
(1, 'admin@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 1),     -- ADMIN
(2, 'carlos.barbeiro@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 2),  -- BARBER
(3, 'joao.barbeiro@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 2),    -- BARBER
(4, 'pedro.barbeiro@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 2),   -- BARBER
(5, 'lucas.barbeiro@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 2),   -- BARBER
(6, 'ana.secretaria@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 3),   -- SECRETARY
(7, 'maria.secretaria@gobarber.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 3)  -- SECRETARY
ON CONFLICT (id_user) DO NOTHING;

-- Atualiza a sequência do EMPLOYEE para evitar conflitos
SELECT setval('employee_id_user_seq', COALESCE((SELECT MAX(id_user) FROM employee), 1));

-- ==========================================
-- ✂️ SERVIÇOS (SERVICE)
-- ==========================================
INSERT INTO service (id_service, name_service, description_service, price_service, time_service) VALUES
(1, 'Corte Masculino', 'Corte tradicional masculino com máquina e tesoura', 35.00, '00:30:00'),
(2, 'Corte Degradê', 'Corte degradê americano ou europeu', 45.00, '00:45:00'),
(3, 'Barba Completa', 'Barba com navalha, toalha quente e hidratação', 30.00, '00:30:00'),
(4, 'Corte + Barba', 'Combo de corte masculino com barba completa', 55.00, '01:00:00'),
(5, 'Pigmentação', 'Pigmentação capilar para disfarçar falhas', 80.00, '01:30:00'),
(6, 'Sobrancelha', 'Design de sobrancelha masculina', 15.00, '00:15:00'),
(7, 'Hidratação Capilar', 'Tratamento de hidratação profunda', 40.00, '00:30:00'),
(8, 'Relaxamento', 'Relaxamento capilar para cabelos crespos', 60.00, '01:00:00'),
(9, 'Platinado', 'Descoloração e platinado completo', 120.00, '02:00:00'),
(10, 'Corte Infantil', 'Corte especial para crianças até 12 anos', 25.00, '00:20:00'),
(11, 'Nevou', 'Efeito nevou no cabelo', 50.00, '00:45:00'),
(12, 'Luzes', 'Mechas e luzes no cabelo', 100.00, '01:30:00')
ON CONFLICT (id_service) DO NOTHING;

-- Atualiza a sequência do SERVICE
SELECT setval('service_id_service_seq', COALESCE((SELECT MAX(id_service) FROM service), 1));

-- ==========================================
-- 💈 BARBEIROS (BARBER)
-- ==========================================
INSERT INTO barber (id_barber, name_barber, cpf_barber, id_adress, salary, admission_date, workload, id_user, contact, start_working, end_working) VALUES
(1, 'Carlos Silva', '12345678901', 1, 3500.00, '2023-01-15', 44, 2, '81999990001', '08:00:00', '18:00:00'),
(2, 'João Santos', '12345678902', 2, 3200.00, '2023-03-20', 44, 3, '81999990002', '09:00:00', '19:00:00'),
(3, 'Pedro Oliveira', '12345678903', 3, 3000.00, '2023-06-10', 40, 4, '81999990003', '08:00:00', '17:00:00'),
(4, 'Lucas Ferreira', '12345678904', 4, 2800.00, '2024-01-05', 44, 5, '81999990004', '10:00:00', '20:00:00')
ON CONFLICT (id_barber) DO NOTHING;

-- Atualiza a sequência do BARBER
SELECT setval('barber_id_barber_seq', COALESCE((SELECT MAX(id_barber) FROM barber), 1));

-- ==========================================
-- 👩‍💼 SECRETÁRIAS (SECRETARY)
-- ==========================================
INSERT INTO secretary (id_secretary, name_secretary, cpf_secretary, id_adress, id_user, salary, admission_date, workload, contact, start_working, end_working) VALUES
(1, 'Ana Costa', '98765432101', 5, 6, 2500.00, '2023-02-01', 44, '81988880001', '08:00:00', '17:00:00'),
(2, 'Maria Souza', '98765432102', 6, 7, 2500.00, '2023-08-15', 44, '81988880002', '12:00:00', '21:00:00')
ON CONFLICT (id_secretary) DO NOTHING;

-- Atualiza a sequência do SECRETARY
SELECT setval('secretary_id_secretary_seq', COALESCE((SELECT MAX(id_secretary) FROM secretary), 1));

-- ==========================================
-- 💈🔗✂️ BARBEIRO x SERVIÇO (BARBER_X_SERVICE)
-- ==========================================
-- Carlos Silva (id=1) - Especialista em cortes e barba
INSERT INTO barber_x_service (id_barber, id_service) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 6), (1, 10);

-- João Santos (id=2) - Especialista em coloração
INSERT INTO barber_x_service (id_barber, id_service) VALUES
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 9), (2, 11), (2, 12);

-- Pedro Oliveira (id=3) - Generalista
INSERT INTO barber_x_service (id_barber, id_service) VALUES
(3, 1), (3, 2), (3, 3), (3, 4), (3, 6), (3, 7), (3, 10);

-- Lucas Ferreira (id=4) - Especialista em tratamentos
INSERT INTO barber_x_service (id_barber, id_service) VALUES
(4, 1), (4, 2), (4, 3), (4, 4), (4, 7), (4, 8), (4, 5);

-- ==========================================
-- 📦 PRODUTOS (PRODUCT)
-- ==========================================
INSERT INTO product (id_product, name_product, brand_product, price_product, size, description) VALUES
(1, 'Pomada Modeladora Matte', 'Fox For Men', 45.00, '150g', 'Pomada efeito matte para fixação média'),
(2, 'Pomada Modeladora Brilho', 'QOD', 55.00, '150g', 'Pomada com brilho para fixação forte'),
(3, 'Cera Capilar', 'Bozzano', 35.00, '100g', 'Cera modeladora para cabelos curtos'),
(4, 'Gel Fixador Forte', 'Duty', 25.00, '300g', 'Gel de fixação forte e duradoura'),
(5, 'Óleo para Barba', 'Souvie', 60.00, '30ml', 'Óleo hidratante e modelador para barba'),
(6, 'Balm para Barba', 'Viking', 50.00, '60g', 'Balm hidratante com fixação leve'),
(7, 'Shampoo Anticaspa', 'Clear Men', 28.00, '400ml', 'Shampoo anticaspa para uso diário'),
(8, 'Shampoo para Barba', 'Barba de Respeito', 40.00, '200ml', 'Shampoo específico para barba'),
(9, 'Condicionador Masculino', 'Tresemmé', 22.00, '400ml', 'Condicionador para cabelos masculinos'),
(10, 'Pós-Barba', 'Nivea', 30.00, '100ml', 'Loção pós-barba refrescante'),
(11, 'Talco Mentolado', 'Granado', 18.00, '100g', 'Talco refrescante pós-corte'),
(12, 'Spray Fixador', 'Got2B', 35.00, '300ml', 'Spray de fixação extra forte'),
(13, 'Tônico Capilar', 'Minancora', 25.00, '150ml', 'Tônico para fortalecimento capilar'),
(14, 'Desodorante Roll-on', 'Rexona Men', 15.00, '50ml', 'Desodorante antitranspirante'),
(15, 'Sabonete Líquido', 'Dove Men', 20.00, '250ml', 'Sabonete líquido masculino')
ON CONFLICT (id_product) DO NOTHING;

-- Atualiza a sequência do PRODUCT
SELECT setval('product_id_product_seq', COALESCE((SELECT MAX(id_product) FROM product), 1));

-- ==========================================
-- 📦 ESTOQUE (PRODUCT_STOCK)
-- ==========================================
INSERT INTO product_stock (id_product, batch_number, quantity, expiration_date, acquisition_date) VALUES
(1, 'LOTE2024-001', 25, '2026-06-15', '2024-06-15'),
(2, 'LOTE2024-002', 20, '2026-06-15', '2024-06-15'),
(3, 'LOTE2024-003', 15, '2026-03-20', '2024-03-20'),
(4, 'LOTE2024-004', 30, '2025-12-31', '2024-01-15'),
(5, 'LOTE2024-005', 18, '2026-08-10', '2024-08-10'),
(6, 'LOTE2024-006', 12, '2026-08-10', '2024-08-10'),
(7, 'LOTE2024-007', 40, '2025-09-30', '2024-03-30'),
(8, 'LOTE2024-008', 22, '2026-05-15', '2024-05-15'),
(9, 'LOTE2024-009', 35, '2025-11-20', '2024-05-20'),
(10, 'LOTE2024-010', 28, '2026-02-28', '2024-08-28'),
(11, 'LOTE2024-011', 50, '2027-01-15', '2024-07-15'),
(12, 'LOTE2024-012', 15, '2025-10-10', '2024-04-10'),
(13, 'LOTE2024-013', 20, '2026-04-05', '2024-04-05'),
(14, 'LOTE2024-014', 45, '2025-12-15', '2024-06-15'),
(15, 'LOTE2024-015', 30, '2026-01-20', '2024-07-20');

-- Segundo lote para alguns produtos (simular múltiplos lotes)
INSERT INTO product_stock (id_product, batch_number, quantity, expiration_date, acquisition_date) VALUES
(1, 'LOTE2024-016', 10, '2026-12-15', '2024-12-01'),
(4, 'LOTE2024-017', 20, '2026-06-30', '2024-12-01'),
(7, 'LOTE2024-018', 25, '2026-03-30', '2024-12-01');

-- ==========================================
-- 💰 PROMOÇÕES (SALE)
-- ==========================================
-- Nota: A migration já insere um registro com id=1, então começamos do id=2
-- ou usamos ON CONFLICT para evitar duplicatas
DELETE FROM sale WHERE id_sale > 1; -- Remove dados antigos do seeder, mantém o da migration

INSERT INTO sale (id_sale, sale_name, total_price, discount_coupon, start_date, end_date) VALUES
(2, 'Promoção de Natal', 49.99, 'NATAL24', '2024-12-01', '2024-12-31'),
(3, 'Black Friday', 39.99, 'BLACK24', '2024-11-25', '2024-11-30'),
(4, 'Dia dos Pais', 44.99, 'PAPAI24', '2024-08-01', '2024-08-11'),
(5, 'Volta às Aulas', 29.99, 'ESCOLA', '2025-01-15', '2025-02-15'),
(6, 'Segunda Maluca', 34.99, 'SEGUNDA', '2024-01-01', '2025-12-31'),
(7, 'Primeira Visita', 39.99, 'BEMVINDO', '2024-01-01', '2025-12-31'),
(8, 'Indique um Amigo', 44.99, 'AMIGO10', '2024-01-01', '2025-12-31'),
(9, 'Aniversariante', 0.00, 'NIVER', '2024-01-01', '2025-12-31')
ON CONFLICT (id_sale) DO NOTHING;

-- Atualiza a sequência do SALE
SELECT setval('sale_id_sale_seq', COALESCE((SELECT MAX(id_sale) FROM sale), 1));

-- ==========================================
-- 📅 AGENDAMENTOS (APPOINTMENT)
-- ==========================================
-- Agendamentos para hoje e próximos dias
INSERT INTO appointment (id_appointment, name_client, number_client, id_barber, start_time, end_time, total_price) VALUES
-- Hoje
(1, 'Ricardo Gomes', '81991110001', 1, NOW()::date + TIME '09:00:00', NOW()::date + TIME '09:30:00', 35.00),
(2, 'Fernando Lima', '81991110002', 1, NOW()::date + TIME '10:00:00', NOW()::date + TIME '11:00:00', 55.00),
(3, 'Gustavo Alves', '81991110003', 2, NOW()::date + TIME '09:30:00', NOW()::date + TIME '10:15:00', 45.00),
(4, 'Bruno Martins', '81991110004', 2, NOW()::date + TIME '11:00:00', NOW()::date + TIME '11:30:00', 30.00),
(5, 'Thiago Costa', '81991110005', 3, NOW()::date + TIME '08:30:00', NOW()::date + TIME '09:00:00', 35.00),
(6, 'André Souza', '81991110006', 4, NOW()::date + TIME '14:00:00', NOW()::date + TIME '15:00:00', 55.00),

-- Amanhã
(7, 'Marcos Paulo', '81991110007', 1, (NOW() + INTERVAL '1 day')::date + TIME '09:00:00', (NOW() + INTERVAL '1 day')::date + TIME '09:30:00', 35.00),
(8, 'Rafael Santos', '81991110008', 1, (NOW() + INTERVAL '1 day')::date + TIME '10:30:00', (NOW() + INTERVAL '1 day')::date + TIME '11:30:00', 55.00),
(9, 'Diego Oliveira', '81991110009', 2, (NOW() + INTERVAL '1 day')::date + TIME '14:00:00', (NOW() + INTERVAL '1 day')::date + TIME '14:45:00', 45.00),
(10, 'Felipe Rocha', '81991110010', 3, (NOW() + INTERVAL '1 day')::date + TIME '16:00:00', (NOW() + INTERVAL '1 day')::date + TIME '16:30:00', 30.00),

-- Próxima semana
(11, 'Leonardo Silva', '81991110011', 1, (NOW() + INTERVAL '3 days')::date + TIME '11:00:00', (NOW() + INTERVAL '3 days')::date + TIME '12:00:00', 55.00),
(12, 'Henrique Costa', '81991110012', 2, (NOW() + INTERVAL '4 days')::date + TIME '15:00:00', (NOW() + INTERVAL '4 days')::date + TIME '17:00:00', 120.00),
(13, 'Gabriel Ferreira', '81991110013', 3, (NOW() + INTERVAL '5 days')::date + TIME '09:00:00', (NOW() + INTERVAL '5 days')::date + TIME '09:30:00', 35.00),
(14, 'Caio Mendes', '81991110014', 4, (NOW() + INTERVAL '6 days')::date + TIME '10:00:00', (NOW() + INTERVAL '6 days')::date + TIME '10:30:00', 30.00)
ON CONFLICT (id_appointment) DO NOTHING;

-- Atualiza a sequência do APPOINTMENT
SELECT setval('appointment_id_appointment_seq', COALESCE((SELECT MAX(id_appointment) FROM appointment), 1));

-- ==========================================
-- 📅🔗✂️ AGENDAMENTO x SERVIÇO (APPOINTMENT_SERVICE)
-- ==========================================
INSERT INTO appointment_service (id_appointment, id_service) VALUES
(1, 1),        -- Ricardo: Corte Masculino
(2, 4),        -- Fernando: Corte + Barba
(3, 2),        -- Gustavo: Corte Degradê
(4, 3),        -- Bruno: Barba Completa
(5, 1),        -- Thiago: Corte Masculino
(6, 4),        -- André: Corte + Barba
(7, 1),        -- Marcos: Corte Masculino
(8, 4),        -- Rafael: Corte + Barba
(9, 2),        -- Diego: Corte Degradê
(10, 3),       -- Felipe: Barba Completa
(11, 4),       -- Leonardo: Corte + Barba
(12, 9),       -- Henrique: Platinado
(13, 1),       -- Gabriel: Corte Masculino
(14, 3);       -- Caio: Barba Completa

-- ==========================================
-- 📊 RESUMO DOS DADOS INSERIDOS
-- ==========================================
DO $$
DECLARE
    v_addresses INT;
    v_users INT;
    v_barbers INT;
    v_secretaries INT;
    v_services INT;
    v_products INT;
    v_stock INT;
    v_sales INT;
    v_appointments INT;
BEGIN
    SELECT COUNT(*) INTO v_addresses FROM address;
    SELECT COUNT(*) INTO v_users FROM employee;
    SELECT COUNT(*) INTO v_barbers FROM barber;
    SELECT COUNT(*) INTO v_secretaries FROM secretary;
    SELECT COUNT(*) INTO v_services FROM service;
    SELECT COUNT(*) INTO v_products FROM product;
    SELECT COUNT(*) INTO v_stock FROM product_stock;
    SELECT COUNT(*) INTO v_sales FROM sale;
    SELECT COUNT(*) INTO v_appointments FROM appointment;
    
    RAISE NOTICE '';
    RAISE NOTICE '==========================================';
    RAISE NOTICE '🌱 SEEDER EXECUTADO COM SUCESSO!';
    RAISE NOTICE '==========================================';
    RAISE NOTICE '📊 Resumo dos dados inseridos:';
    RAISE NOTICE '   🏠 Endereços: %', v_addresses;
    RAISE NOTICE '   👤 Usuários: %', v_users;
    RAISE NOTICE '   💈 Barbeiros: %', v_barbers;
    RAISE NOTICE '   👩‍💼 Secretárias: %', v_secretaries;
    RAISE NOTICE '   ✂️ Serviços: %', v_services;
    RAISE NOTICE '   📦 Produtos: %', v_products;
    RAISE NOTICE '   📦 Itens em estoque: %', v_stock;
    RAISE NOTICE '   💰 Promoções: %', v_sales;
    RAISE NOTICE '   📅 Agendamentos: %', v_appointments;
    RAISE NOTICE '==========================================';
    RAISE NOTICE '';
    RAISE NOTICE '🔐 Credenciais de acesso:';
    RAISE NOTICE '   Admin: admin@gobarber.com / password';
    RAISE NOTICE '   Barbeiro: carlos.barbeiro@gobarber.com / password';
    RAISE NOTICE '   Secretária: ana.secretaria@gobarber.com / password';
    RAISE NOTICE '==========================================';
END $$;
