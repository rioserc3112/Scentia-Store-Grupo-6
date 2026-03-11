-- -------------------------------
-- Sección de administración
-- -------------------------------

drop database if exists scentia_store;
-- Nota: Ten cuidado al borrar el root, usualmente se prefiere usar un usuario específico
drop user if exists 'grupo6'@'%'; 

-- Creación del esquema
CREATE DATABASE scentia_store
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuario (Asegúrate de que el usuario no exista o ajusta según tu entorno)
create user 'grupo6'@'%' identified by '12345';

-- Permisos del usuario
grant select, insert, update, delete
on scentia_store.* to 'grupo6'@'%';

flush privileges;

use scentia_store;

-- -------------------------------
-- Sección de Creación de Tablas
-- -------------------------------

-- 1. Tabla de Productos
create table productos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(500) NULL,
  precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
  imagen_url VARCHAR(2000) NULL,
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  categoria VARCHAR(100) NULL,
  presentacion VARCHAR(50) NULL,
  marca VARCHAR(100) NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  index ndx_nombre (nombre),
  index ndx_categoria (categoria)
) ENGINE = InnoDB;

-- 2. Tabla de Usuarios
CREATE TABLE usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) DEFAULT NULL,
  correo VARCHAR(150) NOT NULL,
  password VARCHAR(200) NOT NULL,
  rol VARCHAR(20) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (id),
  UNIQUE KEY (correo)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- -------------------------------
-- Sección de Inserción de Datos
-- -------------------------------

INSERT INTO productos
(nombre, descripcion, precio, imagen_url, stock, categoria, presentacion, marca, activo)
VALUES
('Bleu de Chanel', 'Fragancia fresca y amaderada con notas de cítricos, incienso y cedro.', 85000, NULL, 12, 'Amaderado', '100ml EDP', 'Chanel', 1),
('Sauvage Dior', 'Refrescante y audaz. Notas de bergamota calabresa y ambroxan.', 92000, NULL, 3, 'Fresco', '60ml EDT', 'Dior', 1),
('La Vie Est Belle', 'Gourmand floral con notas de iris, praliné y patchouli.', 78000, NULL, 0, 'Floral', '75ml EDP', 'Lancôme', 1),
('Decant — Oud Wood Tom Ford', 'Madera de oud ahumada con sándalo y vainilla.', 18500, NULL, 8, 'Orientales', 'Decant 5ml', 'Tom Ford', 1),
('Decant — Black Orchid', 'Floral oscuro y rico. Notas de orquídea negra y patchouli.', 15000, NULL, 5, 'Floral', 'Decant 5ml', 'Tom Ford', 1),
('Aventus Creed', 'Notas de piña, abedul ahumado, musgo y almizcle.', 145000, NULL, 2, 'Amaderado', '100ml EDP', 'Creed', 1),
('Flowerbomb', 'Explosión floral con jazmín, rosa, freesia y patchouli.', 88000, NULL, 6, 'Floral', '100ml EDP', 'Viktor & Rolf', 1),
('Decant — Baccarat Rouge 540', 'Ambergris, cedro de cachemira y azafrán.', 32000, NULL, 4, 'Orientales', 'Decant 10ml', 'Maison Francis Kurkdjian', 1);