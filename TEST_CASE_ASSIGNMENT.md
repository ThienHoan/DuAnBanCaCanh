# PHÂN CHIA TEST CASES CHO NHÓM - DỰ ÁN BÁN CÁ CẢNH

## TỔNG QUAN DỰ ÁN
Dự án là một website bán cá cảnh với các chức năng:
- Authentication & Authorization
- Quản lý sản phẩm và danh mục
- Giỏ hàng và đơn hàng
- Wishlist
- Blog và tin tức
- Admin dashboard
- Báo cáo và thống kê

---

## 1. THIỆN (LEADER) - AUTHENTICATION & ADMIN DASHBOARD

### 🔐 **Authentication Testing**

#### **Login Functionality**
- **TC_AUTH_001**: Test đăng nhập hợp lệ với username/password
- **TC_AUTH_002**: Test đăng nhập với email thay vì username
- **TC_AUTH_003**: Test đăng nhập sai password
- **TC_AUTH_004**: Test đăng nhập với tài khoản không tồn tại
- **TC_AUTH_005**: Test remember me checkbox
- **TC_AUTH_006**: Test Google OAuth login
- **TC_AUTH_007**: Test session timeout và auto-logout

#### **Registration Testing**
- **TC_REG_001**: Test đăng ký với thông tin hợp lệ
- **TC_REG_002**: Test đăng ký với username đã tồn tại
- **TC_REG_003**: Test đăng ký với email đã tồn tại
- **TC_REG_004**: Test validation các trường bắt buộc
- **TC_REG_005**: Test format email không hợp lệ
- **TC_REG_006**: Test mật khẩu không khớp
- **TC_REG_007**: Test agree terms checkbox

#### **Password Management**
- **TC_PWD_001**: Test forgot password flow
- **TC_PWD_002**: Test reset password với token hợp lệ
- **TC_PWD_003**: Test reset password với token expired
- **TC_PWD_004**: Test change password trong profile
- **TC_PWD_005**: Test password encryption/hashing

### 🛡️ **Authorization Testing**
- **TC_AUTH_008**: Test role-based redirection (admin vs user)
- **TC_AUTH_009**: Test truy cập admin pages với role user
- **TC_AUTH_010**: Test truy cập protected pages khi chưa login
- **TC_AUTH_011**: Test RoleBasedFilter functionality
- **TC_AUTH_012**: Test AuthenticationFilter functionality

### 🎛️ **Admin Dashboard Testing**

#### **Dashboard Overview**
- **TC_ADMIN_001**: Test hiển thị tổng quan dashboard
- **TC_ADMIN_002**: Test thống kê tổng số users, products, orders
- **TC_ADMIN_003**: Test revenue statistics (today, monthly)
- **TC_ADMIN_004**: Test recent orders display
- **TC_ADMIN_005**: Test top products display
- **TC_ADMIN_006**: Test order status statistics

#### **Admin Panel Navigation**
- **TC_ADMIN_007**: Test navigation menu trong admin panel
- **TC_ADMIN_008**: Test breadcrumb navigation
- **TC_ADMIN_009**: Test responsive design cho admin pages
- **TC_ADMIN_010**: Test admin logout functionality

#### **Database Management**
- **TC_DB_001**: Test reset identity functionality
- **TC_DB_002**: Test database connection status
- **TC_DB_003**: Test data consistency checks
- **TC_DB_004**: Test backup/restore functionality (nếu có)

### 📊 **Reporting & Analytics**
- **TC_REPORT_001**: Test monthly revenue reports
- **TC_REPORT_002**: Test user activity reports
- **TC_REPORT_003**: Test product performance reports
- **TC_REPORT_004**: Test export functionality (PDF/Excel)
- **TC_REPORT_005**: Test date range filtering for reports

---

## 2. KHA - QUẢN LÝ SẢN PHẨM & GIỎ HÀNG

### 🛍️ **Product Management Testing**

#### **Product CRUD Operations**
- **TC_PROD_001**: Test tạo sản phẩm mới với thông tin hợp lệ
- **TC_PROD_002**: Test tạo sản phẩm với thông tin thiếu/không hợp lệ
- **TC_PROD_003**: Test cập nhật thông tin sản phẩm
- **TC_PROD_004**: Test xóa sản phẩm (soft delete)
- **TC_PROD_005**: Test restore sản phẩm đã xóa
- **TC_PROD_006**: Test bulk operations (xóa nhiều sản phẩm)

#### **Product Information Management**
- **TC_PROD_007**: Test thêm/sửa/xóa product details
- **TC_PROD_008**: Test product attributes management
- **TC_PROD_009**: Test product pricing (price, sale_price)
- **TC_PROD_010**: Test stock quantity management
- **TC_PROD_011**: Test product status (active/inactive)
- **TC_PROD_012**: Test featured products functionality

