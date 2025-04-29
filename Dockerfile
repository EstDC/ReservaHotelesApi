# ----------- STAGE 1: BUILD ------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copiamos solo los POMs para aprovechar la caché
COPY pom.xml .
COPY common/pom.xml     common/pom.xml
COPY domain/pom.xml     domain/pom.xml
COPY application/pom.xml application/pom.xml
COPY infra/pom.xml      infra/pom.xml
COPY api/pom.xml        api/pom.xml

# Bajamos las dependencias
RUN mvn -B dependency:go-offline -pl api -am

# Copiamos el resto del código fuente
COPY common     common
COPY domain     domain
COPY application application
COPY infra      infra
COPY api        api

# Compilamos el proyecto
RUN mvn -B clean package -pl api -am -DskipTests

# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app
ARG JAR_FILE=/workspace/api/target/api-0.0.1-SNAPSHOT.jar

COPY --from=builder ${JAR_FILE} app.jar

EXPOSE 8083
ENTRYPOINT ["java","-Dserver.port=8083","-jar","/app/app.jar"]