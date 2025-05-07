# ReservaHoteles API

API REST para el sistema de reserva de hoteles desarrollada con Spring Boot.

## Requisitos

- Java 17 o superior
- Maven
- Docker y Docker Compose

## Estructura del Proyecto

```
ReservaHoteles/
├── src/                    # Código fuente
├── docker/                 # Configuración de Docker
│   ├── mysql/             # Configuración de MySQL
│   └── nginx/             # Configuración de Nginx
├── docker-compose.yml     # Configuración de contenedores
└── pom.xml                # Dependencias del proyecto
```

## Base de Datos

El proyecto utiliza MySQL como base de datos. La estructura incluye:

- Hoteles con sus detalles (ubicación, estrellas, servicios)
- Habitaciones por hotel (5 por hotel, diferentes tipos y precios)
- Extras disponibles para las habitaciones
- Servicios ofrecidos por cada hotel

## Configuración

1. Clonar el repositorio:
```bash
git clone https://github.com/EstDC/ReservaHotelesApi.git
cd ReservaHotelesApi
```

2. Iniciar los contenedores:
```bash
docker-compose up -d
```

3. Acceder a la API:
- API: http://localhost:8083
- phpMyAdmin: http://localhost:8082 (usuario: app, contraseña: app_pw)

## Endpoints Principales

- `GET /api/hotels` - Lista todos los hoteles
- `GET /api/hotels/{id}` - Obtiene detalles de un hotel específico
- `GET /api/hotels/{id}/rooms` - Lista las habitaciones de un hotel
- `GET /api/services` - Lista todos los servicios disponibles

## Desarrollo

Para ejecutar el proyecto en modo desarrollo:

```bash
./mvnw spring-boot:run
```

## Licencia

Este proyecto está bajo la Licencia MIT. 