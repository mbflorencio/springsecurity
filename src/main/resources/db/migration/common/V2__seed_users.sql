-- Seed básico para facilitar o uso em aula (H2 e PostgreSQL).
-- Insere somente se ainda não existir.

INSERT INTO users (id, username, profile, login, password)
SELECT 1, 'Admin', 'ADMIN', 'admin', '$2a$10$6mHcSbpLxJaTFGU3DZB5fe9vlcRfrB8QfCqelFqFvOZu6VwtvHFTm'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);

INSERT INTO users (id, username, profile, login, password)
SELECT 2, 'User', 'USER', 'user', '$2a$10$6mHcSbpLxJaTFGU3DZB5fe9vlcRfrB8QfCqelFqFvOZu6VwtvHFTm'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 2);

