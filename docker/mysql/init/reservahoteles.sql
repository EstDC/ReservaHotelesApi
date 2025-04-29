-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-04-2025 a las 22:15:30
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `reservahoteles`
--
CREATE DATABASE IF NOT EXISTS reservahoteles;
USE reservahoteles;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` BIGINT(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `rol` enum('CLIENTE','ADMINISTRADOR') DEFAULT 'CLIENTE',
  `tipo_identificacion` enum('DNI','PASAPORTE','NIE') NOT NULL,
  `identificacion` varchar(30) NOT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `ultima_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `activo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `email`, `telefono`, `direccion`, `rol`, `tipo_identificacion`, `identificacion`, `fecha_registro`, `ultima_actualizacion`, `activo`) VALUES
(1, 'Admin General', 'admin@hotelxyz.com', '000000000', 'Oficina Central', 'ADMINISTRADOR', 'DNI', 'ADM0001', '2025-04-22 21:34:29', '2025-04-22 21:34:29', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_bancario`
--

CREATE TABLE `cliente_bancario` (
  `id_cliente_bancario` BIGINT(11) NOT NULL,
  `id_cliente` BIGINT(11) NOT NULL,
  `banco` varchar(50) NOT NULL,
  `numero_cuenta` varchar(50) NOT NULL,
  `otros_detalles` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente_bancario`
--

INSERT INTO `cliente_bancario` (`id_cliente_bancario`, `id_cliente`, `banco`, `numero_cuenta`, `otros_detalles`) VALUES
(1, 1, 'SIN BANCO', '000000000000', 'Administrador sin cuenta activa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credenciales`
--

CREATE TABLE `credenciales` (
  `id` BIGINT(11) NOT NULL,
  `id_cliente` BIGINT(11) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `salt` varchar(50) DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `ultima_actualizacion` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `reset_token` varchar(100) DEFAULT NULL,
  `reset_token_expiry` datetime DEFAULT NULL,
  `failed_attempts` BIGINT(11) NOT NULL DEFAULT 0,
  `account_non_locked` tinyint(1) NOT NULL DEFAULT 1,
  `lock_time` datetime DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `credenciales`
--

INSERT INTO `credenciales` (`id`, `id_cliente`, `username`, `password_hash`, `salt`, `fecha_creacion`, `ultima_actualizacion`, `reset_token`, `reset_token_expiry`, `failed_attempts`, `account_non_locked`, `lock_time`, `activo`) VALUES
(1, 1, 'RH_Admin', '$2a$10$wHVmuPp0AzLJvnfLk0WkfuH1R4ijDrlyOVaZsqIj4nv.zGoxTe0QW', NULL, '2025-04-22 21:34:29', '2025-04-22 21:34:29', NULL, NULL, 0, 1, NULL, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `extras`
--

CREATE TABLE `extras` (
  `id_extra` BIGINT(11) NOT NULL,
  `nombre_extra` varchar(50) NOT NULL,
  `costo_adicional` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitaciones`
--

CREATE TABLE `habitaciones` (
  `id_habitacion` BIGINT(11) NOT NULL,
  `id_hotel` BIGINT(11) NOT NULL,
  `numero_habitacion` varchar(10) NOT NULL,
  `tipo` enum('INDIVIDUAL','DOBLE','SUITE','FAMILIAR') NOT NULL,
  `capacidad` BIGINT(11) NOT NULL,
  `precio_por_noche` decimal(8,2) NOT NULL,
  `estado` enum('DISPONIBLE','OCUPADA','MANTENIMIENTO','RESERVADA') NOT NULL,
  `modo_asignacion` enum('AUTOMATICA','MANUAL') NOT NULL DEFAULT 'AUTOMATICA',
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitacion_extras`
--

CREATE TABLE `habitacion_extras` (
  `id_habitacion` BIGINT(11) NOT NULL,
  `id_extra` BIGINT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_reservas`
--

CREATE TABLE `historial_reservas` (
  `id_historial` BIGINT(11) NOT NULL,
  `id_reserva` BIGINT(11) NOT NULL,
  `id_cliente` BIGINT(11) NOT NULL,
  `id_habitacion` BIGINT(11) NOT NULL,
  `fecha_entrada` date NOT NULL,
  `fecha_salida` date NOT NULL,
  `estado_reserva` varchar(20) NOT NULL,
  `fecha_reserva` datetime DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `fecha_archivo` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_reserva_extra`
--

CREATE TABLE `historial_reserva_extra` (
  `id_historial` BIGINT(11) NOT NULL,
  `id_extra` BIGINT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hoteles`
--

CREATE TABLE `hoteles` (
  `id_hotel` BIGINT(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(200) NOT NULL,
  `pais` varchar(50) NOT NULL,
  `ciudad` varchar(50) NOT NULL,
  `latitud` decimal(9,6) NOT NULL,
  `longitud` decimal(9,6) NOT NULL,
  `numero_estrellas` tinyint(3) UNSIGNED NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ultima_actualizacion` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel_servicios`
--

CREATE TABLE `hotel_servicios` (
  `id_hotel` BIGINT(11) NOT NULL,
  `id_servicio` BIGINT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

CREATE TABLE `pagos` (
  `id_pago` BIGINT(11) NOT NULL,
  `id_reserva` BIGINT(11) NOT NULL,
  `id_cliente` BIGINT(20) NOT NULL,
  `estado_pago` enum('PENDIENTE','COMPLETADO','RECHAZADO') NOT NULL,
  `fecha_pago` datetime NOT NULL,
  `ultima_actualizacion` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `monto` decimal(10,2) NOT NULL,
  `otros_detalles` varchar(255) DEFAULT NULL,
  `tipo` enum('TARJETA','TRANSFERENCIA') NOT NULL DEFAULT 'TARJETA',
  `numero_cuenta` varchar(50) DEFAULT NULL,
  `titular` varchar(100) DEFAULT NULL,
  `expiracion` varchar(10) DEFAULT NULL,
  `fecha_registro` datetime NOT NULL DEFAULT current_timestamp(),
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `tipo_pago` enum('TARJETA','TRANSFERENCIA') NOT NULL DEFAULT 'TARJETA',
  `token_recupero` varchar(64) DEFAULT NULL COMMENT 'para recuperar contraseña',
  `token_expiracion` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reservas`
--

CREATE TABLE `reservas` (
  `id_reserva` BIGINT(11) NOT NULL,
  `id_cliente` BIGINT(11) NOT NULL,
  `id_habitacion` BIGINT(11) NOT NULL,
  `fecha_entrada` date NOT NULL,
  `fecha_salida` date NOT NULL,
  `estado_reserva` enum('PENDIENTE','CONFIRMADA','CANCELADA','COMPLETADA') NOT NULL,
  `fecha_reserva` datetime DEFAULT current_timestamp(),
  `total` decimal(10,2) NOT NULL,
  `ultima_actualizacion` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva_extra`
--

CREATE TABLE `reserva_extra` (
  `id_reserva` BIGINT(11) NOT NULL,
  `id_extra` BIGINT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicios`
--

CREATE TABLE `servicios` (
  `id_servicio` BIGINT(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `cliente_bancario`
--
ALTER TABLE `cliente_bancario`
  ADD PRIMARY KEY (`id_cliente_bancario`),
  ADD KEY `id_cliente` (`id_cliente`);

--
-- Indices de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `id_cliente` (`id_cliente`);

--
-- Indices de la tabla `extras`
--
ALTER TABLE `extras`
  ADD PRIMARY KEY (`id_extra`);

--
-- Indices de la tabla `habitaciones`
--
ALTER TABLE `habitaciones`
  ADD PRIMARY KEY (`id_habitacion`),
  ADD KEY `id_hotel` (`id_hotel`);

--
-- Indices de la tabla `habitacion_extras`
--
ALTER TABLE `habitacion_extras`
  ADD PRIMARY KEY (`id_habitacion`,`id_extra`),
  ADD KEY `id_extra` (`id_extra`);

--
-- Indices de la tabla `historial_reservas`
--
ALTER TABLE `historial_reservas`
  ADD PRIMARY KEY (`id_historial`),
  ADD KEY `id_reserva` (`id_reserva`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_habitacion` (`id_habitacion`);

--
-- Indices de la tabla `historial_reserva_extra`
--
ALTER TABLE `historial_reserva_extra`
  ADD PRIMARY KEY (`id_historial`,`id_extra`),
  ADD KEY `fk_hrex_extra` (`id_extra`);

--
-- Indices de la tabla `hoteles`
--
ALTER TABLE `hoteles`
  ADD PRIMARY KEY (`id_hotel`);

--
-- Indices de la tabla `hotel_servicios`
--
ALTER TABLE `hotel_servicios`
  ADD PRIMARY KEY (`id_hotel`,`id_servicio`),
  ADD KEY `id_servicio` (`id_servicio`);

--
-- Indices de la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD PRIMARY KEY (`id_pago`),
  ADD KEY `fk_pago_reserva` (`id_reserva`);

--
-- Indices de la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD PRIMARY KEY (`id_reserva`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_habitacion` (`id_habitacion`);

--
-- Indices de la tabla `reserva_extra`
--
ALTER TABLE `reserva_extra`
  ADD PRIMARY KEY (`id_reserva`,`id_extra`),
  ADD KEY `fk_res_extra_extra` (`id_extra`);

--
-- Indices de la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD PRIMARY KEY (`id_servicio`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` BIGINT(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `cliente_bancario`
--
ALTER TABLE `cliente_bancario`
  MODIFY `id_cliente_bancario` BIGINT(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  MODIFY `id` BIGINT(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `extras`
--
ALTER TABLE `extras`
  MODIFY `id_extra` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `habitaciones`
--
ALTER TABLE `habitaciones`
  MODIFY `id_habitacion` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `historial_reservas`
--
ALTER TABLE `historial_reservas`
  MODIFY `id_historial` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hoteles`
--
ALTER TABLE `hoteles`
  MODIFY `id_hotel` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pagos`
--
ALTER TABLE `pagos`
  MODIFY `id_pago` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reservas`
--
ALTER TABLE `reservas`
  MODIFY `id_reserva` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `servicios`
--
ALTER TABLE `servicios`
  MODIFY `id_servicio` BIGINT(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente_bancario`
--
ALTER TABLE `cliente_bancario`
  ADD CONSTRAINT `cliente_bancario_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`);

--
-- Filtros para la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD CONSTRAINT `credenciales_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`);

--
-- Filtros para la tabla `habitaciones`
--
ALTER TABLE `habitaciones`
  ADD CONSTRAINT `habitaciones_ibfk_1` FOREIGN KEY (`id_hotel`) REFERENCES `hoteles` (`id_hotel`);

--
-- Filtros para la tabla `habitacion_extras`
--
ALTER TABLE `habitacion_extras`
  ADD CONSTRAINT `habitacion_extras_ibfk_1` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`),
  ADD CONSTRAINT `habitacion_extras_ibfk_2` FOREIGN KEY (`id_extra`) REFERENCES `extras` (`id_extra`);

--
-- Filtros para la tabla `historial_reservas`
--
ALTER TABLE `historial_reservas`
  ADD CONSTRAINT `historial_reservas_ibfk_1` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id_reserva`),
  ADD CONSTRAINT `historial_reservas_ibfk_2` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  ADD CONSTRAINT `historial_reservas_ibfk_3` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`);

--
-- Filtros para la tabla `historial_reserva_extra`
--
ALTER TABLE `historial_reserva_extra`
  ADD CONSTRAINT `fk_hrex_extra` FOREIGN KEY (`id_extra`) REFERENCES `extras` (`id_extra`),
  ADD CONSTRAINT `fk_hrex_hist` FOREIGN KEY (`id_historial`) REFERENCES `historial_reservas` (`id_historial`) ON DELETE CASCADE;

--
-- Filtros para la tabla `hotel_servicios`
--
ALTER TABLE `hotel_servicios`
  ADD CONSTRAINT `hotel_servicios_ibfk_1` FOREIGN KEY (`id_hotel`) REFERENCES `hoteles` (`id_hotel`),
  ADD CONSTRAINT `hotel_servicios_ibfk_2` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`);

--
-- Filtros para la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD CONSTRAINT `fk_pago_reserva` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id_reserva`);

--
-- Filtros para la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  ADD CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`);

--
-- Filtros para la tabla `reserva_extra`
--
ALTER TABLE `reserva_extra`
  ADD CONSTRAINT `fk_res_extra_extra` FOREIGN KEY (`id_extra`) REFERENCES `extras` (`id_extra`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_res_extra_reserva` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id_reserva`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