#### **Product Image Management**
- **TC_IMG_001**: Test upload single image
- **TC_IMG_002**: Test upload multiple images
- **TC_IMG_003**: Test image format validation (jpg, png, gif)
- **TC_IMG_004**: Test image size validation
- **TC_IMG_005**: Test delete product images
- **TC_IMG_006**: Test image display on frontend
- **TC_IMG_007**: Test image optimization/compression

#### **Product Display**
- **TC_DISP_001**: Test product detail page display
- **TC_DISP_002**: Test product listing page
- **TC_DISP_003**: Test product image gallery
- **TC_DISP_004**: Test product information tabs
- **TC_DISP_005**: Test related products display
- **TC_DISP_006**: Test product reviews section

### 🛒 **Shopping Cart Testing**

#### **Cart Functionality**
- **TC_CART_001**: Test thêm sản phẩm vào giỏ hàng
- **TC_CART_002**: Test thay đổi số lượng sản phẩm trong giỏ
- **TC_CART_003**: Test xóa sản phẩm khỏi giỏ hàng
- **TC_CART_004**: Test clear all cart items
- **TC_CART_005**: Test cart persistence (session/cookie)
- **TC_CART_006**: Test cart cho user chưa đăng nhập
- **TC_CART_007**: Test merge cart khi user đăng nhập

#### **Cart Calculations**
- **TC_CALC_001**: Test tính tổng giá trị giỏ hàng
- **TC_CALC_002**: Test áp dụng discount/coupon
- **TC_CALC_003**: Test tính phí shipping
- **TC_CALC_004**: Test tính thuế (nếu có)
- **TC_CALC_005**: Test final amount calculation

#### **Cart Display**
- **TC_CART_008**: Test hiển thị cart trong header
- **TC_CART_009**: Test cart page display
- **TC_CART_010**: Test cart item count update
- **TC_CART_011**: Test empty cart message
- **TC_CART_012**: Test continue shopping functionality

---

## 3. TRƯỜNG - DANH MỤC & TRANG CHỦ

### 📂 **Category Management Testing**

#### **Category CRUD**
- **TC_CAT_001**: Test tạo danh mục mới
- **TC_CAT_002**: Test cập nhật thông tin danh mục
- **TC_CAT_003**: Test xóa danh mục
- **TC_CAT_004**: Test activate/deactivate danh mục
- **TC_CAT_005**: Test slug generation từ tên danh mục
- **TC_CAT_006**: Test danh mục cha-con (nếu có hierarchy)

#### **Category Display**
- **TC_CAT_007**: Test hiển thị danh mục trên menu
- **TC_CAT_008**: Test danh mục trong sidebar
- **TC_CAT_009**: Test breadcrumb cho danh mục
- **TC_CAT_010**: Test responsive design cho category pages

### 🏠 **Homepage Testing**

#### **Homepage Layout**
- **TC_HOME_001**: Test layout tổng thể của trang chủ
- **TC_HOME_002**: Test header navigation
- **TC_HOME_003**: Test footer display
- **TC_HOME_004**: Test responsive design (mobile, tablet)
- **TC_HOME_005**: Test loading performance

#### **Homepage Content**
- **TC_HOME_006**: Test featured products section
- **TC_HOME_007**: Test categories showcase
- **TC_HOME_008**: Test promotional banners
- **TC_HOME_009**: Test latest blog posts
- **TC_HOME_010**: Test newsletter subscription
- **TC_HOME_011**: Test social media links

### 🔍 **Search & Filter Testing**

#### **Product Search**
- **TC_SEARCH_001**: Test tìm kiếm theo tên sản phẩm
- **TC_SEARCH_002**: Test tìm kiếm theo mô tả
- **TC_SEARCH_003**: Test tìm kiếm với từ khóa rỗng
- **TC_SEARCH_004**: Test tìm kiếm với ký tự đặc biệt
- **TC_SEARCH_005**: Test search suggestions/autocomplete
- **TC_SEARCH_006**: Test no results found scenario

#### **Product Filtering**
- **TC_FILTER_001**: Test filter theo category
- **TC_FILTER_002**: Test filter theo price range
- **TC_FILTER_003**: Test filter theo brand (nếu có)
- **TC_FILTER_004**: Test filter theo availability
- **TC_FILTER_005**: Test multiple filters combination
- **TC_FILTER_006**: Test clear all filters

