-- H2: após inserir IDs manualmente (seed), o identity pode não avançar.
-- Reinicia o identity em um valor alto para evitar colisões de PK.
ALTER TABLE users ALTER COLUMN id RESTART WITH 1000;

