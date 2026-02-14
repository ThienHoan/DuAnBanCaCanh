# Tính năng "Mua lại" đơn hàng

## Mô tả
Tính năng cho phép người dùng mua lại các sản phẩm từ đơn hàng đã hủy hoặc đã xác nhận nhận hàng bằng cách thêm tất cả sản phẩm vào giỏ hàng.

## Các thay đổi đã thực hiện

### 1. Giao diện người dùng

#### Trang chi tiết đơn hàng (`order-detail.jsp`)
- Thêm nút "Mua lại" cho đơn hàng có trạng thái `cancelled` hoặc `delivered`
- Nút có màu cam (#f57c00) để phân biệt với các nút khác
- Hiển thị xác nhận trước khi thực hiện

#### Trang danh sách đơn hàng (`orders.jsp`)
- Thêm nút "Mua lại" trong danh sách đơn hàng
- Chỉ hiển thị cho khách hàng (không phải admin)
- Áp dụng cùng điều kiện trạng thái

### 2. Backend Logic

#### OrderServlet.java
- Thêm action `reorder` vào switch case
- Import CartDAO để thao tác với giỏ hàng
- Tạo method `reorderItems()` với các tính năng:
  - Kiểm tra quyền truy cập đơn hàng
  - Xác thực trạng thái đơn hàng (chỉ cho phép `cancelled` hoặc `delivered`)
  - Lấy danh sách sản phẩm từ đơn hàng
  - Kiểm tra tồn kho cho từng sản phẩm
  - Thêm vào giỏ hàng với số lượng tối đa có thể
  - Thông báo kết quả chi tiết

### 3. Xử lý logic

#### Kiểm tra tồn kho
- Kiểm tra số lượng tồn kho hiện tại
- Chỉ thêm số lượng có sẵn trong kho
- Bỏ qua sản phẩm hết hàng

#### Thông báo kết quả
- Thành công: "Đã thêm X sản phẩm vào giỏ hàng"
- Một phần: "Đã thêm X sản phẩm vào giỏ hàng. Y sản phẩm không thể thêm do hết hàng"
- Thất bại: "Không thể thêm sản phẩm nào vào giỏ hàng. Tất cả sản phẩm đều hết hàng"

#### Chuyển hướng
- Thành công: Chuyển đến trang giỏ hàng (`cartClient`)
- Thất bại: Quay lại trang chi tiết đơn hàng

## Cách sử dụng

1. Người dùng đăng nhập vào hệ thống
2. Vào trang "Đơn hàng của tôi"
3. Tìm đơn hàng có trạng thái "Đã hủy" hoặc "Đã giao hàng"
4. Nhấn nút "Mua lại" (màu cam)
5. Xác nhận thao tác
6. Hệ thống sẽ thêm tất cả sản phẩm có sẵn vào giỏ hàng
7. Chuyển đến trang giỏ hàng để tiếp tục mua hàng

## Lưu ý bảo mật

- Kiểm tra quyền truy cập: Chỉ chủ đơn hàng hoặc admin mới có thể mua lại
- Xác thực trạng thái: Chỉ cho phép mua lại đơn hàng đã hoàn thành hoặc hủy
- Kiểm tra tồn kho: Đảm bảo không thêm sản phẩm hết hàng
- Log lỗi: Ghi log chi tiết khi có lỗi xảy ra

## Tương thích

- Hoạt động với tất cả loại đơn hàng (COD, VNPay)
- Tương thích với hệ thống giỏ hàng hiện tại
- Không ảnh hưởng đến các tính năng khác 