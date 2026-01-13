# Tóm tắt Use Cases - Fish Shop

## Tổng quan
Dự án Fish Shop có **34 use cases chính** được chia thành **10 nhóm chức năng**.

## Bảng tóm tắt Use Cases

| ID | Tên Use Case | Actor | Mô tả ngắn |
|---|---|---|---|
| **1. QUẢN LÝ NGƯỜI DÙNG** |
| UC-001 | Đăng ký tài khoản | Khách hàng mới | Tạo tài khoản mới với thông tin cá nhân |
| UC-002 | Đăng nhập | Người dùng | Xác thực và tạo session |
| UC-003 | Đăng nhập bằng Google | Người dùng | Đăng nhập qua OAuth Google |
| UC-004 | Quên mật khẩu | Người dùng | Reset mật khẩu qua email |
| UC-005 | Cập nhật thông tin cá nhân | Người dùng đã đăng nhập | Sửa thông tin profile |
| **2. QUẢN LÝ SẢN PHẨM** |
| UC-006 | Xem danh sách sản phẩm | Khách hàng | Hiển thị sản phẩm theo nhóm tên |
| UC-007 | Tìm kiếm sản phẩm | Khách hàng | Tìm kiếm theo từ khóa |
| UC-008 | Xem chi tiết sản phẩm | Khách hàng | Hiển thị thông tin chi tiết và đánh giá |
| UC-009 | Lọc sản phẩm theo thuộc tính | Khách hàng | Lọc theo màu sắc, kích thước |
| UC-010 | Sắp xếp sản phẩm | Khách hàng | Sắp xếp theo giá, tên |
| **3. QUẢN LÝ GIỎ HÀNG** |
| UC-011 | Thêm sản phẩm vào giỏ hàng | Khách hàng đã đăng nhập | Thêm sản phẩm với số lượng |
| UC-012 | Xem giỏ hàng | Khách hàng đã đăng nhập | Hiển thị danh sách sản phẩm trong giỏ |
| UC-013 | Cập nhật số lượng sản phẩm | Khách hàng đã đăng nhập | Thay đổi số lượng và tính lại tiền |
| UC-014 | Xóa sản phẩm khỏi giỏ hàng | Khách hàng đã đăng nhập | Xóa sản phẩm khỏi giỏ |
| **4. QUẢN LÝ WISHLIST** |
| UC-015 | Thêm sản phẩm vào wishlist | Khách hàng đã đăng nhập | Lưu sản phẩm yêu thích |
| UC-016 | Xem danh sách wishlist | Khách hàng đã đăng nhập | Hiển thị sản phẩm yêu thích |
| UC-017 | Xóa sản phẩm khỏi wishlist | Khách hàng đã đăng nhập | Xóa sản phẩm khỏi danh sách yêu thích |
| **5. QUẢN LÝ ĐÁNH GIÁ** |
| UC-018 | Đánh giá sản phẩm | Khách hàng đã mua | Viết đánh giá và upload hình ảnh |
| UC-019 | Xem đánh giá sản phẩm | Khách hàng | Xem đánh giá và rating |
| **6. QUẢN LÝ ĐƠN HÀNG** |
| UC-020 | Tạo đơn hàng | Khách hàng đã đăng nhập | Tạo đơn hàng từ giỏ hàng |
| UC-021 | Thanh toán đơn hàng | Khách hàng | Thanh toán qua VNPay/SePay |
| UC-022 | Xem lịch sử đơn hàng | Khách hàng đã đăng nhập | Xem danh sách đơn hàng |
| UC-023 | Hủy đơn hàng | Khách hàng | Hủy đơn hàng chưa xử lý |
| **7. QUẢN LÝ BLOG** |
| UC-024 | Xem bài viết blog | Khách hàng | Đọc bài viết về cá cảnh |
| UC-025 | Tìm kiếm bài viết | Khách hàng | Tìm kiếm bài viết theo từ khóa |
| **8. QUẢN TRỊ HỆ THỐNG** |
| UC-026 | Quản lý sản phẩm (Admin) | Admin | CRUD sản phẩm và thuộc tính |
| UC-027 | Quản lý đơn hàng (Admin) | Admin | Xử lý và cập nhật trạng thái đơn hàng |
| UC-028 | Quản lý người dùng (Admin) | Admin | Quản lý tài khoản và quyền hạn |
| UC-029 | Quản lý blog (Admin) | Admin | Tạo và quản lý bài viết blog |
| UC-030 | Quản lý kho hàng (Admin) | Admin | Quản lý tồn kho và nhập/xuất |
| **9. TÍCH HỢP AI** |
| UC-031 | Tư vấn sản phẩm bằng AI | Khách hàng | Nhận tư vấn về chăm sóc cá cảnh |
| UC-032 | Mua hàng trực tiếp qua AI Chat | Khách hàng đã đăng nhập | Mua hàng bằng câu nói tự nhiên |
| UC-033 | Thanh toán qua AI với SePay | Khách hàng đã đăng nhập | Thanh toán QR qua AI chat |
| UC-034 | Tư vấn chuyên sâu về cá cảnh | Khách hàng | Tư vấn theo kinh nghiệm người chơi |
| UC-035 | Chẩn đoán bệnh cá qua AI | Khách hàng | Chẩn đoán và điều trị bệnh cá |
| UC-036 | Tạo nội dung blog bằng AI | Admin | Tạo bài viết tự động |
| **10. QUẢN LÝ KHUYẾN MÃI** |
| UC-037 | Áp dụng mã giảm giá | Khách hàng | Sử dụng coupon giảm giá |
| UC-038 | Quản lý khuyến mãi (Admin) | Admin | Tạo và quản lý mã giảm giá |

