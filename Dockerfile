# STAGE 1: Ngerakit (Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# STAGE 2: Jalanin (Run)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Kita ambil hasil rakitan dari Stage 1 tadi
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Pake limit RAM biar stabil di sisa hari terakhir
ENTRYPOINT ["java", "-Xmx512M", "-jar", "app.jar"]