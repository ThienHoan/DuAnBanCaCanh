# Use Cases - Fish Shop (DuAnBanCaCanh)

## Tổng quan dự án
Fish Shop là một hệ thống thương mại điện tử chuyên bán cá cảnh và các sản phẩm liên quan. Hệ thống bao gồm các chức năng cho khách hàng, quản trị viên và tích hợp thanh toán.

## 1. QUẢN LÝ NGƯỜI DÙNG (User Management)

### UC-001: Đăng ký tài khoản
**Actor:** Khách hàng mới  
**Precondition:** Chưa có tài khoản  
**Main Flow:**
1. Khách hàng truy cập trang đăng ký
2. Nhập thông tin: username, email, password, full_name, phone
3. Hệ thống validate dữ liệu
4. Tạo tài khoản mới với role "customer"
5. Gửi email xác nhận
6. Chuyển hướng đến trang đăng nhập

**Alternative Flow:**
- Email đã tồn tại → Hiển thị thông báo lỗi
- Username đã tồn tại → Hiển thị thông báo lỗi
- Dữ liệu không hợp lệ → Hiển thị lỗi validation

### UC-002: Đăng nhập
**Actor:** Người dùng đã đăng ký  
**Precondition:** Có tài khoản hợp lệ  
**Main Flow:**
1. Người dùng nhập username/email và password
2. Hệ thống xác thực thông tin
3. Tạo session và lưu thông tin user
4. Chuyển hướng đến trang chủ

**Alternative Flow:**
- Thông tin đăng nhập sai → Hiển thị thông báo lỗi
- Tài khoản bị khóa → Hiển thị thông báo tài khoản bị khóa

### UC-003: Đăng nhập bằng Google
**Actor:** Người dùng  
**Precondition:** Có tài khoản Google  
**Main Flow:**
1. Người dùng click "Đăng nhập bằng Google"
2. Chuyển hướng đến Google OAuth
3. Người dùng xác thực với Google
4. Hệ thống nhận thông tin từ Google
5. Tạo hoặc cập nhật tài khoản local
6. Tạo session và chuyển hướng về trang chủ

### UC-004: Quên mật khẩu
**Actor:** Người dùng  
**Precondition:** Có tài khoản với email hợp lệ  
**Main Flow:**
1. Người dùng nhập email
2. Hệ thống tạo reset token
3. Gửi email chứa link reset password
4. Người dùng click link và nhập mật khẩu mới
5. Cập nhật mật khẩu và xóa reset token

### UC-005: Cập nhật thông tin cá nhân
**Actor:** Người dùng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang profile
2. Cập nhật thông tin: full_name, phone, avatar
3. Hệ thống lưu thông tin mới
4. Hiển thị thông báo thành công

## 2. QUẢN LÝ SẢN PHẨM (Product Management)

### UC-006: Xem danh sách sản phẩm
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Khách hàng truy cập trang category
2. Hệ thống hiển thị sản phẩm theo nhóm tên
3. Hỗ trợ phân trang (12 nhóm/trang)
4. Hiển thị thông tin: tên, giá, hình ảnh, rating

**Alternative Flow:**
- Không có sản phẩm → Hiển thị thông báo "Không có sản phẩm"

### UC-007: Tìm kiếm sản phẩm
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng nhập từ khóa tìm kiếm
2. Hệ thống tìm kiếm theo tên sản phẩm
3. Hiển thị kết quả theo nhóm tên
4. Hỗ trợ phân trang kết quả

### UC-008: Xem chi tiết sản phẩm
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng click vào sản phẩm
2. Hiển thị thông tin chi tiết: mô tả, giá, hình ảnh, thuộc tính
3. Hiển thị các biến thể (màu sắc, kích thước)
4. Hiển thị đánh giá và bình luận
5. Cho phép thêm vào giỏ hàng/wishlist