## Phân loại theo Actor

### 👤 Khách hàng (Customer)
- **Không cần đăng nhập**: UC-006, UC-007, UC-008, UC-009, UC-010, UC-019, UC-024, UC-025, UC-031, UC-034, UC-035
- **Cần đăng nhập**: UC-011, UC-012, UC-013, UC-014, UC-015, UC-016, UC-017, UC-018, UC-020, UC-021, UC-022, UC-023, UC-032, UC-033, UC-037

### 👨‍💼 Admin
- UC-026, UC-027, UC-028, UC-029, UC-030, UC-036, UC-038

## Phân loại theo độ phức tạp

### 🟢 Đơn giản
- Xem danh sách, tìm kiếm, xem chi tiết
- Thêm/xóa wishlist
- Xem đánh giá

### 🟡 Trung bình
- Quản lý giỏ hàng
- Đánh giá sản phẩm
- Quản lý profile

### 🔴 Phức tạp
- Thanh toán online
- Quản lý đơn hàng
- Tích hợp AI
- Quản trị hệ thống

## Các tính năng đặc biệt

### 🎯 Tính năng nhóm sản phẩm
- **UC-006**: Hiển thị sản phẩm theo nhóm tên
- Phân trang theo nhóm (12 nhóm/trang)
- Không cần product_id liên tiếp

### 💳 Tích hợp thanh toán
- **UC-021**: Thanh toán qua VNPay và SePay
- Webhook xử lý kết quả thanh toán
- Hỗ trợ nhiều phương thức thanh toán

### 🤖 Tích hợp AI
- **UC-031**: Tư vấn chăm sóc cá cảnh
- **UC-032**: Mua hàng trực tiếp qua AI chat
- **UC-033**: Thanh toán qua AI với SePay QR
- **UC-034**: Tư vấn chuyên sâu theo kinh nghiệm
- **UC-035**: Chẩn đoán bệnh cá qua AI
- **UC-036**: Tạo nội dung blog tự động

### ⭐ Hệ thống đánh giá
- **UC-018, UC-019**: Đánh giá bằng sao và upload hình ảnh
- Hiển thị rating trung bình
- Phân trang đánh giá

## Số liệu thống kê

- **Tổng số Use Cases**: 38
- **Use Cases cho Khách hàng**: 27
- **Use Cases cho Admin**: 7
- **Use Cases cho cả hai**: 4
- **Use Cases không cần đăng nhập**: 11
- **Use Cases cần đăng nhập**: 27

## Ưu tiên phát triển

### 🔥 Ưu tiên cao
1. UC-001, UC-002: Đăng ký/đăng nhập
2. UC-006, UC-008: Xem sản phẩm
3. UC-011, UC-012: Giỏ hàng
4. UC-020, UC-021: Đặt hàng và thanh toán

### ⚡ Ưu tiên trung bình
1. UC-015, UC-016: Wishlist
2. UC-018, UC-019: Đánh giá
3. UC-024: Blog
4. UC-031, UC-034, UC-035: AI tư vấn và chẩn đoán

### 📈 Ưu tiên thấp
1. UC-032, UC-033: AI mua hàng và thanh toán
2. UC-036: AI tạo blog
3. UC-037, UC-038: Khuyến mãi
4. UC-029, UC-030: Quản lý blog và kho hàng 