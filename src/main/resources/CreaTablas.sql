
CREATE DATABASE IF NOT EXISTS scentia_store
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE scentia_store;


CREATE TABLE IF NOT EXISTS productos (
  id            BIGINT        NOT NULL AUTO_INCREMENT,
  nombre        VARCHAR(150)  NOT NULL,
  descripcion   VARCHAR(500)  NULL,
  precio        DECIMAL(10,2) NOT NULL,
  imagen_url    VARCHAR(300)  NULL,
  stock         INT           NOT NULL DEFAULT 0,
  categoria     VARCHAR(100)  NULL,
  presentacion  VARCHAR(50)   NULL,
  marca         VARCHAR(100)  NULL,
  activo        TINYINT(1)    NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE pedido(
id_pedido INT AUTO_INCREMENT PRIMARY KEY,
estado VARCHAR(20),
subtotal DECIMAL(12,2),
total DECIMAL(12,2)
);

CREATE TABLE pedido_detalle(
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT,
    precio DECIMAL(12,2),
    id_producto BIGINT,
    id_pedido INT,
    FOREIGN KEY (id_producto) REFERENCES productos(id),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, precio, imagen_url, stock, categoria, presentacion, marca, activo)
VALUES
  ('Bleu de Chanel',
   'Fragancia fresca y amaderada con notas de cítricos, incienso y cedro. Icónica y versátil.',
   85000, NULL, 12, 'Amaderado', '100ml EDP', 'Chanel', 1),

  ('Sauvage Dior',
   'Refrescante y audaz. Notas de bergamota calabresa y ambroxan en el fondo.',
   92000, NULL, 3, 'Fresco', '60ml EDT', 'Dior', 1),

  ('La Vie Est Belle',
   'Gourmand floral con notas de iris, praliné y patchouli. Dulce y femenino.',
   78000, NULL, 0, 'Floral', '75ml EDP', 'Lancôme', 1),

  ('Decant — Oud Wood Tom Ford',
   'Madera de oud ahumada con sándalo y vainilla. Lujo en formato accesible.',
   18500, NULL, 8, 'Orientales', 'Decant 5ml', 'Tom Ford', 1),

  ('Decant — Black Orchid',
   'Floral oscuro y rico. Notas de orquídea negra, bergamota y patchouli negro.',
   15000, NULL, 5, 'Floral', 'Decant 5ml', 'Tom Ford', 1),

  ('Aventus Creed',
   'Notas de piña, abedul ahumado, musgo y almizcle. El preferido de los conocedores.',
   145000, NULL, 2, 'Amaderado', '100ml EDP', 'Creed', 1),

  ('Flowerbomb',
   'Explosión floral con jazmín, rosa, freesia y patchouli. Irresistible y adictivo.',
   88000, NULL, 6, 'Floral', '100ml EDP', 'Viktor & Rolf', 1),

  ('Decant — Baccarat Rouge 540',
   'Ambergris, cedro de cachemira y azafrán. Uno de los más codiciados del mundo.',
   32000, NULL, 4, 'Orientales', 'Decant 10ml', 'Maison Francis Kurkdjian', 1);