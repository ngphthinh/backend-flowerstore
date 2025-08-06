# Backend Flower Shop

Đây là dự án backend cho hệ thống quản lý shop hoa, xây dựng bằng Spring Boot. Dự án cung cấp các API RESTful để quản lý sản phẩm, đơn hàng, người dùng và nhiều chức năng khác.

## Tính năng
- Xác thực và phân quyền người dùng (Spring Security, JWT)
- Quản lý sản phẩm, đơn hàng, khách hàng
- Tích hợp cơ sở dữ liệu MySQL (Spring Data JPA)
- Redis caching
- Tích hợp Cloudinary để upload ảnh
- Xuất báo cáo với JasperReports
- Tài liệu API với OpenAPI/Swagger
- Kiểm tra dữ liệu đầu vào với các validator tùy chỉnh
- Hỗ trợ tác vụ định kỳ và bất đồng bộ

## Công nghệ sử dụng
- Java 17/21
- Spring Boot 3.5.x
- MySQL
- Redis
- Cloudinary
- JasperReports
- MapStruct
- Lombok

## Bắt đầu

### Yêu cầu
- Java 17 trở lên
- Maven
- MySQL
- Redis

### Cấu hình
1. Tạo file `src/main/resources/application-secret.properties` từ file mẫu (nếu có) và điền thông tin bí mật (DB, Cloudinary, ...).
2. Chỉnh sửa `src/main/resources/application.properties` cho phù hợp môi trường của bạn.

### Build & Chạy
```bash
mvn clean install
mvn spring-boot:run
```

### Tài liệu API
Sau khi chạy, truy cập Swagger UI tại: `http://localhost:8080/swagger-ui.html`

## Hướng dẫn chạy dự án từ đầu

1. **Clone source code:**
   ```bash
   git clone https://github.com/ngphthinh/backend-flowerstore.git
   ```
2. **Di chuyển vào thư mục dự án:**
   ```bash
   cd backend-flowerstore
   ```
3. **Cài đặt các phụ thuộc và build project:**
   ```bash
   mvn clean install
   ```
4. **Cấu hình kết nối database, redis, cloudinary:**
   - Sửa file `src/main/resources/application.properties` cho phù hợp môi trường của bạn (MySQL, Redis, ...)
   - Nếu có file `application-secret.properties.example`, hãy copy thành `application-secret.properties` và điền thông tin bí mật
5. **Chạy ứng dụng:**
   ```bash
   mvn spring-boot:run
   ```

6. **Truy cập API:**
- Mở file *Backend-Flower-V1.postman_collection.json* trong thư mục gốc dự án để xem các API đã được định nghĩa.
## Kiểm thử
Chạy toàn bộ test:
```bash
mvn test
```

## License
Dự án phục vụ mục đích học tập/demo.
