# Stage 1: Build with Maven (usando mvnw)
FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copiar el wrapper de Maven
COPY .mvn .mvn
COPY mvnw .

# Copiar proyecto
COPY pom.xml .
COPY src ./src

# Hacer ejecutable mvnw y compilar
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run con JRE ligero
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
