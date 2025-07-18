# 🚀 Hướng Dẫn Chuyển Giao Dự Án AI Agent

## 📋 Tổng Quan
Dự án này sử dụng **Perplexity AI API** để tạo AI Agent chuyên về bán cá cảnh. Để dự án hoạt động, cần cấu hình API key từ Perplexity.

## 🔑 Cách Lấy API Key Perplexity

### Bước 1: Tạo tài khoản Perplexity
1. Truy cập: https://www.perplexity.ai/
2. Đăng ký tài khoản mới (miễn phí)
3. Đăng nhập vào tài khoản

### Bước 2: Lấy API Key
1. Vào Settings: https://www.perplexity.ai/settings/api
2. Click "Generate New API Key"
3. Copy API key (bắt đầu bằng `pplx-...`)
4. **LƯU Ý**: API key này chỉ hiển thị 1 lần, hãy lưu lại ngay!

## ⚙️ Cách Cấu Hình API Key

### Phương Pháp 1: Biến Môi Trường (Khuyến nghị)

#### Trên Windows:
```powershell
# Cách 1: Set tạm thời (chỉ cho session hiện tại)
$env:PERPLEXITY_API_KEY="pplx-your-api-key-here"

# Cách 2: Set vĩnh viễn
[Environment]::SetEnvironmentVariable("PERPLEXITY_API_KEY", "pplx-your-api-key-here", "User")

# Kiểm tra
echo $env:PERPLEXITY_API_KEY
```

#### Trên Linux/Mac:
```bash
# Thêm vào ~/.bashrc hoặc ~/.zshrc
export PERPLEXITY_API_KEY="pplx-your-api-key-here"

# Reload
source ~/.bashrc
```

### Phương Pháp 2: File Cấu Hình

#### Option A: Sử dụng file agent.config (đã có sẵn)
1. Mở file `agent.config` trong thư mục gốc
2. Thay đổi dòng:
   ```
   PERPLEXITY_API_KEY=pplx-your-api-key-here
   ```

#### Option B: Tạo file perplexity.properties
1. Copy file `perplexity.properties.template` thành `perplexity.properties`
2. Đặt vào thư mục `src/`
3. Sửa:
   ```
   api.key=pplx-your-api-key-here
   ```

### Phương Pháp 3: System Property (khi chạy)
```bash
# Khi chạy với ant
ant run -Dperplexity.api.key="pplx-your-api-key-here"

# Khi chạy Tomcat
export CATALINA_OPTS="-Dperplexity.api.key=pplx-your-api-key-here"
```

## 🛠️ Hướng Dẫn Chuyển Giao

### Cho Người Giao (Bạn)
1. **Xóa API key cá nhân** khỏi các file:
   ```bash
   # Thay API key trong agent.config thành placeholder
   PERPLEXITY_API_KEY=your_perplexity_api_key_here
   ```

2. **Tạo gói chuyển giao**:
   - Copy toàn bộ thư mục dự án
   - Đảm bảo folder `web/WEB-INF/lib/` có đủ thư viện
   - Kèm file hướng dẫn này

3. **Hướng dẫn người nhận**:
   - Gửi link này: `SETUP_FOR_NEW_USER.md`
   - Giải thích cách lấy và set API key

### Cho Người Nhận (Bạn bè)
1. **Lấy API key** theo hướng dẫn phía trên
2. **Chọn 1 trong các cách cấu hình** (khuyến nghị biến môi trường)
3. **Test dự án**:
   ```powershell
   # Chạy build
   ant clean build

   # Deploy lên Tomcat và test
   # Truy cập: http://localhost:8080/yourproject/ai-agent.jsp
   ```

## 🧪 Kiểm Tra Hoạt Động

### Test API Key
1. Mở file: `web/test-module4-integration.jsp`
2. Truy cập: `http://localhost:8080/yourproject/test-module4-integration.jsp`
3. Xem log console để kiểm tra:
   - ✅ `Found API key from...` = thành công
   - ❌ `No API key found` = cần cấu hình lại

### Test AI Agent
1. Truy cập: `http://localhost:8080/yourproject/ai-agent.jsp`
2. Gửi câu hỏi: "Tôi muốn nuôi cá cảnh, bạn có thể tư vấn không?"
3. Kiểm tra response:
   - **Có API key**: Trả lời từ Perplexity AI
   - **Không có API key**: Trả lời demo mode

## 🔧 Xử Lý Sự Cố

### Lỗi "No API key found"
1. Kiểm tra biến môi trường: `echo $env:PERPLEXITY_API_KEY`
2. Restart IDE/Terminal sau khi set biến môi trường
3. Kiểm tra file cấu hình có đúng format không

### Lỗi "API key invalid"
1. Kiểm tra API key có đúng format `pplx-...` không
2. Verify trên trang Perplexity Settings
3. Tạo API key mới nếu cần

### Lỗi thư viện
1. Chạy script: `install_minimal_libs.ps1`
2. Hoặc tải thủ công từ Maven Central

## 📚 Thông Tin Bổ Sung

### Cấu Trúc Dự Án
```
/
├── src/java/ai/           # AI logic
├── web/ai-agent.jsp       # Giao diện chính  
├── web/WEB-INF/lib/       # Thư viện Java
├── agent.config           # File cấu hình
└── build.xml             # Build script
```

### File Quan Trọng
- `src/java/ai/LLM.java` - Kết nối Perplexity API
- `src/java/ai/agents/AquariumAgent.java` - Logic AI Agent
- `src/java/ai/tools/AquariumTools.java` - Tools chuyên biệt
- `web/ai-agent.jsp` - Giao diện người dùng

### API Usage & Pricing
- Perplexity có free tier với giới hạn
- Xem chi tiết: https://docs.perplexity.ai/docs/pricing
- Monitor usage tại dashboard

## 📞 Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra log console (F12 → Console)
2. Kiểm tra Tomcat logs
3. Tham khảo file `AI_AGENT_SETUP_GUIDE.md`

---
*Cập nhật: $(Get-Date -Format "yyyy-MM-dd")*
