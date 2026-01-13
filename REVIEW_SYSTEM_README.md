# Hệ thống Review - Fish Shop

## Tổng quan
Hệ thống review cho phép khách hàng đánh giá sản phẩm với rating từ 1-5 sao, viết comment và upload hình ảnh.

## Cấu trúc Database

### Bảng Reviews
```sql
CREATE TABLE Reviews (
    review_id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT,
    user_id INT,
    order_id INT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    review_date DATETIME2 DEFAULT GETDATE(),
    status NVARCHAR(50) DEFAULT 'pending',
    is_verified_purchase BIT DEFAULT 0,
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);
```

### Bảng Review_images
```sql
CREATE TABLE Review_images (
    image_id INT IDENTITY(1,1) PRIMARY KEY,
    review_id INT NOT NULL,
    image_url NVARCHAR(500) NOT NULL,
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (review_id) REFERENCES Reviews(review_id)
);
```

## Cấu trúc Code

### 1. Model Classes
- `model.entity.pReview.Review` - Entity class cho review
- `model.entity.pReview.ReviewImage` - Entity class cho review images

### 2. DAO Classes
- `dao.impl.pReview.ReviewDAO` - Data access cho reviews
- `dao.impl.pReview.ReviewImageDAO` - Data access cho review images

### 3. Service Classes
- `service.interfaces.ReviewService` - Interface cho review service
- `service.impl.ReviewServiceImpl` - Implementation của review service

### 4. Controller Classes
- `controller.client.ReviewController` - Xử lý submit/delete review
- `controller.client.ProductDetailWithReviewServlet` - Hiển thị product detail với reviews (file mới)
- `controller.client.ProductDetailServlet` - File cũ (có thể conflict)

### 5. JSP Components
- `web/components/review-section.jsp` - Component hiển thị review section

## Tính năng chính

### 1. Hiển thị Review
- Hiển thị danh sách reviews cho sản phẩm
- Hiển thị rating trung bình và số lượng reviews
- Hiển thị phân bố rating (1-5 sao)
- Hiển thị hình ảnh review (nếu có)

### 2. Submit Review
- Form đánh giá với rating từ 1-5 sao
- Textarea để viết comment
- Upload nhiều hình ảnh
- Validation: user phải đăng nhập, chưa review sản phẩm này

### 3. Delete Review
- Xóa review (soft delete)
- Chỉ user sở hữu review hoặc admin mới được xóa

### 4. Review Statistics
- Tính rating trung bình
- Đếm số lượng reviews
- Phân bố rating theo từng mức sao

## Cách sử dụng

### 1. Thêm Review Section vào Product Detail
Trong file `product_detail.jsp`, thêm:
```jsp
<%@ include file="components/review-section.jsp" %>
```

### 2. Cấu hình URL Mapping
Đảm bảo các servlet đã được map đúng trong `web.xml`:
```xml
<servlet>
    <servlet-name>ProductDetailServlet</servlet-name>
    <servlet-class>controller.client.ProductDetailServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>ProductDetailServlet</servlet-name>
    <url-pattern>/product-detail</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>ReviewController</servlet-name>
    <servlet-class>controller.client.ReviewController</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>ReviewController</servlet-name>
    <url-pattern>/review</url-pattern>
</servlet-mapping>
```

### 3. Tạo thư mục upload
Tạo thư mục `web/uploads/reviews/` để lưu hình ảnh review.

## API Endpoints

### GET /product-detail-with-review?id={productId}
Hiển thị trang chi tiết sản phẩm với reviews (sử dụng servlet mới)

### POST /review
Submit review mới
- Parameters: `action=submit`, `productId`, `rating`, `comment`, `reviewImages`

### POST /review
Delete review
- Parameters: `action=delete`, `reviewId`, `productId`

## Testing

Chạy file test để kiểm tra hệ thống:
```java
src/java/test/TestReviewSystem.java
```

## Lưu ý

1. **Authentication**: User phải đăng nhập để submit review
2. **Validation**: Mỗi user chỉ được review một sản phẩm một lần
3. **File Upload**: Hỗ trợ upload nhiều hình ảnh, max 10MB mỗi file
4. **Soft Delete**: Reviews được soft delete thay vì hard delete
5. **Rating**: Rating từ 1-5 sao, bắt buộc phải chọn
6. **Comment**: Comment không được để trống

## Troubleshooting

### Lỗi thường gặp:
1. **Import errors**: Đảm bảo sử dụng đúng package jakarta.servlet
2. **Database connection**: Kiểm tra kết nối database
3. **File upload**: Kiểm tra quyền ghi thư mục uploads
4. **Session**: Đảm bảo user đã đăng nhập trước khi review

### Debug:
- Kiểm tra log server để xem lỗi chi tiết
- Sử dụng `TestReviewSystem.java` để test từng component
- Kiểm tra database để đảm bảo dữ liệu được lưu đúng 