### UC-009: Lọc sản phẩm theo thuộc tính
**Actor:** Khách hàng  
**Precondition:** Đang xem danh sách sản phẩm  
**Main Flow:**
1. Người dùng chọn thuộc tính (màu sắc, kích thước)
2. Hệ thống lọc sản phẩm theo thuộc tính
3. Cập nhật danh sách sản phẩm hiển thị
4. Hiển thị sản phẩm phù hợp với thuộc tính đã chọn

### UC-010: Sắp xếp sản phẩm
**Actor:** Khách hàng  
**Precondition:** Đang xem danh sách sản phẩm  
**Main Flow:**
1. Người dùng chọn tiêu chí sắp xếp (giá tăng/giảm, tên A-Z)
2. Hệ thống sắp xếp sản phẩm theo tiêu chí
3. Cập nhật danh sách hiển thị

## 3. QUẢN LÝ GIỎ HÀNG (Cart Management)

### UC-011: Thêm sản phẩm vào giỏ hàng
**Actor:** Khách hàng  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng chọn sản phẩm và thuộc tính
2. Nhập số lượng
3. Click "Thêm vào giỏ hàng"
4. Hệ thống kiểm tra tồn kho
5. Thêm vào giỏ hàng
6. Hiển thị thông báo thành công

**Alternative Flow:**
- Hết hàng → Hiển thị thông báo "Hết hàng"
- Số lượng vượt quá tồn kho → Hiển thị thông báo lỗi

### UC-012: Xem giỏ hàng
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang giỏ hàng
2. Hiển thị danh sách sản phẩm trong giỏ
3. Hiển thị thông tin: tên, giá, số lượng, tổng tiền
4. Cho phép cập nhật số lượng hoặc xóa sản phẩm

### UC-013: Cập nhật số lượng sản phẩm
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Có sản phẩm trong giỏ hàng  
**Main Flow:**
1. Người dùng thay đổi số lượng
2. Hệ thống kiểm tra tồn kho
3. Cập nhật số lượng và tính lại tổng tiền
4. Hiển thị tổng tiền mới

### UC-014: Xóa sản phẩm khỏi giỏ hàng
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Có sản phẩm trong giỏ hàng  
**Main Flow:**
1. Người dùng click "Xóa" sản phẩm
2. Hệ thống xóa sản phẩm khỏi giỏ hàng
3. Cập nhật tổng tiền
4. Hiển thị thông báo thành công

## 4. QUẢN LÝ WISHLIST

### UC-015: Thêm sản phẩm vào wishlist
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng click icon "Yêu thích" trên sản phẩm
2. Hệ thống thêm sản phẩm vào wishlist
3. Cập nhật trạng thái icon
4. Hiển thị thông báo thành công

### UC-016: Xem danh sách wishlist
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang wishlist
2. Hiển thị danh sách sản phẩm yêu thích
3. Cho phép thêm vào giỏ hàng hoặc xóa khỏi wishlist

### UC-017: Xóa sản phẩm khỏi wishlist
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Có sản phẩm trong wishlist  
**Main Flow:**
1. Người dùng click "Xóa" hoặc icon "Yêu thích"
2. Hệ thống xóa sản phẩm khỏi wishlist
3. Cập nhật danh sách hiển thị

## 5. QUẢN LÝ ĐÁNH GIÁ (Review Management)

### UC-018: Đánh giá sản phẩm
**Actor:** Khách hàng đã mua sản phẩm  
**Precondition:** Đã mua và nhận sản phẩm  
**Main Flow:**
1. Người dùng truy cập trang đánh giá
2. Chọn số sao (1-5)
3. Viết nội dung đánh giá
4. Upload hình ảnh (tùy chọn)
5. Gửi đánh giá
6. Hệ thống lưu và hiển thị đánh giá

### UC-019: Xem đánh giá sản phẩm
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng xem trang chi tiết sản phẩm
2. Hiển thị tổng quan đánh giá (số sao trung bình, số lượng)
3. Hiển thị danh sách đánh giá chi tiết
4. Hỗ trợ phân trang đánh giá

## 6. QUẢN LÝ ĐƠN HÀNG (Order Management)

