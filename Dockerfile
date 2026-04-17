# 1. Ambil 'lingkungan' Java dari internet
FROM openjdk:17-jdk-slim

# 2. Masukkan file .jar hasil build kamu ke dalam Docker
COPY target/*.jar app.jar

# 3. Kasih tahu Docker untuk buka pintu (port) 8080
EXPOSE 8080

# 4. Perintah untuk menyalakan aplikasinya
ENTRYPOINT ["java","-jar","/app.jar"]