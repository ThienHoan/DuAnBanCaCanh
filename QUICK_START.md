# 🤖 AI Agent Quick Start

## Để chạy AI Agent, bạn cần API key Perplexity:

### 1. Lấy API Key
- Truy cập: https://www.perplexity.ai/settings/api
- Tạo API key mới (miễn phí)
- Copy key (bắt đầu với `pplx-...`)

### 2. Cấu Hình (Chọn 1 cách)

**Cách 1: Biến môi trường (Khuyến nghị)**
```powershell
$env:PERPLEXITY_API_KEY="pplx-your-api-key-here"
```

**Cách 2: File cấu hình**
- Sửa file `agent.config`:
```
PERPLEXITY_API_KEY=pplx-your-api-key-here
```

### 3. Kiểm Tra
```powershell
.\check-api-key.ps1
```

### 4. Chạy Dự Án
```powershell
ant clean build
# Deploy lên Tomcat
# Truy cập: http://localhost:8080/yourproject/ai-agent.jsp
```

---

📖 **Chi tiết**: Xem file `SETUP_FOR_NEW_USER.md`