#### **Product Sorting**
- **TC_SORT_001**: Test sort by price (low to high, high to low)
- **TC_SORT_002**: Test sort by name (A-Z, Z-A)
- **TC_SORT_003**: Test sort by newest/oldest
- **TC_SORT_004**: Test sort by popularity
- **TC_SORT_005**: Test sort by rating

### 🌟 **Featured Products**
- **TC_FEAT_001**: Test hiển thị featured products
- **TC_FEAT_002**: Test set/unset featured status
- **TC_FEAT_003**: Test featured products trong homepage
- **TC_FEAT_004**: Test featured products limit/pagination

---

## 4. SƠN - QUẢN LÝ ĐƠN HÀNG & THANH TOÁN

### 📦 **Order Management Testing**

#### **Order Creation**
- **TC_ORDER_001**: Test tạo đơn hàng từ cart
- **TC_ORDER_002**: Test order với user đã đăng nhập
- **TC_ORDER_003**: Test order với guest user
- **TC_ORDER_004**: Test order validation (address, payment)
- **TC_ORDER_005**: Test inventory check khi tạo order
- **TC_ORDER_006**: Test order number generation

#### **Order CRUD Operations**
- **TC_ORDER_007**: Test xem chi tiết đơn hàng
- **TC_ORDER_008**: Test cập nhật trạng thái đơn hàng
- **TC_ORDER_009**: Test hủy đơn hàng
- **TC_ORDER_010**: Test restore đơn hàng đã hủy
- **TC_ORDER_011**: Test order history cho user
- **TC_ORDER_012**: Test search/filter orders trong admin

#### **Order Status Management**
- **TC_STATUS_001**: Test chuyển đổi trạng thái: pending → confirmed
- **TC_STATUS_002**: Test chuyển đổi trạng thái: confirmed → processing
- **TC_STATUS_003**: Test chuyển đổi trạng thái: processing → shipped
- **TC_STATUS_004**: Test chuyển đổi trạng thái: shipped → delivered
- **TC_STATUS_005**: Test chuyển đổi trạng thái: any → cancelled
- **TC_STATUS_006**: Test trạng thái returned/refunded

### 💳 **Payment Testing**

#### **Payment Methods**
- **TC_PAY_001**: Test thanh toán COD (Cash on Delivery)
- **TC_PAY_002**: Test thanh toán online banking
- **TC_PAY_003**: Test thanh toán qua ví điện tử
- **TC_PAY_004**: Test thanh toán credit/debit card
- **TC_PAY_005**: Test payment gateway integration
- **TC_PAY_006**: Test payment failure scenarios

#### **Payment Processing**
- **TC_PAY_007**: Test payment amount calculation
- **TC_PAY_008**: Test payment confirmation
- **TC_PAY_009**: Test payment receipt generation
- **TC_PAY_010**: Test refund processing
- **TC_PAY_011**: Test payment status updates
- **TC_PAY_012**: Test security của payment data

### 🚚 **Shipping Management**

#### **Shipping Configuration**
- **TC_SHIP_001**: Test shipping fee calculation
- **TC_SHIP_002**: Test shipping methods selection
- **TC_SHIP_003**: Test shipping address validation
- **TC_SHIP_004**: Test multiple shipping addresses
- **TC_SHIP_005**: Test shipping zones/regions
- **TC_SHIP_006**: Test free shipping conditions

#### **Shipping Tracking**
- **TC_TRACK_001**: Test order tracking number generation
- **TC_TRACK_002**: Test tracking status updates
- **TC_TRACK_003**: Test tracking notifications
- **TC_TRACK_004**: Test delivery confirmation
- **TC_TRACK_005**: Test shipping partner integration

### 📊 **Order Reports**
- **TC_REP_ORDER_001**: Test daily orders report
- **TC_REP_ORDER_002**: Test monthly revenue report
- **TC_REP_ORDER_003**: Test payment method statistics
- **TC_REP_ORDER_004**: Test shipping performance report
- **TC_REP_ORDER_005**: Test cancelled orders analysis

### 🔔 **Order Notifications**
- **TC_NOTIF_001**: Test email notifications cho order status changes
- **TC_NOTIF_002**: Test SMS notifications (nếu có)
- **TC_NOTIF_003**: Test admin notifications cho new orders
- **TC_NOTIF_004**: Test notification templates
- **TC_NOTIF_005**: Test notification delivery status

---

## 5. VŨ - QUẢN LÝ NGƯỜI DÙNG & TÍNH NĂNG BỔ SUNG

### 👥 **User Management Testing**

