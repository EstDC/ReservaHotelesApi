USE reservahoteles;

-- Insertar servicios básicos
INSERT INTO `servicios` (`id_servicio`, `nombre`, `descripcion`) VALUES
(1, 'WiFi', 'Conexión inalámbrica a internet'),
(2, 'Piscina', 'Piscina climatizada'),
(3, 'Spa', 'Servicios de spa y masajes'),
(4, 'Gimnasio', 'Gimnasio equipado'),
(5, 'Restaurante', 'Restaurante con servicio completo'),
(6, 'Bar', 'Bar con servicio de bebidas'),
(7, 'Estacionamiento', 'Estacionamiento privado'),
(8, 'Servicio a la habitación', 'Servicio de comida a la habitación'),
(9, 'Lavandería', 'Servicio de lavandería'),
(10, 'Concierge', 'Servicio de conserjería');

-- Insertar hoteles
INSERT INTO `hoteles` (`id_hotel`, `nombre`, `direccion`, `pais`, `ciudad`, `latitud`, `longitud`, `numero_estrellas`, `telefono`, `email`) VALUES
(1, 'Hotel Azores Paradise', 'Rua do Mar 1', 'Portugal', 'Azores', 37.7412, -25.6756, 5, '+351 296 123 456', 'info@azoresparadise.com'),
(2, 'Hotel Bordeaux Chateau', 'Rue du Vin 1', 'Francia', 'Burdeos', 44.8378, -0.5792, 5, '+33 5 56 123 456', 'info@bordeauxchateau.com'),
(3, 'Hotel Lisboa Riverside', 'Avenida Ribeira 1', 'Portugal', 'Lisboa', 38.7223, -9.1393, 4, '+351 21 123 456', 'info@lisboriverside.com'),
(4, 'Hotel London Royal', 'Kings Road 1', 'Reino Unido', 'Londres', 51.5074, -0.1278, 5, '+44 20 1234 5678', 'info@londonroyal.com'),
(5, 'Hotel Menorca Beach', 'Calle del Mar 1', 'España', 'Menorca', 39.8883, 4.2644, 4, '+34 971 123 456', 'info@menorcabeach.com'),
(6, 'Hotel Paris Eiffel', 'Avenue des Champs-Élysées 1', 'Francia', 'París', 48.8566, 2.3522, 5, '+33 1 23 45 67 89', 'info@pariseiffel.com'),
(7, 'Hotel Prague Castle', 'Castle Square 1', 'República Checa', 'Praga', 50.0755, 14.4378, 4, '+420 123 456 789', 'info@praguecastle.com'),
(8, 'Hotel Santander Bay', 'Paseo Marítimo 1', 'España', 'Santander', 43.4623, -3.8099, 4, '+34 942 123 456', 'info@santanderbay.com'),
(9, 'Hotel Santorini Sunset', 'Caldera Road 1', 'Grecia', 'Santorini', 36.3932, 25.4615, 5, '+30 228 123 456', 'info@santorinisunset.com'),
(10, 'Hotel Split Palace', 'Riva Promenade 1', 'Croacia', 'Split', 43.5081, 16.4402, 4, '+385 21 123 456', 'info@splitpalace.com'),
(11, 'Hotel Venice Canal', 'Canal Grande 1', 'Italia', 'Venecia', 45.4408, 12.3155, 5, '+39 041 123 456', 'info@venicecanal.com');

-- Asociar servicios a hoteles (todos los servicios para hoteles 4-5 estrellas)
INSERT INTO `hotel_servicios` (`id_hotel`, `id_servicio`)
SELECT h.id_hotel, s.id_servicio
FROM hoteles h
CROSS JOIN servicios s
WHERE h.numero_estrellas >= 4;

-- Asociar servicios básicos a hoteles 3 estrellas
INSERT INTO `hotel_servicios` (`id_hotel`, `id_servicio`)
SELECT h.id_hotel, s.id_servicio
FROM hoteles h
CROSS JOIN servicios s
WHERE h.numero_estrellas = 3
AND s.id_servicio IN (1, 5, 7, 8, 9);

