# 🤖 AI Agent Setup Guide - Hướng dẫn thiết lập AI Agent

## 📋 Tóm tắt hiện tại

✅ **Đã hoàn thành:**
- AI Agent code (AgentLoop.java, LLM.java, AgentTest.java)
- Web interface (ai-agent.jsp)
- Servlet integration (AIAgentServlet.java)
- Demo Mode hoạt động hoàn hảo
- Các JAR dependencies cần thiết đã có (trừ 2 JAR Jackson)

❌ **Còn thiếu để Real Mode hoạt động:**
- jackson-core-2.19.1.jar
- jackson-annotations-2.19.1.jar

## 🚀 Hướng dẫn thiết lập Real Mode

### Bước 1: Download các JAR còn thiếu

1. **Truy cập Maven Central Repository:**
   - Trang web: https://mvnrepository.com/

2. **Download jackson-core-2.19.1:**
   - Tìm kiếm: "jackson-core 2.19.1"
   - Download file JAR: `jackson-core-2.19.1.jar`

3. **Download jackson-annotations-2.19.1:**
   - Tìm kiếm: "jackson-annotations 2.19.1"
   - Download file JAR: `jackson-annotations-2.19.1.jar`

### Bước 2: Đặt JAR vào đúng vị trí

1. **Copy 2 file JAR vào thư mục:**
   ```
   d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh\build\web\WEB-INF\lib\
   ```

2. **Kiểm tra thư mục lib sau khi copy:**
   Thư mục này phải có:
   - ✅ jackson-databind-2.19.1.jar (đã có)
   - ✅ jackson-core-2.19.1.jar (cần thêm)
   - ✅ jackson-annotations-2.19.1.jar (cần thêm)
   - ✅ Các JAR khác (gson, openai-java, etc.)

### Bước 3: Thiết lập API Key

1. **Tạo file agent.config trong thư mục gốc project:**
   ```
   d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh\agent.config
   ```

2. **Nội dung file agent.config:**
   ```
   # Perplexity API Configuration
   api.key=your_perplexity_api_key_here
   api.url=https://api.perplexity.ai/chat/completions
   model=llama-3.1-sonar-small-128k-online
   ```

3. **Thay thế `your_perplexity_api_key_here` bằng API key thật từ Perplexity**

### Bước 4: Rebuild và Deploy

1. **Trong NetBeans:**
   - Clean and Build project (Shift+F11)
   - Run project (F6)

2. **Hoặc chạy manual:**
   ```powershell
   cd "d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh"
   ant clean
   ant compile
   ant deploy
   ```

## 🎯 Cách sử dụng AI Agent

### Phương pháp 1: Web Interface (Khuyến nghị)

1. **Truy cập giao diện web:**
   ```
   http://localhost:8080/DuAnBanCaCanh/ai-agent.jsp
   ```

2. **Sử dụng:**
   - Nhập yêu cầu vào ô text
   - Chọn mode:
     - **Demo Mode**: Không cần API key, mô phỏng phản hồi
     - **Real Mode**: Sử dụng Perplexity AI API thật
   - Click "Chạy Agent"

### Phương pháp 2: Command Line Testing

1. **Compile và chạy AgentTest:**
   ```powershell
   cd "d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh\src\java"
   javac -cp "..\..\build\web\WEB-INF\lib\*" com\juleswhite\module1\*.java
   java -cp ".;..\..\build\web\WEB-INF\lib\*" com.juleswhite.module1.AgentTest
   ```

2. **Chọn mode và nhập yêu cầu theo hướng dẫn**

## 🛠️ Troubleshooting

### Lỗi "ClassNotFoundException: com.fasterxml.jackson.core"

**Nguyên nhân:** Thiếu jackson-core-2.19.1.jar

**Giải pháp:**
1. Download jackson-core-2.19.1.jar từ Maven Central
2. Copy vào `build\web\WEB-INF\lib\`
3. Rebuild project

### Lỗi "ClassNotFoundException: com.fasterxml.jackson.annotation"

**Nguyên nhân:** Thiếu jackson-annotations-2.19.1.jar

**Giải pháp:**
1. Download jackson-annotations-2.19.1.jar từ Maven Central
2. Copy vào `build\web\WEB-INF\lib\`
3. Rebuild project

### Lỗi "API key not configured"

**Nguyên nhân:** Chưa thiết lập API key trong agent.config

**Giải pháp:**
1. Tạo file `agent.config` trong thư mục gốc project
2. Thêm API key Perplexity hợp lệ
3. Restart application

### Real Mode không hoạt động

**Giải pháp theo thứ tự:**
1. Kiểm tra đủ 3 JAR Jackson trong `build\web\WEB-INF\lib\`
2. Kiểm tra file `agent.config` có API key hợp lệ
3. Rebuild project (Clean and Build)
4. Restart server
5. Nếu vẫn lỗi, sử dụng Demo Mode tạm thời

## 📁 File Structure

```
DuAnBanCaCanh/
├── agent.config                          # API configuration
├── src/java/com/juleswhite/module1/      # AI Agent source code
│   ├── AgentLoop.java                    # Main agent logic
│   ├── LLM.java                         # LLM API wrapper
│   ├── AgentTest.java                   # Command line interface
│   ├── Message.java                     # Message model
│   ├── Action.java                      # Action model
│   └── ActionResult.java                # Action result model
├── src/java/controller/client/
│   └── AIAgentServlet.java              # Web servlet for agent
├── web/
│   └── ai-agent.jsp                     # Web interface
├── web/WEB-INF/
│   └── web.xml                          # Servlet mapping
└── build/web/WEB-INF/lib/               # JAR dependencies
    ├── jackson-databind-2.19.1.jar     ✅ Có sẵn
    ├── jackson-core-2.19.1.jar         ❌ Cần thêm
    ├── jackson-annotations-2.19.1.jar  ❌ Cần thêm
    ├── gson-2.10.1.jar                 ✅ Có sẵn
    ├── openai-java-2.12.0.jar          ✅ Có sẵn
    └── ... (other JARs)                 ✅ Có sẵn
```

## 🎉 Test Cases

### Demo Mode Test:
1. Nhập: "List all files in the project"
2. Kết quả mong đợi: Hiển thị danh sách files mô phỏng

### Real Mode Test:
1. Nhập: "Analyze the project structure and tell me about the main components"
2. Kết quả mong đợi: Agent sử dụng Perplexity AI để phân tích thật

## ✅ Checklist hoàn thành

- [ ] Download jackson-core-2.19.1.jar
- [ ] Download jackson-annotations-2.19.1.jar  
- [ ] Copy 2 JAR vào build\web\WEB-INF\lib\
- [ ] Tạo file agent.config với API key
- [ ] Clean and Build project
- [ ] Test Demo Mode
- [ ] Test Real Mode
- [ ] Xác nhận không còn lỗi ClassNotFoundException

**Sau khi hoàn thành checklist trên, AI Agent sẽ hoạt động hoàn hảo ở cả Demo Mode và Real Mode!**
