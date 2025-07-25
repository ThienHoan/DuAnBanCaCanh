# Chức năng Wishlist - Fish Shop

## Tổng quan
Chức năng Wishlist cho phép người dùng lưu các sản phẩm yêu thích để mua sau. Chức năng này bao gồm:

- Thêm/xóa sản phẩm vào/khỏi danh sách yêu thích
- Xem danh sách sản phẩm yêu thích
- Hiển thị số lượng sản phẩm yêu thích trong header
- Toggle trạng thái yêu thích của sản phẩm

## Cấu trúc Files

### Database
- `resources/database/wishlist_schema.sql` - Schema cho bảng wishlist
- Đã thêm vào `resources/database/init.sql`

### Model & DAO
- `src/java/model/entity/Wishlist.java` - Entity class
- `src/java/dao/interfaces/WishlistDAO.java` - DAO interface  
- `src/java/dao/impl/WishlistDAOImpl.java` - DAO implementation

### Service
- `src/java/service/interfaces/WishlistService.java` - Service interface
- `src/java/service/impl/WishlistServiceImpl.java` - Service implementation

### Controller
- `src/java/controller/client/WishlistController.java` - Controller xử lý requests

### Frontend
- `web/client/wishlist.jsp` - Trang hiển thị wishlist
- `web/components/wishlist-button.jsp` - Component button wishlist có thể tái sử dụng
- `web/assets/css/wishlist.css` - CSS cho wishlist
- `web/assets/js/wishlist.js` - JavaScript xử lý wishlist
- `web/wishlist-demo.jsp` - Trang demo test chức năng

## Database Schema

```sql
CREATE TABLE wishlist (
    wishlist_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    
    UNIQUE(user_id, product_id)
);
```

## API Endpoints

### GET /wishlist
- `?action=list` (default) - Hiển thị trang wishlist
- `?action=count` - Lấy số lượng items trong wishlist (JSON)
- `?action=check&productId=X` - Kiểm tra sản phẩm có trong wishlist không (JSON)
- `?action=popular&limit=X` - Lấy sản phẩm được yêu thích nhiều nhất (JSON)

### POST /wishlist
- `action=add&productId=X` - Thêm sản phẩm vào wishlist
- `action=remove&productId=X` hoặc `action=remove&wishlistId=X` - Xóa sản phẩm
- `action=toggle&productId=X` - Toggle trạng thái wishlist
- `action=clear` - Xóa tất cả items trong wishlist

## Cách sử dụng

### 1. Thêm vào trang sản phẩm
```jsp
<!-- Button với text -->
<jsp:include page="components/wishlist-button.jsp">
    <jsp:param name="productId" value="${product.productId}" />
    <jsp:param name="iconOnly" value="false" />
</jsp:include>

<!-- Chỉ icon -->
<jsp:include page="components/wishlist-button.jsp">
    <jsp:param name="productId" value="${product.productId}" />
    <jsp:param name="iconOnly" value="true" />
</jsp:include>
```

### 2. Include CSS và JS
```jsp
<link rel="stylesheet" href="assets/css/wishlist.css">
<script src="assets/js/wishlist.js"></script>
```

### 3. JavaScript API
```javascript
// Thêm vào wishlist
window.wishlistManager.addToWishlist(productId);

// Xóa khỏi wishlist  
window.wishlistManager.removeFromWishlist(productId);

// Toggle wishlist
window.wishlistManager.toggleWishlist(productId);

// Kiểm tra trạng thái
window.wishlistManager.isInWishlist(productId);

// Cập nhật count
window.wishlistManager.updateWishlistCount();
```

## Tính năng

### Frontend
- ✅ Button component có thể tái sử dụng
- ✅ Hiển thị số lượng wishlist trong header
- ✅ Trang wishlist với đầy đủ chức năng CRUD
- ✅ Responsive design
- ✅ Animation và UX tốt
- ✅ AJAX không reload trang

### Backend  
- ✅ RESTful API design
- ✅ Validation dữ liệu
- ✅ Error handling
- ✅ Transaction safety
- ✅ Database constraints

### Security
- ✅ Kiểm tra đăng nhập
- ✅ Kiểm tra quyền sở hữu
- ✅ SQL injection prevention
- ✅ XSS protection

## Test

1. Chạy ứng dụng
2. Truy cập `/wishlist-demo.jsp` để test
3. Đăng nhập để test đầy đủ chức năng
4. Test các tính năng:
   - Thêm/xóa sản phẩm
   - Toggle wishlist
   - Xem trang wishlist
   - Xóa tất cả

## Tích hợp

### Với Header
- Đã update `header.jsp` để hiển thị count và link wishlist
- Load count tự động khi user đăng nhập

### Với trang sản phẩm
- Sử dụng component `wishlist-button.jsp`
- Include CSS và JS cần thiết

### Với hệ thống User
- Kiểm tra đăng nhập
- Liên kết với user ID trong session

## Lưu ý

1. **Yêu cầu đăng nhập**: User phải đăng nhập để sử dụng wishlist
2. **Unique constraint**: Một user chỉ có thể thêm một sản phẩm vào wishlist một lần
3. **Cascade delete**: Khi xóa user hoặc product, wishlist items sẽ bị xóa theo
4. **Performance**: Đã có index trên các cột quan trọng

## Mở rộng tương lai

1. **Share wishlist**: Chia sẻ wishlist với người khác
2. **Multiple wishlists**: Tạo nhiều wishlist theo chủ đề
3. **Wishlist notification**: Thông báo khi sản phẩm yêu thích giảm giá
4. **Export wishlist**: Xuất danh sách ra file
5. **Wishlist analytics**: Thống kê sản phẩm được yêu thích nhiều nhất
