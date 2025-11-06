# Dùng image JDK chính thức của Eclipse Temurin (JDK 17)
FROM eclipse-temurin:17-jdk

# Tạo thư mục làm việc
WORKDIR /app

# Copy file .jar từ target (do bạn đã build bằng Maven/Gradle)
COPY target/backend-flower-shop-0.0.1.jar app.jar

# Mở port cho ứng dụng
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
