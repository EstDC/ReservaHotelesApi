# ReservaHoteles API

API REST para el sistema de reserva de hoteles desarrollada con Spring Boot.

## Arquitectura

El proyecto sigue los principios de la Arquitectura Hexagonal (también conocida como Ports and Adapters), que separa la lógica de negocio de los detalles técnicos. Esta arquitectura se divide en tres capas principales:

### Capa de Dominio (Domain)
- Contiene la lógica de negocio central
- Define las entidades y reglas de negocio
- Es independiente de frameworks y tecnologías
- Ubicación: `com.reservahoteles.domain`

### Capa de Aplicación (Application)
- Implementa los casos de uso
- Coordina el flujo entre el dominio y los adaptadores
- Define los puertos (interfaces) para la comunicación
- Ubicación: `com.reservahoteles.application`

### Capa de Infraestructura (Infrastructure)
- Implementa los adaptadores para bases de datos, APIs, etc.
- Contiene la configuración de Spring Boot
- Maneja la persistencia y comunicación externa
- Ubicación: `com.reservahoteles.infra`

## Estructura del Proyecto

```
ReservaHoteles/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       ├── reservahoteles/
│   │   │       │   ├── domain/           # Lógica de negocio
│   │   │       │   │   ├── model/        # Entidades de dominio
│   │   │       │   │   └── service/      # Servicios de dominio
│   │   │       │   ├── application/      # Casos de uso
│   │   │       │   │   ├── port/
│   │   │       │   │   │   ├── in/       # Puertos de entrada
│   │   │       │   │   │   └── out/      # Puertos de salida
│   │   │       │   │   └── service/      # Implementación de casos de uso
│   │   │       │   └── infra/            # Adaptadores
│   │   │       │       ├── config/       # Configuración
│   │   │       │       ├── persistence/  # Repositorios
│   │   │       │       └── web/          # Controladores REST
│   │   │       └── security/             # Configuración de seguridad
│   │   └── resources/
│   │       └── application.yml
│   └── test/                             # Tests unitarios y de integración
├── docker/
│   ├── mysql/
│   │   └── init/                        # Scripts de inicialización
│   └── nginx/
├── docker-compose.yml
└── pom.xml
```

## Módulos Principales

### Módulo de Hoteles
- Gestión de hoteles y sus servicios
- Ubicación: `com.reservahoteles.domain.model.hotel`
- Casos de uso: `com.reservahoteles.application.port.in.hotel`

### Módulo de Habitaciones
- Gestión de habitaciones y extras
- Ubicación: `com.reservahoteles.domain.model.habitacion`
- Casos de uso: `com.reservahoteles.application.port.in.habitacion`

### Módulo de Reservas
- Gestión de reservas y pagos
- Ubicación: `com.reservahoteles.domain.model.reserva`
- Casos de uso: `com.reservahoteles.application.port.in.reserva`

### Módulo de Clientes
- Gestión de clientes y autenticación
- Ubicación: `com.reservahoteles.domain.model.cliente`
- Casos de uso: `com.reservahoteles.application.port.in.cliente`

## Patrones de Diseño

- **Arquitectura Hexagonal**: Separación clara de responsabilidades
- **CQRS**: Separación de comandos y consultas
- **Repository Pattern**: Abstracción de la capa de persistencia
- **Factory Pattern**: Creación de objetos complejos
- **Strategy Pattern**: Implementación de diferentes estrategias de negocio

## Flujo de Datos

1. **Entrada**: Los controladores REST reciben las peticiones HTTP
2. **Adaptación**: Los DTOs convierten los datos a objetos de dominio
3. **Lógica**: Los casos de uso implementan la lógica de negocio
4. **Persistencia**: Los repositorios manejan la persistencia de datos
5. **Salida**: Los DTOs convierten los resultados a respuestas HTTP

## Requisitos

- Java 17 o superior
- Maven
- Docker y Docker Compose

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
- Swagger UI: http://localhost:8083/swagger-ui.html
- phpMyAdmin: http://localhost:8082 (usuario: app, contraseña: app_pw)

## Endpoints de la API

### Autenticación
- `POST /api/auth/login` - Autenticación de usuarios
- `POST /api/auth/recuperar` - Recuperación de contraseña
- `POST /api/auth/confirmar` - Confirmación de recuperación de contraseña

### Hoteles
- `GET /api/hoteles` - Lista y búsqueda de hoteles con filtros
- `POST /api/hoteles` - Crea un nuevo hotel (ADMIN)
- `PUT /api/hoteles/{id}` - Actualiza un hotel (ADMIN)
- `DELETE /api/hoteles/{id}` - Elimina un hotel (ADMIN)

### Habitaciones
- `GET /api/habitaciones` - Lista habitaciones con filtros
- `POST /api/habitaciones` - Crea una nueva habitación
- `PUT /api/habitaciones/{id}` - Actualiza una habitación
- `DELETE /api/habitaciones/{id}` - Elimina una habitación
- `GET /api/habitaciones/{id}/historial` - Consulta historial de una habitación

