# 🖼️ Hướng dẫn thay đổi ảnh Hero Background

## 📋 Tổng quan
Script này giúp bạn thay đổi ảnh nền cho hero section của website một cách dễ dàng và an toàn.

## 🚀 Cách sử dụng

### Cách 1: Script đơn giản (Khuyến nghị)
```powershell
# Chạy từ thư mục css
.\quick-change-hero.ps1 "C:\đường\dẫn\đến\ảnh\mới.jpg"
```

**Ví dụ:**
```powershell
.\quick-change-hero.ps1 "C:\Users\Admin\Desktop\hero_new.jpg"
```

### Cách 2: Script chi tiết
```powershell
# Script với nhiều tùy chọn
.\change-hero-image.ps1 -NewImagePath "C:\đường\dẫn\đến\ảnh\mới.jpg"
```

## 📁 Cấu trúc file

```
web/assets/css/
├── change-hero-image.ps1      # Script chi tiết
├── quick-change-hero.ps1      # Script đơn giản
└── README-HERO-CHANGE.md      # Hướng dẫn này

web/assets/images/
├── hero_bg.jpg                # Ảnh hiện tại
└── hero_bg_backup_*.jpg       # File backup
```

## 🎯 Tính năng

### ✅ Script tự động:
- **Backup file cũ** với timestamp
- **Thay thế file mới** an toàn
- **Kiểm tra lỗi** và thông báo
- **Hiển thị thông tin** file trước/sau

### 🔧 Tùy chọn:
- Tự động backup với tên có timestamp
- Kiểm tra file tồn tại
- Thông báo chi tiết quá trình
- Hướng dẫn các bước tiếp theo

## 📐 Yêu cầu ảnh

### 🎨 Kích thước khuyến nghị:
- **Width**: 1920px (hoặc lớn hơn)
- **Height**: 400-600px
- **Tỷ lệ**: 16:9 hoặc 4:1

### 📦 Định dạng hỗ trợ:
- JPG/JPEG
- PNG
- WebP

### 🎯 Chất lượng:
- **Độ phân giải**: Tối thiểu 1920x400px
- **Kích thước file**: < 500KB (tối ưu)
- **Tương phản**: Tốt để text trắng dễ đọc

## 🔄 Quy trình thay đổi

1. **Chuẩn bị ảnh mới**
   - Đặt ảnh vào thư mục dễ truy cập
   - Đảm bảo kích thước phù hợp

2. **Chạy script**
   ```powershell
   cd DuAnBanCaCanh\web\assets\css
   .\quick-change-hero.ps1 "đường_dẫn_ảnh_mới"
   ```

3. **Kiểm tra kết quả**
   - Mở website
   - Refresh trang (Ctrl+F5)
   - Kiểm tra hero section

## 🛠️ Xử lý sự cố

### ❌ Lỗi thường gặp:

**1. "Không tìm thấy file ảnh"**
```
Giải pháp: Kiểm tra đường dẫn file ảnh
```

**2. "Không có quyền truy cập"**
```
Giải pháp: Chạy PowerShell với quyền Administrator
```

**3. "Ảnh không hiển thị"**
```
Giải pháp: 
- Clear cache trình duyệt (Ctrl+Shift+Delete)
- Kiểm tra tên file: hero_bg.jpg
- Kiểm tra CSS: ../images/hero_bg.jpg
```

### 🔧 Khôi phục file cũ:
```powershell
# Tìm file backup
dir ..\images\hero_bg_backup_*.jpg

# Khôi phục
copy "hero_bg_backup_YYYYMMDD_HHMMSS.jpg" hero_bg.jpg
```

## 📝 Lưu ý quan trọng

1. **Backup tự động**: Script luôn backup file cũ
2. **Tên file**: Phải là `hero_bg.jpg`
3. **Cache trình duyệt**: Có thể cần clear cache
4. **CSS đã cập nhật**: Đường dẫn `../images/hero_bg.jpg`

## 🎨 Gợi ý ảnh phù hợp

### Chủ đề website cá cảnh:
- Ảnh cá cảnh đẹp
- Bể cá thủy sinh
- Cảnh quan dưới nước
- Background xanh dương nhạt

### Kỹ thuật:
- Độ tương phản cao
- Màu sắc hài hòa
- Không quá chi tiết
- Phù hợp với text trắng

---

**💡 Tip**: Test ảnh trên nhiều thiết bị để đảm bảo hiển thị tốt! 