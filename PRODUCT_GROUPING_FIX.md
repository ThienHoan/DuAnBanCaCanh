# Sửa lỗi nhóm sản phẩm theo tên

## Vấn đề
Trước đây, hệ thống yêu cầu các sản phẩm có cùng tên phải có `product_id` liên tiếp nhau để được nhóm lại thành một nhóm. Điều này không thực tế vì:
- Product ID thường được tạo tự động và có thể không liên tiếp
- Khi xóa sản phẩm, các ID sẽ bị gián đoạn
- Khó khăn trong việc quản lý dữ liệu

## Giải pháp
Đã sửa đổi logic nhóm sản phẩm để chỉ dựa vào tên sản phẩm, không cần product_id liên tiếp.

### Thay đổi trong ProductDAO.java

#### 1. Phương thức `getProductsByCategoryId()`
- **Trước**: Sử dụng pagination trực tiếp trong SQL với `OFFSET` và `FETCH NEXT`
- **Sau**: 
  - Lấy tất cả sản phẩm từ database
  - Nhóm theo tên trong Java
  - Áp dụng pagination sau khi nhóm

```java
// Lấy tất cả sản phẩm trước
List<Product> allProducts = new ArrayList<>();
// ... query database ...

// Nhóm theo tên
Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
for (Product product : allProducts) {
    String name = product.getName();
    if (!groupedProducts.containsKey(name)) {
        groupedProducts.put(name, new ArrayList<>());
    }
    groupedProducts.get(name).add(product);
}

// Áp dụng pagination theo nhóm (không phải theo sản phẩm)
List<List<Product>> productGroups = new ArrayList<>(groupedProducts.values());

int startGroupIndex = (page - 1) * pageSize; // pageSize = 12 nhóm
int endGroupIndex = Math.min(startGroupIndex + pageSize, productGroups.size());

if (startGroupIndex < productGroups.size()) {
    // Lấy các nhóm cho trang hiện tại
    List<List<Product>> currentPageGroups = productGroups.subList(startGroupIndex, endGroupIndex);
    
    // Chuyển các nhóm thành danh sách sản phẩm
    for (List<Product> group : currentPageGroups) {
        products.addAll(group);
    }
}
```

#### 2. Phương thức `getTotalProductsByCategoryId()`
- **Trước**: Đếm trực tiếp từ database
- **Sau**: Lấy tất cả sản phẩm, nhóm theo tên, rồi đếm **số nhóm** (không phải số sản phẩm)

### Thay đổi trong CategoryServlet.java
- Cập nhật comment để phản ánh việc sản phẩm đã được nhóm trong DAO
- Logic nhóm vẫn được giữ lại để hiển thị trên frontend

## Ví dụ cụ thể

Giả sử có 4 nhóm sản phẩm:
- **Cá Betta** (3 sản phẩm): ID 1, 2, 3
- **Cá Chép** (3 sản phẩm): ID 4, 5, 6  
- **Cá Rồng** (4 sản phẩm): ID 7, 8, 9, 10
- **Cá Neon** (3 sản phẩm): ID 11, 12, 13

Với `pageSize = 12` (12 nhóm/trang):
- **Trang 1**: Hiển thị tất cả 4 nhóm (13 sản phẩm)
- **Nếu có 15 nhóm**: Trang 1 hiển thị 12 nhóm đầu, Trang 2 hiển thị 3 nhóm còn lại

## Lợi ích
1. **Linh hoạt hơn**: Không cần product_id liên tiếp
2. **Dễ quản lý**: Có thể xóa/sửa sản phẩm mà không ảnh hưởng đến nhóm
3. **Chính xác hơn**: Nhóm dựa trên tên thực tế thay vì ID
4. **Phân trang theo nhóm**: Mỗi trang hiển thị tối đa 12 nhóm sản phẩm
5. **Hiệu suất tốt**: Vẫn duy trì pagination để tránh load quá nhiều dữ liệu

## Cách test
1. Chạy file `test_group_pagination.sql` để kiểm tra logic phân trang theo nhóm
2. Truy cập trang category và xem các sản phẩm có cùng tên có được nhóm lại không
3. Kiểm tra pagination có hoạt động đúng không (mỗi trang hiển thị tối đa 12 nhóm sản phẩm)
4. Test tìm kiếm với các sản phẩm có cùng tên

## Lưu ý
- Thay đổi này chỉ ảnh hưởng đến việc hiển thị sản phẩm trong category
- Các chức năng khác như cart, order vẫn hoạt động bình thường
- Cần rebuild và deploy lại ứng dụng để áp dụng thay đổi 