### Reservas
- `GET /api/reservas` - Lista reservas con filtros
- `POST /api/reservas` - Crea una nueva reserva
- `PUT /api/reservas/{id}` - Actualiza una reserva
- `DELETE /api/reservas/{id}` - Elimina una reserva
- `PUT /api/reservas/{id}/cancelar` - Cancela una reserva
- `PUT /api/reservas/{id}/archivar` - Archiva una reserva

### Clientes
- `POST /api/clientes` - Registro de nuevo cliente
- `GET /api/clientes` - Lista clientes con filtros
- `PUT /api/clientes/{id}` - Actualiza datos de cliente
- `DELETE /api/clientes/{id}` - Elimina un cliente

### Pagos
- `POST /api/pagos` - Registra un nuevo pago
- `GET /api/pagos` - Consulta pagos con filtros
- `PUT /api/pagos/{id}/estado` - Actualiza estado de pago

### Extras
- `GET /api/extras` - Lista extras disponibles
- `POST /api/extras` - Crea un nuevo extra
- `PUT /api/extras/{id}` - Actualiza un extra
- `DELETE /api/extras/{id}` - Elimina un extra
- `PUT /api/extras/reservas` - Solicita extras en una reserva

### Historial
- `GET /api/historial/reservas` - Consulta historial de reservas
- `GET /api/historial/clientes` - Consulta historial de clientes
- `GET /api/historial/habitaciones` - Consulta historial de habitaciones

## Características Principales

- Autenticación JWT con roles (ADMIN, CLIENTE)
- Gestión completa de hoteles y habitaciones
- Sistema de reservas con estados (PENDIENTE, CONFIRMADA, CANCELADA, ARCHIVADA)
- Gestión de pagos y extras
- Historial detallado de reservas, clientes y habitaciones
- Documentación completa con Swagger UI
- Paginación en todos los endpoints de listado
- Filtros avanzados en búsquedas

## Desarrollo

Para ejecutar el proyecto en modo desarrollo:

```bash
./mvnw spring-boot:run
```

## Configuración del Entorno de Desarrollo

1. **Requisitos Previos**
   ```bash
   # Verificar versión de Java
   java -version  # Debe ser 17 o superior
   
   # Verificar Maven
   mvn -version
   
   # Verificar Docker
   docker --version
   docker-compose --version
   ```

2. **Configuración del IDE**
   - Recomendado: IntelliJ IDEA o Eclipse con Spring Tools
   - Plugins necesarios:
     - Lombok
     - Spring Boot Tools
     - Docker

3. **Variables de Entorno**
   ```properties
   # application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3307/reservahoteles
       username: app
       password: app_pw
     jpa:
       hibernate:
         ddl-auto: validate
       show-sql: true
   ```

## Testing

> **Nota**: La implementación de tests está pendiente de desarrollo. Se planea implementar:
> - Tests unitarios para cada capa de la arquitectura
> - Tests de integración para los flujos principales
> - Tests de aceptación para los casos de uso críticos
> - Cobertura de código con JaCoCo

## Ejemplos de Uso

### Autenticación
```bash
# Login
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@ejemplo.com","password":"contraseña"}'

# Respuesta
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer"
}
```

### Crear Reserva
```bash
# Crear nueva reserva
curl -X POST http://localhost:8083/api/reservas \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "idHabitacion": 1,
    "fechaInicio": "2024-03-01",
    "fechaFin": "2024-03-05",
    "extras": [1, 2]
  }'
```

## Despliegue

### Despliegue Local
```bash
# Construir la aplicación
mvn clean package

# Ejecutar con Docker
docker-compose up -d
```

### Despliegue en Producción
1. **Preparación**
   ```bash
   # Construir imagen Docker
   docker build -t reservahoteles-api .
   
   # Etiquetar imagen
   docker tag reservahoteles-api:latest tu-registro/reservahoteles-api:1.0.0
   ```

2. **Despliegue**
   ```bash
   # Subir imagen
   docker push tu-registro/reservahoteles-api:1.0.0
   
   # Desplegar en servidor
   docker-compose -f docker-compose.prod.yml up -d
   ```

## Guía de Contribución

1. **Fork del Repositorio**
   - Crea un fork del proyecto
   - Clona tu fork localmente

2. **Desarrollo**
   - Crea una rama para tu feature: `git checkout -b feature/nueva-funcionalidad`
   - Realiza tus cambios
   - Asegúrate de que los tests pasan
   - Actualiza la documentación si es necesario

3. **Pull Request**
   - Push a tu fork: `git push origin feature/nueva-funcionalidad`
   - Crea un Pull Request
   - Describe los cambios realizados
   - Espera la revisión

4. **Convenciones de Código**
   - Sigue las convenciones de Java
   - Usa nombres descriptivos
   - Documenta el código
   - Escribe tests unitarios

## Troubleshooting

> **Nota**: Esta sección se actualizará con la experiencia de uso y los problemas comunes encontrados durante el desarrollo y despliegue.

## Licencia

Este proyecto está bajo la Licencia MIT.

## Contacto

- Autor: [Tu Nombre]
- Email: [Tu Email]
- GitHub: [Tu Perfil de GitHub] 