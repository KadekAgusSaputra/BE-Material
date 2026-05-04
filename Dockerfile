# Gunakan JDK 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy file jar hasil build (pastikan kamu sudah run 'mvn clean package' di lokal)
COPY target/*.jar app.jar

# Expose port agar bisa diakses luar
EXPOSE 8080

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]