-- Insertar habitaciones para cada hotel (5 habitaciones por hotel)
INSERT INTO `habitaciones` (`id_habitacion`, `id_hotel`, `numero_habitacion`, `tipo`, `capacidad`, `precio_por_noche`, `estado`, `modo_asignacion`, `descripcion`) VALUES
-- Hotel Azores Paradise (5 estrellas)
(1, 1, '101', 'INDIVIDUAL', 1, 150.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al océano'),
(2, 1, '102', 'DOBLE', 2, 250.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(3, 1, '103', 'FAMILIAR', 4, 400.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con sala de estar'),
(4, 1, '104', 'SUITE', 2, 500.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite de lujo con jacuzzi'),
(5, 1, '105', 'DOBLE', 2, 280.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel Bordeaux Chateau (5 estrellas)
(6, 2, '201', 'INDIVIDUAL', 1, 160.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista a los viñedos'),
(7, 2, '202', 'DOBLE', 2, 260.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con terraza'),
(8, 2, '203', 'FAMILIAR', 4, 420.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con cocina'),
(9, 2, '204', 'SUITE', 2, 520.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite presidencial'),
(10, 2, '205', 'DOBLE', 2, 290.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble ejecutiva'),

-- Hotel Lisboa Riverside (4 estrellas)
(11, 3, '301', 'INDIVIDUAL', 1, 120.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al río'),
(12, 3, '302', 'DOBLE', 2, 200.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(13, 3, '303', 'FAMILIAR', 4, 350.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con sala de estar'),
(14, 3, '304', 'SUITE', 2, 400.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite con vista panorámica'),
(15, 3, '305', 'DOBLE', 2, 220.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel London Royal (5 estrellas)
(16, 4, '401', 'INDIVIDUAL', 1, 170.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista a la ciudad'),
(17, 4, '402', 'DOBLE', 2, 270.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con terraza'),
(18, 4, '403', 'FAMILIAR', 4, 450.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con jacuzzi'),
(19, 4, '404', 'SUITE', 2, 550.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite presidencial'),
(20, 4, '405', 'DOBLE', 2, 300.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble ejecutiva'),

-- Hotel Menorca Beach (4 estrellas)
(21, 5, '501', 'INDIVIDUAL', 1, 125.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al mar'),
(22, 5, '502', 'DOBLE', 2, 210.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(23, 5, '503', 'FAMILIAR', 4, 360.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con terraza'),
(24, 5, '504', 'SUITE', 2, 410.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite con vista panorámica'),
(25, 5, '505', 'DOBLE', 2, 230.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel Paris Eiffel (5 estrellas)
(26, 6, '601', 'INDIVIDUAL', 1, 180.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista a la Torre Eiffel'),
(27, 6, '602', 'DOBLE', 2, 280.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con terraza'),
(28, 6, '603', 'FAMILIAR', 4, 470.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con jacuzzi'),
(29, 6, '604', 'SUITE', 2, 570.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite presidencial'),
(30, 6, '605', 'DOBLE', 2, 320.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble ejecutiva'),

-- Hotel Prague Castle (4 estrellas)
(31, 7, '701', 'INDIVIDUAL', 1, 130.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al castillo'),
(32, 7, '702', 'DOBLE', 2, 220.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(33, 7, '703', 'FAMILIAR', 4, 370.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con terraza'),
(34, 7, '704', 'SUITE', 2, 420.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite con vista panorámica'),
(35, 7, '705', 'DOBLE', 2, 240.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel Santander Bay (4 estrellas)
(36, 8, '801', 'INDIVIDUAL', 1, 120.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista a la bahía'),
(37, 8, '802', 'DOBLE', 2, 200.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(38, 8, '803', 'FAMILIAR', 4, 350.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con terraza'),
(39, 8, '804', 'SUITE', 2, 400.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite con vista panorámica'),
(40, 8, '805', 'DOBLE', 2, 220.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel Santorini Sunset (5 estrellas)
(41, 9, '901', 'INDIVIDUAL', 1, 190.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al mar Egeo'),
(42, 9, '902', 'DOBLE', 2, 290.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con terraza'),
(43, 9, '903', 'FAMILIAR', 4, 490.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con jacuzzi'),
(44, 9, '904', 'SUITE', 2, 590.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite presidencial'),
(45, 9, '905', 'DOBLE', 2, 330.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble ejecutiva'),

-- Hotel Split Palace (4 estrellas)
(46, 10, '1001', 'INDIVIDUAL', 1, 125.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al palacio'),
(47, 10, '1002', 'DOBLE', 2, 210.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con balcón'),
(48, 10, '1003', 'FAMILIAR', 4, 360.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con terraza'),
(49, 10, '1004', 'SUITE', 2, 410.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite con vista panorámica'),
(50, 10, '1005', 'DOBLE', 2, 230.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble premium'),

-- Hotel Venice Canal (5 estrellas)
(51, 11, '1101', 'INDIVIDUAL', 1, 200.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación individual con vista al canal'),
(52, 11, '1102', 'DOBLE', 2, 300.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble con terraza'),
(53, 11, '1103', 'FAMILIAR', 4, 500.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite familiar con jacuzzi'),
(54, 11, '1104', 'SUITE', 2, 600.00, 'DISPONIBLE', 'AUTOMATICA', 'Suite presidencial'),
(55, 11, '1105', 'DOBLE', 2, 340.00, 'DISPONIBLE', 'AUTOMATICA', 'Habitación doble ejecutiva');

-- Insertar extras disponibles
INSERT INTO `extras` (`id_extra`, `nombre_extra`, `costo_adicional`) VALUES
(1, 'Desayuno buffet', 15.00),
(2, 'Cama extra', 25.00),
(3, 'Cuna', 10.00),
(4, 'Minibar', 20.00),
(5, 'Vista al mar', 30.00),
(6, 'Vista a la ciudad', 20.00),
(7, 'Balcón', 25.00),
(8, 'Jacuzzi', 50.00),
(9, 'Servicio de limpieza premium', 15.00),
(10, 'Parking privado', 20.00),
(11, 'Acceso a spa', 35.00),
(12, 'Acceso a gimnasio', 15.00),
(13, 'Servicio de niñera', 40.00),
(14, 'Traslado al aeropuerto', 45.00),
(15, 'Servicio de lavandería', 20.00);

-- Asociar extras a habitaciones según su tipo y categoría del hotel
-- Suites y habitaciones de hoteles 5 estrellas
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, e.id_extra
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
CROSS JOIN extras e
WHERE h.tipo = 'SUITE' AND ht.numero_estrellas = 5;

-- Habitaciones familiares de hoteles 4-5 estrellas
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, e.id_extra
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
CROSS JOIN extras e
WHERE h.tipo = 'FAMILIAR' AND ht.numero_estrellas >= 4
AND e.id_extra IN (1, 2, 3, 4, 9, 13);

-- Habitaciones dobles de hoteles 4-5 estrellas
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, e.id_extra
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
CROSS JOIN extras e
WHERE h.tipo = 'DOBLE' AND ht.numero_estrellas >= 4
AND e.id_extra IN (1, 4, 6, 7, 9);

-- Habitaciones individuales de hoteles 4-5 estrellas
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, e.id_extra
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
CROSS JOIN extras e
WHERE h.tipo = 'INDIVIDUAL' AND ht.numero_estrellas >= 4
AND e.id_extra IN (1, 4, 6, 9);

-- Extras específicos para hoteles con vistas especiales
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, 5
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
WHERE ht.ciudad IN ('Azores', 'Menorca', 'Santorini', 'Venecia')
AND h.descripcion LIKE '%vista al mar%';

-- Extras específicos para hoteles con ubicaciones especiales
INSERT INTO `habitacion_extras` (`id_habitacion`, `id_extra`)
SELECT h.id_habitacion, 14
FROM habitaciones h
JOIN hoteles ht ON h.id_hotel = ht.id_hotel
WHERE ht.ciudad IN ('Londres', 'París', 'Praga', 'Venecia'); 