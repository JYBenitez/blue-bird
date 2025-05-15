-- =============================================
-- Script para crear tablas: usuarios, tweets, y seguidores
-- Utiliza UUIDs, restricciones, claves foráneas e índices
-- =============================================

-- =============================================
-- Tabla: users
-- Contiene usuarios con UUID como clave primaria
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY ,
    nombre VARCHAR(100) NOT NULL UNIQUE, -- Aseguro que no se repitan nombres
    created_at TIMESTAMP DEFAULT now()
);

-- =============================================
-- Tabla: tweets
-- Almacena tweets asociados a un usuario
-- =============================================
CREATE TABLE IF NOT EXISTS tweets (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,  -- No permite contenido vacío
    created_at TIMESTAMP DEFAULT now(),

    -- FK: user
    user_id UUID REFERENCES users(id) ON DELETE SET NULL
);

-- Índice: mejora rendimiento de búsquedas por user_id
CREATE INDEX IF NOT EXISTS idx_tweets_user_id ON tweets(user_id);

-- =============================================
--  Tabla intermedia: follow
-- Relaciona usuarios con usuarios (N:M)
-- =============================================
CREATE TABLE IF NOT EXISTS follows (
    user_id UUID NOT NULL,
    follow_id UUID NOT NULL,

    -- Clave primaria compuesta para evitar duplicados
    PRIMARY KEY (user_id, follow_id),

    -- Claves foráneas con integridad referencial
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (follow_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Evita que un usuario se siga a sí mismo
    CHECK (user_id <> follow_id)
);

-- Índices para mejorar el rendimiento de consultas
CREATE INDEX IF NOT EXISTS idx_follow_user ON follows(user_id);
CREATE INDEX IF NOT EXISTS idx_follow_following ON follows(follow_id);


INSERT INTO users (id, nombre) VALUES
  ('c8e7b1f0-1111-4b2e-aaaa-000000000001', 'alice'),
  ('c8e7b1f0-1111-4b2e-aaaa-000000000002', 'bob'),
  ('c8e7b1f0-1111-4b2e-aaaa-000000000003', 'carol'),
  ('c8e7b1f0-1111-4b2e-aaaa-000000000004', 'dave'),
  ('c8e7b1f0-1111-4b2e-aaaa-000000000005', 'eve');

INSERT INTO tweets (id, content, user_id) VALUES
  (uuid_generate_v4(), 'Hola, soy Alice!', 'c8e7b1f0-1111-4b2e-aaaa-000000000001'),
  (uuid_generate_v4(), 'Probando mi primer tweet 🐦', 'c8e7b1f0-1111-4b2e-aaaa-000000000001'),

  (uuid_generate_v4(), '¡Buenos días desde la cuenta de Bob!', 'c8e7b1f0-1111-4b2e-aaaa-000000000002'),
  (uuid_generate_v4(), 'Bob de nuevo por aquí 👋', 'c8e7b1f0-1111-4b2e-aaaa-000000000002'),

  (uuid_generate_v4(), 'Soy Carol y me encanta postear ✨', 'c8e7b1f0-1111-4b2e-aaaa-000000000003'),
  (uuid_generate_v4(), 'Segundo tweet de Carol 💬', 'c8e7b1f0-1111-4b2e-aaaa-000000000003'),

  (uuid_generate_v4(), 'Dave aquí, feliz de unirme!', 'c8e7b1f0-1111-4b2e-aaaa-000000000004'),
  (uuid_generate_v4(), 'Otro día, otro tweet.', 'c8e7b1f0-1111-4b2e-aaaa-000000000004'),

  (uuid_generate_v4(), 'Soy Eve, saludos a todos 👋', 'c8e7b1f0-1111-4b2e-aaaa-000000000005'),
  (uuid_generate_v4(), 'Eve otra vez, compartiendo ideas.', 'c8e7b1f0-1111-4b2e-aaaa-000000000005');

INSERT INTO follows (user_id, follow_id) VALUES
  ('c8e7b1f0-1111-4b2e-aaaa-000000000001', 'c8e7b1f0-1111-4b2e-aaaa-000000000002'), -- Alice sigue a Bob
  ('c8e7b1f0-1111-4b2e-aaaa-000000000001', 'c8e7b1f0-1111-4b2e-aaaa-000000000003'), -- Alice sigue a Carol
  ('c8e7b1f0-1111-4b2e-aaaa-000000000002', 'c8e7b1f0-1111-4b2e-aaaa-000000000001'), -- Bob sigue a Alice
  ('c8e7b1f0-1111-4b2e-aaaa-000000000002', 'c8e7b1f0-1111-4b2e-aaaa-000000000005'), -- Bob sigue a Eve
  ('c8e7b1f0-1111-4b2e-aaaa-000000000003', 'c8e7b1f0-1111-4b2e-aaaa-000000000004'), -- Carol sigue a Dave
  ('c8e7b1f0-1111-4b2e-aaaa-000000000005', 'c8e7b1f0-1111-4b2e-aaaa-000000000001'); -- Eve sigue a Alice
