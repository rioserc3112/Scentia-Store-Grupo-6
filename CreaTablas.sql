-- ──────────────────────────────────────────────────────────────────────────────
-- Scentia Store — Script completo de inicialización
-- ──────────────────────────────────────────────────────────────────────────────

-- ══════════════════════════════════════════════════════════════════════════════
-- PENDIENTE: descomentar y ejecutar cuando el usuario de BD tenga permisos DDL
-- ══════════════════════════════════════════════════════════════════════════════
-- -- Nuevas columnas en usuario (verificación de cuenta y recuperación de contraseña)
-- ALTER TABLE usuario ADD COLUMN IF NOT EXISTS activo BIT NOT NULL DEFAULT 1;
-- ALTER TABLE usuario ADD COLUMN IF NOT EXISTS token VARCHAR(64);
--
-- -- Tabla de rutas de seguridad dinámicas
-- CREATE TABLE IF NOT EXISTS ruta (
--     id     BIGINT       NOT NULL AUTO_INCREMENT,
--     patron VARCHAR(150) NOT NULL,
--     rol_name VARCHAR(50),
--     PRIMARY KEY (id)
-- );
--
-- -- Datos iniciales de rutas
-- INSERT INTO ruta (patron, rol_name)
-- SELECT * FROM (SELECT '/producto/**', 'ADMIN') AS tmp
-- WHERE NOT EXISTS (SELECT 1 FROM ruta LIMIT 1);
--
-- INSERT INTO ruta (patron, rol_name)
-- SELECT * FROM (SELECT '/catalogo/**', NULL) AS tmp
-- WHERE NOT EXISTS (SELECT 1 FROM ruta WHERE patron = '/catalogo/**');
--
-- INSERT INTO ruta (patron, rol_name)
-- SELECT * FROM (SELECT '/carrito/**', NULL) AS tmp
-- WHERE NOT EXISTS (SELECT 1 FROM ruta WHERE patron = '/carrito/**');
--
-- INSERT INTO ruta (patron, rol_name)
-- SELECT * FROM (SELECT '/pedido/**', NULL) AS tmp
-- WHERE NOT EXISTS (SELECT 1 FROM ruta WHERE patron = '/pedido/**');
-- ══════════════════════════════════════════════════════════════════════════════

-- ──────────────────────────────────────────────────────────────────────────────
-- Datos de prueba para la tabla `productos`
-- ──────────────────────────────────────────────────────────────────────────────

INSERT INTO productos (nombre, descripcion, precio, imagen_url, stock, categoria, presentacion, marca, activo)
VALUES
  ('Bleu de Chanel',
   'Fragancia fresca y amaderada con notas de cítricos, incienso y cedro. Icónica y versátil.',
   85000, NULL, 12, 'Amaderado', '100ml EDP', 'Chanel', true),

  ('Sauvage Dior',
   'Refrescante y audaz. Notas de bergamota calabresa y ambroxan en el fondo.',
   92000, NULL, 3, 'Fresco', '60ml EDT', 'Dior', true),

  ('La Vie Est Belle',
   'Gourmand floral con notas de iris, praliné y patchouli. Dulce y femenino.',
   78000, NULL, 0, 'Floral', '75ml EDP', 'Lancôme', true),

  ('Decant — Oud Wood Tom Ford',
   'Madera de oud ahumada con sándalo y vainilla. Lujo en formato accesible.',
   18500, NULL, 8, 'Orientales', 'Decant 5ml', 'Tom Ford', true),

  ('Decant — Black Orchid',
   'Floral oscuro y rico. Notas de orquídea negra, bergamota y patchouli negro.',
   15000, NULL, 5, 'Floral', 'Decant 5ml', 'Tom Ford', true),

  ('Aventus Creed',
   'Notas de piña, abedul ahumado, musgo y almizcle. El preferido de los conocedores.',
   145000, NULL, 2, 'Amaderado', '100ml EDP', 'Creed', true),

  ('Flowerbomb',
   'Explosión floral con jazmín, rosa, freesia y patchouli. Irresistible y adictivo.',
   88000, NULL, 6, 'Floral', '100ml EDP', 'Viktor & Rolf', true),

  ('Decant — Baccarat Rouge 540',
   'Ambergris, cedro de cachemira y azafrán. Uno de los más codiciados del mundo.',
   32000, NULL, 4, 'Orientales', 'Decant 10ml', 'Maison Francis Kurkdjian', true);
