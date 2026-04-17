# Gunakan Eclipse Temurin, ini lebih stabil dan didukung banyak platform cloud
FROM eclipse-temurin:17-jdk-alpine

# Buat folder kerja di dalam Docker
WORKDIR /app

# Copy file .jar dari folder target (hasil build kamu) ke dalam Docker
# Pastikan nama file .jar kamu sesuai. Kalau namanya beda, ganti 'target/*.jar' jadi nama spesifik filenya
COPY target/*.jar app.jar

# Jalankan aplikasinya
ENTRYPOINT ["java", "-jar", "app.jar"]