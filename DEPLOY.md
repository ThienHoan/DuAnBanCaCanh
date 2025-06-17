# Hướng dẫn Deploy DuAnBanCaCanh lên Render.com

## Yêu cầu trước khi bắt đầu

1. **GitHub Repository**: Đẩy code của bạn lên một repository GitHub
2. **Tài khoản Render**: Tạo tài khoản miễn phí tại [render.com](https://render.com)
3. **Cấu hình Google OAuth**: Cập nhật redirect URI cho môi trường production

## Các bước triển khai

### 1. Chuẩn bị Repository

1. Đẩy tất cả các file bao gồm `render.yaml` lên GitHub repository của bạn
2. Đảm bảo tất cả các script trong thư mục `/scripts/` có quyền thực thi

### 2. Cập nhật cài đặt Google OAuth

1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Điều hướng đến OAuth 2.0 Client của bạn
3. Thêm authorized redirect URI: `https://ten-ung-dung-render.onrender.com/DuAnCaCanh/googlecallback`
4. Cập nhật URI trong file `render.yaml` nếu cần

### 3. Triển khai trên Render

1. **Kết nối Repository**:
   - Đăng nhập vào Render.com
   - Nhấp "New +" → "Blueprint"
   - Kết nối với GitHub repository của bạn
   - Chọn repository chứa dự án này

2. **Cấu hình Services**:
   - Render sẽ tự động phát hiện file `render.yaml`
   - Xem lại các services được tạo:
     - `fishshop-db`: Cơ sở dữ liệu PostgreSQL
     - `fishshop-web`: Ứng dụng web Java

3. **Biến môi trường**:
   - Hầu hết các biến đã được cấu hình trong `render.yaml`
   - Cập nhật `GOOGLE_OAUTH_REDIRECT_URI` với URL thực tế của ứng dụng Render
   - Thông tin đăng nhập database sẽ được tự động tạo

4. **Triển khai**:
   - Nhấp "Create New Blueprint"
   - Chờ các services build và deploy (lần đầu có thể mất 10-15 phút)

### 4. Thiết lập Database

Database sẽ được tự động khởi tạo với:
- Tất cả các bảng cần thiết (đã chuyển đổi từ SQL Server sang PostgreSQL)
- Dữ liệu mẫu bao gồm danh mục blog và tài khoản admin
- Các chỉ mục và mối quan hệ phù hợp

**Tài khoản Admin mặc định**:
- Tên đăng nhập: `admin`
- Email: `admin@fishshop.com`
- Mật khẩu: `admin123` (đổi sau lần đăng nhập đầu tiên)

### 5. Sau khi triển khai

1. **Kiểm tra ứng dụng**:
   - Truy cập URL ứng dụng: `https://ten-ung-dung.onrender.com`
   - Kiểm tra chức năng đăng nhập
   - Xác minh kết nối database
   - Kiểm tra các tính năng blog và admin

2. **Cập nhật cấu hình**:
   - Đăng nhập vào panel admin và đổi mật khẩu mặc định
   - Cập nhật Google OAuth redirect URI nếu cần
   - Cấu hình các thiết lập bổ sung

## Cấu trúc File

```
├── render.yaml              # Cấu hình triển khai Render
├── Dockerfile              # Container ứng dụng chính
├── docker/
│   └── Dockerfile.postgres  # Container database
├── scripts/
│   ├── build.sh            # Script build
│   └── start.sh            # Script khởi động
├── resources/
│   └── database/
│       └── init.sql        # Khởi tạo PostgreSQL
└── ...                     # Các file ứng dụng của bạn
```

## Lưu ý quan trọng

1. **Di chuyển Database**: Ứng dụng đã được cấu hình để sử dụng PostgreSQL thay vì SQL Server để tương thích với Render

2. **Biến môi trường**: Kết nối database và các cấu hình khác được xử lý thông qua biến môi trường trong production

3. **Lưu trữ File**: Đối với production, nên cân nhắc sử dụng cloud storage (AWS S3, Cloudinary) cho các file upload

4. **SSL/HTTPS**: Render cung cấp chứng chỉ SSL miễn phí cho custom domain

5. **Giám sát**: Kiểm tra dashboard Render để xem logs và metrics

## Xử lý sự cố

### Lỗi Build
- Kiểm tra build logs trong dashboard Render
- Đảm bảo tương thích Java 11
- Xác minh cấu hình Ant build

### Lỗi Database
- Kiểm tra logs kết nối database
- Xác minh biến môi trường
- Đảm bảo cú pháp SQL đúng cho PostgreSQL

### Lỗi OAuth
- Xác minh redirect URI khớp chính xác
- Kiểm tra cài đặt Google Cloud Console
- Đảm bảo sử dụng HTTPS trong production

## Hỗ trợ

Đối với các vấn đề triển khai:
1. Kiểm tra tài liệu Render
2. Xem lại build và runtime logs
3. Test locally với các biến môi trường tương tự

## Ước tính chi phí

**Giới hạn Free Tier**:
- Web Service: 750 giờ/tháng
- PostgreSQL: 1GB lưu trữ, 1 triệu dòng
- Tự động ngủ sau 15 phút không hoạt động

**Gói trả phí**: Bắt đầu từ $7/tháng cho web service, $7/tháng cho database