### UC-020: Tạo đơn hàng
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Có sản phẩm trong giỏ hàng  
**Main Flow:**
1. Người dùng truy cập trang checkout
2. Nhập thông tin giao hàng
3. Chọn phương thức thanh toán
4. Áp dụng mã giảm giá (nếu có)
5. Xác nhận đơn hàng
6. Hệ thống tạo đơn hàng và chuyển đến thanh toán

### UC-021: Thanh toán đơn hàng
**Actor:** Khách hàng  
**Precondition:** Đã tạo đơn hàng  
**Main Flow:**
1. Chọn phương thức thanh toán (VNPay, SePay)
2. Chuyển hướng đến cổng thanh toán
3. Thực hiện thanh toán
4. Nhận kết quả thanh toán
5. Cập nhật trạng thái đơn hàng

### UC-022: Xem lịch sử đơn hàng
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang "Đơn hàng của tôi"
2. Hiển thị danh sách đơn hàng
3. Hiển thị trạng thái từng đơn hàng
4. Cho phép xem chi tiết đơn hàng

### UC-023: Hủy đơn hàng
**Actor:** Khách hàng  
**Precondition:** Đơn hàng chưa được xử lý  
**Main Flow:**
1. Người dùng chọn đơn hàng cần hủy
2. Click "Hủy đơn hàng"
3. Xác nhận hủy
4. Hệ thống cập nhật trạng thái và hoàn tiền (nếu đã thanh toán)

## 7. QUẢN LÝ BLOG

### UC-024: Xem bài viết blog
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang blog
2. Hiển thị danh sách bài viết
3. Hỗ trợ phân trang và tìm kiếm
4. Click vào bài viết để xem chi tiết

### UC-025: Tìm kiếm bài viết
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng nhập từ khóa tìm kiếm
2. Hệ thống tìm kiếm theo tiêu đề và nội dung
3. Hiển thị kết quả tìm kiếm

## 8. QUẢN TRỊ HỆ THỐNG (Admin Management)

### UC-026: Quản lý sản phẩm (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin truy cập trang quản lý sản phẩm
2. Xem danh sách sản phẩm
3. Thêm/sửa/xóa sản phẩm
4. Quản lý hình ảnh và thuộc tính sản phẩm
5. Cập nhật giá và tồn kho

### UC-027: Quản lý đơn hàng (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin xem danh sách đơn hàng
2. Cập nhật trạng thái đơn hàng
3. Xem chi tiết đơn hàng
4. Quản lý vận chuyển

### UC-028: Quản lý người dùng (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin xem danh sách người dùng
2. Khóa/mở khóa tài khoản
3. Xem thông tin chi tiết người dùng
4. Quản lý quyền hạn

### UC-029: Quản lý blog (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin tạo/sửa/xóa bài viết blog
2. Upload hình ảnh cho bài viết
3. Quản lý danh mục blog
4. Xuất bản/lưu nháp bài viết

### UC-030: Quản lý kho hàng (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin xem báo cáo tồn kho
2. Nhập/xuất kho
3. Kiểm kê hàng tồn kho
4. Quản lý nhà cung cấp

## 9. TÍCH HỢP AI

### UC-031: Tư vấn sản phẩm bằng AI
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng truy cập trang AI Agent
2. Nhập câu hỏi về cá cảnh, thức ăn, thiết bị thủy sinh
3. Hệ thống AI phân tích câu hỏi và trả lời
4. Đề xuất sản phẩm phù hợp dựa trên kinh nghiệm người chơi
5. Hiển thị thông tin chi tiết về cách chăm sóc

**Alternative Flow:**
- Câu hỏi về bệnh cá → AI chẩn đoán và đưa ra lời khuyên
- Câu hỏi về thức ăn → AI tư vấn chế độ ăn phù hợp
- Câu hỏi về thiết bị → AI giới thiệu sản phẩm phù hợp

