-- ──────────────────────────────────────────────────────────────────────────────
-- Scentia Store — Datos de prueba para la tabla `productos`
-- Ejecutar en MySQL una vez que Hibernate haya creado la tabla (ddl-auto=update)
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
