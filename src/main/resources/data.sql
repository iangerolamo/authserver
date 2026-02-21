-- 1. Inserir roles primeiro
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_USER'),
                                 (2, 'ROLE_ADMIN');

-- Inserir usuário
INSERT INTO users (email, password, name, provider, google_id, enabled, created_at, updated_at, deleted_at)
VALUES ('ian@example.com', '123456', 'Ian', 'LOCAL', NULL, TRUE,
        TIMESTAMP '2026-01-24 12:00:00', TIMESTAMP '2026-01-24 12:00:00', NULL);

INSERT INTO users (email, password, name, provider, google_id, enabled, created_at, updated_at, deleted_at)
VALUES ('jessica@example.com', '123456', 'Jessica', 'LOCAL', NULL, TRUE,
        TIMESTAMP '2026-01-24 12:00:00', TIMESTAMP '2026-01-24 12:00:00', NULL);

INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),
                                              (2,  1),
                                              (1, 2);
