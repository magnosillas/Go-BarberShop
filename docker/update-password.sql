-- Atualizar senha para "password" com hash BCrypt válido
-- Hash gerado com BCryptPasswordEncoder().encode("password")
UPDATE gobarber.employee 
SET password = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' 
WHERE email = 'admin@gobarber.com';

-- Também atualiza os outros usuários
UPDATE gobarber.employee 
SET password = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG' 
WHERE id_user > 1;
