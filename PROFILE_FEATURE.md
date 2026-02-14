# Chức năng Profile Người Dùng - Fish Shop

## Tổng quan
Chức năng profile người dùng cho phép khách hàng quản lý thông tin cá nhân, bao gồm xem, chỉnh sửa thông tin, đổi mật khẩu, và upload avatar.

## Tính năng chính

### 1. Dashboard
- **URL**: `/dashboard`
- **Mô tả**: Trang tổng quan hiển thị thông tin cá nhân và thống kê
- **Tính năng**:
  - Hiển thị thông tin cơ bản của người dùng
  - Thống kê sản phẩm yêu thích
  - Tỷ lệ hoàn thiện profile
  - Trạng thái bảo mật tài khoản
  - Hoạt động gần đây
  - Liên kết nhanh đến các chức năng chính

### 2. Xem thông tin cá nhân
- **URL**: `/profile`
- **Mô tả**: Hiển thị chi tiết thông tin cá nhân
- **Tính năng**:
  - Avatar với fallback
  - Thông tin cơ bản (tên, email, điện thoại)
  - Thông tin tài khoản (ngày tạo, đăng nhập cuối)
  - Thống kê sản phẩm yêu thích

### 3. Chỉnh sửa thông tin
- **URL**: `/profile?action=edit`
- **Mô tả**: Form chỉnh sửa thông tin cá nhân
- **Tính năng**:
  - Validation form client-side và server-side
  - Upload avatar với preview
  - Cập nhật thông tin cơ bản
  - Thông báo kết quả thao tác

### 4. Đổi mật khẩu
- **URL**: `/profile?action=change-password`
- **Mô tả**: Form đổi mật khẩu an toàn
- **Tính năng**:
  - Xác thực mật khẩu hiện tại
  - Kiểm tra độ mạnh mật khẩu mới
  - Xác nhận mật khẩu
  - Mã hóa mật khẩu bằng hash

## Cấu trúc file

### Backend (Java)
```
src/java/controller/client/
├── ProfileController.java      # Controller chính xử lý profile
├── DashboardController.java    # Controller riêng cho dashboard

src/java/model/entity/
├── User.java                   # Entity User đã có đầy đủ fields

src/java/dao/
├── interfaces/UserDAO.java     # Interface DAO
├── impl/UserDAOImpl.java       # Implementation DAO

src/java/service/
├── interfaces/UserService.java # Interface Service
├── impl/UserServiceImpl.java   # Implementation Service
```

### Frontend (JSP)
```
web/client/account/
├── dashboard.jsp               # Dashboard tổng quan
├── profile.jsp                 # Xem thông tin cá nhân
├── edit-profile.jsp            # Chỉnh sửa thông tin
├── change-password.jsp         # Đổi mật khẩu

web/components/
├── user-avatar.jsp             # Component hiển thị avatar

web/assets/
├── css/profile.css             # CSS cho trang profile
├── js/profile.js               # JavaScript cho tương tác
```

### Uploads
```
web/uploads/
├── avatars/                    # Thư mục chứa avatar người dùng
```

## Tính năng bảo mật

### 1. Authentication
- Kiểm tra đăng nhập trước khi truy cập
- Redirect về trang login nếu chưa đăng nhập
- Session management

### 2. Validation
- **Client-side**: JavaScript validation real-time
- **Server-side**: Validation trong controller
- **File upload**: Kiểm tra định dạng và kích thước file

### 3. Password Security
- Hash mật khẩu bằng bcrypt hoặc tương tự
- Kiểm tra độ mạnh mật khẩu
- Xác thực mật khẩu hiện tại khi đổi

## Responsive Design
- Mobile-first approach
- Responsive grid layout
- Touch-friendly interface
- Optimized cho các thiết bị khác nhau

## UX/UI Features

### 1. Animations
- Smooth transitions
- Hover effects
- Loading states
- Fade-in animations

### 2. Feedback
- Success/error messages
- Form validation feedback
- Loading indicators
- Progress bars

### 3. Accessibility
- Semantic HTML
- ARIA labels
- Keyboard navigation
- Screen reader support

## Menu Navigation
Dropdown menu trong header với các tùy chọn:
- Dashboard
- Thông tin cá nhân
- Chỉnh sửa thông tin
- Đổi mật khẩu
- Danh sách yêu thích
- Admin Dashboard (nếu là admin)
- Đăng xuất

## JavaScript Features

### 1. Form Validation
- Real-time validation
- Email format check
- Phone number validation
- Password strength meter

### 2. File Upload
- Drag & drop support
- File preview
- Size and format validation
- Progress indication

### 3. AJAX Integration
- Asynchronous form submission
- Dynamic content loading
- Wishlist count update
- Profile stats loading

## CSS Features

### 1. Modern Design
- Gradient backgrounds
- Card-based layout
- Rounded corners
- Box shadows

### 2. Interactive Elements
- Hover animations
- Smooth transitions
- Loading states
- Progress indicators

### 3. Responsive Grid
- Flexbox layout
- CSS Grid
- Mobile breakpoints
- Adaptive design

## API Endpoints

### Profile Controller
- `GET /profile` - Xem thông tin cá nhân
- `GET /profile?action=edit` - Form chỉnh sửa
- `GET /profile?action=change-password` - Form đổi mật khẩu
- `POST /profile?action=update-profile` - Cập nhật thông tin
- `POST /profile?action=update-avatar` - Upload avatar
- `POST /profile?action=change-password` - Đổi mật khẩu

### Dashboard Controller
- `GET /dashboard` - Dashboard tổng quan

## Tích hợp với hệ thống

### 1. Wishlist Integration
- Hiển thị số lượng sản phẩm yêu thích
- Link đến trang wishlist
- AJAX loading count

### 2. Header Integration
- Dropdown menu tài khoản
- Avatar display
- User greeting

### 3. Admin Integration
- Phân quyền admin
- Link đến admin dashboard
- Role-based features

## Hướng dẫn sử dụng

### 1. Cho Developer
1. Đảm bảo database có đầy đủ fields cho User
2. Upload folder `web/uploads/avatars/` phải có quyền ghi
3. Cấu hình proper session management
4. Test trên nhiều trình duyệt

### 2. Cho End User
1. Đăng nhập vào hệ thống
2. Click vào tên người dùng ở header
3. Chọn Dashboard hoặc Thông tin cá nhân
4. Sử dụng các tính năng có sẵn

## Troubleshooting

### Common Issues
1. **Avatar không hiển thị**: Kiểm tra đường dẫn và quyền thư mục
2. **Form validation fails**: Kiểm tra JavaScript console
3. **Session expired**: Cần đăng nhập lại
4. **File upload error**: Kiểm tra kích thước và định dạng file

### Debug Tips
1. Bật browser developer tools
2. Kiểm tra console errors
3. Verify network requests
4. Check server logs

## Future Enhancements

### 1. Possible Additions
- Order history integration
- Address book management
- Notification preferences
- Two-factor authentication
- Social media login
- Profile privacy settings

### 2. Performance Optimizations
- Image compression
- Lazy loading
- Caching strategies
- CDN integration

## Conclusion
Chức năng profile người dùng đã được implement hoàn chỉnh với đầy đủ tính năng cần thiết, bảo mật tốt, và UX thân thiện. Hệ thống có thể mở rộng thêm các tính năng khác trong tương lai.