#### **User CRUD Operations**
- **TC_USER_001**: Test tạo user mới trong admin
- **TC_USER_002**: Test cập nhật thông tin user
- **TC_USER_003**: Test xóa/vô hiệu hóa user
- **TC_USER_004**: Test khôi phục user đã xóa
- **TC_USER_005**: Test bulk user operations
- **TC_USER_006**: Test user role assignment

#### **User Profile Management**
- **TC_PROFILE_001**: Test cập nhật profile information
- **TC_PROFILE_002**: Test upload/change avatar
- **TC_PROFILE_003**: Test change password trong profile
- **TC_PROFILE_004**: Test profile completion percentage
- **TC_PROFILE_005**: Test multiple addresses management
- **TC_PROFILE_006**: Test profile data validation

#### **User Statistics**
- **TC_USER_007**: Test user registration statistics
- **TC_USER_008**: Test user activity tracking
- **TC_USER_009**: Test user purchase history
- **TC_USER_010**: Test user engagement metrics

### ⭐ **Product Review Testing**

#### **Review Management**
- **TC_REVIEW_001**: Test user gửi review cho sản phẩm
- **TC_REVIEW_002**: Test review với rating (1-5 stars)
- **TC_REVIEW_003**: Test review với hình ảnh
- **TC_REVIEW_004**: Test edit/delete review của user
- **TC_REVIEW_005**: Test admin moderate reviews
- **TC_REVIEW_006**: Test review approval/rejection

#### **Review Display**
- **TC_REVIEW_007**: Test hiển thị reviews trên product page
- **TC_REVIEW_008**: Test review statistics (average rating)
- **TC_REVIEW_009**: Test sort reviews by date/rating
- **TC_REVIEW_010**: Test helpful votes cho reviews
- **TC_REVIEW_011**: Test review pagination

### ❤️ **Wishlist Testing**

#### **Wishlist Operations**
- **TC_WISH_001**: Test thêm sản phẩm vào wishlist
- **TC_WISH_002**: Test xóa sản phẩm khỏi wishlist
- **TC_WISH_003**: Test toggle wishlist status
- **TC_WISH_004**: Test clear all wishlist items
- **TC_WISH_005**: Test wishlist cho user chưa đăng nhập
- **TC_WISH_006**: Test wishlist persistence

#### **Wishlist Display**
- **TC_WISH_007**: Test wishlist page display
- **TC_WISH_008**: Test wishlist count trong header
- **TC_WISH_009**: Test wishlist icon status (filled/empty heart)
- **TC_WISH_010**: Test move item from wishlist to cart
- **TC_WISH_011**: Test share wishlist (nếu có)

#### **Wishlist Analytics**
- **TC_WISH_012**: Test most wished products
- **TC_WISH_013**: Test wishlist statistics
- **TC_WISH_014**: Test wishlist conversion rate

### 📦 **Inventory Management**

#### **Stock Management**
- **TC_STOCK_001**: Test cập nhật stock quantity
- **TC_STOCK_002**: Test low stock alerts
- **TC_STOCK_003**: Test out-of-stock notifications
- **TC_STOCK_004**: Test stock reservation during checkout
- **TC_STOCK_005**: Test automatic stock updates after order
- **TC_STOCK_006**: Test bulk stock import/export

#### **Inventory Reports**
- **TC_INV_001**: Test current stock levels report
- **TC_INV_002**: Test stock movement history
- **TC_INV_003**: Test reorder point alerts
- **TC_INV_004**: Test dead stock analysis
- **TC_INV_005**: Test inventory valuation report

---

## ⚠️ **LƯU Ý QUAN TRỌNG**

### **Phân chia trách nhiệm chéo:**
1. **Thiện**: Hỗ trợ tất cả thành viên với integration testing
2. **Mỗi thành viên**: Phải test liên kết với các module khác
3. **User Acceptance Testing**: Tất cả cùng thực hiện

### **Tools cần sử dụng:**
- **Unit Testing**: JUnit
- **Integration Testing**: Selenium WebDriver
- **Database Testing**: SQL scripts
- **API Testing**: Postman/RestAssured
- **Performance Testing**: JMeter (optional)

### **Test Data Management:**
- Mỗi thành viên tạo test data riêng
- Sử dụng database test riêng biệt
- Backup/restore data trước mỗi test cycle

### **Reporting:**
- Mỗi thành viên tạo test report riêng
- Thiện tổng hợp final test report
- Track bugs/issues qua GitHub Issues hoặc Jira

### **Timeline gợi ý:**
- **Week 1**: Viết test cases chi tiết
- **Week 2**: Implement unit tests
- **Week 3**: Integration testing
- **Week 4**: User acceptance testing & bug fixes

**Total Test Cases: ~175 test cases** (phân chia cân bằng cho 5 thành viên)
