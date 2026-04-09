-- PostgreSQL: garante que a sequence do ID fique >= MAX(id)
SELECT setval(
  pg_get_serial_sequence('users', 'id'),
  GREATEST((SELECT COALESCE(MAX(id), 0) FROM users), 1)
);

