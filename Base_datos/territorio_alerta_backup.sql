-- ============================================
-- TerritorioAlerta - Base de Datos
-- Backup completo
-- Comuna 3 - Manrique, Medellín
-- ============================================


-- ============================================
-- ESTRUCTURA DE TABLAS
-- ============================================

-- TABLA 1: categorias
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    icono VARCHAR(50),
    prioridad_default VARCHAR(20) DEFAULT 'media',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABLA 2: usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) DEFAULT 'ciudadano',
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABLA 3: reportes
CREATE TABLE reportes (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    estado VARCHAR(20) DEFAULT 'activo',
    id_usuario INT REFERENCES usuarios(id),
    id_categoria INT REFERENCES categorias(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABLA 4: alertas
CREATE TABLE alertas (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    id_usuario INT REFERENCES usuarios(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Categorias
INSERT INTO categorias (nombre, descripcion, icono, prioridad_default) VALUES
('Basura acumulada', 'Acumulación de residuos en vías o zonas públicas', 'trash', 'media'),
('Riesgo de deslizamiento', 'Zonas con peligro de derrumbe o deslizamiento de tierra', 'alert-triangle', 'alta'),
('Inundación', 'Acumulación de agua en vías o viviendas', 'droplets', 'alta');

INSERT INTO categorias (nombre, descripcion, icono, prioridad_default) VALUES
('Daños en vías', 'Huecos, grietas o deterioro en calles y andenes', 'road', 'media'),
('Corte de agua', 'Falta de suministro de agua potable', 'droplet-off', 'media'),
('Alumbrado dañado', 'Postes o luminarias en mal estado o apagadas', 'lamp-off', 'baja');

INSERT INTO categorias (nombre, descripcion, icono, prioridad_default) VALUES
('Problemas de movilidad', 'Bloqueos, trancones o dificultades de tránsito', 'car', 'baja'),
('Riesgo comunitario', 'Situaciones de peligro para la comunidad en general', 'shield-alert', 'alta');

-- Usuarios
INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES
('Carlos Gómez', 'carlos@email.com', '1234', 'ciudadano'),
('María López', 'maria@email.com', '1234', 'ciudadano'),
('Admin Manrique', 'admin@territorialerta.com', '1234', 'admin');

-- Reportes
INSERT INTO reportes (titulo, descripcion, ubicacion, estado, id_usuario, id_categoria) VALUES
('Basura en la esquina', 'Llevan 3 días sin recoger la basura en la esquina del parque', 'Calle 45 con Carrera 20', 'activo', 1, 1),
('Grieta en el talud', 'Se ve una grieta grande en el talud de la subida', 'Barrio Manrique Central', 'activo', 2, 2),
('Calle inundada', 'Después de la lluvia la calle quedó completamente inundada', 'Carrera 15 con Calle 50', 'activo', 1, 3);

-- Alertas
INSERT INTO alertas (titulo, descripcion, tipo, activa, id_usuario) VALUES
('Corte de agua programado', 'Se realizará mantenimiento en la red de acueducto el día sábado', 'servicios públicos', TRUE, 3),
('Riesgo de deslizamiento zona alta', 'Por las lluvias fuertes se recomienda evacuar la parte alta del barrio', 'emergencia', TRUE, 3),
('Cierre vial Carrera 20', 'La carrera 20 estará cerrada por obras durante esta semana', 'movilidad', TRUE, 3);