### UC-032: Mua hàng trực tiếp qua AI Chat
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Đã đăng nhập  
**Main Flow:**
1. Người dùng chat với AI về sản phẩm muốn mua
2. AI nhận diện ý định mua hàng từ câu nói tự nhiên
3. AI tự động thêm sản phẩm vào giỏ hàng
4. AI xác nhận thông tin đơn hàng
5. AI hỗ trợ thanh toán trực tiếp qua chat

**Alternative Flow:**
- Người dùng nói "mua 2 cá betta" → AI tự động thêm vào giỏ
- Người dùng nói "xem giỏ hàng" → AI hiển thị sản phẩm trong giỏ
- Người dùng nói "thanh toán" → AI chuyển đến checkout

### UC-033: Thanh toán qua AI với SePay
**Actor:** Khách hàng đã đăng nhập  
**Precondition:** Có sản phẩm trong giỏ hàng  
**Main Flow:**
1. Người dùng yêu cầu thanh toán qua AI
2. AI tạo đơn hàng và chuyển đến SePay
3. AI tạo mã QR thanh toán
4. Người dùng quét mã QR để thanh toán
5. AI kiểm tra trạng thái thanh toán và thông báo kết quả

### UC-034: Tư vấn chuyên sâu về cá cảnh
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng hỏi về kinh nghiệm chơi cá cảnh
2. AI phân tích mức độ kinh nghiệm (beginner/intermediate/advanced)
3. AI đưa ra lời khuyên phù hợp
4. AI đề xuất sản phẩm theo mức độ kinh nghiệm
5. AI cung cấp thông tin chăm sóc chi tiết

### UC-035: Chẩn đoán bệnh cá qua AI
**Actor:** Khách hàng  
**Precondition:** Không cần đăng nhập  
**Main Flow:**
1. Người dùng mô tả triệu chứng bệnh của cá
2. AI phân tích triệu chứng và chẩn đoán bệnh
3. AI đưa ra phương pháp điều trị
4. AI đề xuất thuốc và sản phẩm hỗ trợ
5. AI cung cấp lời khuyên phòng bệnh

### UC-036: Tạo nội dung blog bằng AI
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin nhập chủ đề bài viết về cá cảnh
2. AI tạo nội dung bài viết chi tiết
3. Admin review và chỉnh sửa nội dung
4. AI hỗ trợ tối ưu SEO cho bài viết
5. Xuất bản bài viết lên blog

## 10. QUẢN LÝ KHUYẾN MÃI

### UC-037: Áp dụng mã giảm giá
**Actor:** Khách hàng  
**Precondition:** Có mã giảm giá hợp lệ  
**Main Flow:**
1. Người dùng nhập mã giảm giá
2. Hệ thống kiểm tra tính hợp lệ
3. Áp dụng giảm giá vào đơn hàng
4. Cập nhật tổng tiền

### UC-038: Quản lý khuyến mãi (Admin)
**Actor:** Admin  
**Precondition:** Đã đăng nhập với quyền admin  
**Main Flow:**
1. Admin tạo mã giảm giá mới
2. Thiết lập điều kiện áp dụng
3. Quản lý thời gian hiệu lực
4. Theo dõi hiệu quả khuyến mãi

## Các tính năng đặc biệt

### Tính năng nhóm sản phẩm theo tên
- Sản phẩm có cùng tên được nhóm lại thành một nhóm
- Phân trang theo nhóm (12 nhóm/trang)
- Không cần product_id liên tiếp

### Tích hợp thanh toán
- VNPay: Thanh toán trực tuyến
- SePay: Thanh toán qua ví điện tử
- Webhook xử lý kết quả thanh toán

### Hệ thống đánh giá
- Đánh giá bằng sao (1-5)
- Upload hình ảnh đánh giá
- Hiển thị đánh giá trung bình

### Tích hợp AI
- Tư vấn chăm sóc cá cảnh và chẩn đoán bệnh
- Mua hàng trực tiếp qua AI chat
- Thanh toán qua AI với SePay QR
- Tạo nội dung blog tự động
- Đề xuất sản phẩm theo kinh nghiệm người chơi 