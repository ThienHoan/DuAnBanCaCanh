# Hướng dẫn chạy AI Agent

## Cách 1: Chạy qua Ant Build
```bash
# Build và compile project
ant clean compile

# Chạy AI Agent 
ant run-agent

# Test với sample request
ant test-agent
```

## Cách 2: Chạy trực tiếp trong NetBeans
1. Mở project trong NetBeans
2. Right-click vào `AgentRunner.java`
3. Chọn "Run File" (hoặc Shift+F6)

## Cách 3: Chạy qua Terminal/Command Prompt
```bash
# Compile trước
javac -cp "path/to/gson-2.10.1.jar" src/java/com/juleswhite/module1/*.java

# Chạy
java -cp "build/classes:path/to/gson-2.10.1.jar" com.juleswhite.module1.AgentRunner
```

## Cách sử dụng Agent:

1. **Liệt kê files**: 
   - "List all files in current directory"
   - "Show me all files"

2. **Đọc file**: 
   - "Read the content of build.xml"
   - "Show me what's in README.md"

3. **Kết hợp**:
   - "List files then read build.xml"
   - "Find all .java files and read the first one"

4. **Kết thúc**:
   - Agent sẽ tự động kết thúc khi hoàn thành task
   - Hoặc type "exit" để thoát manual

## Lưu ý:
- Agent cần kết nối internet để gọi Perplexity AI API
- API key đã được hardcode trong LLM.java (dòng 26)
- Agent chỉ có thể thao tác trong thư mục hiện tại (security)
- Tối đa 10 iterations per session

## Ví dụ session:
```
=== AI Agent Runner ===
Nhập yêu cầu của bạn: List all files then tell me about build.xml

--- Bắt đầu thực thi Agent ---
Agent thinking...
Agent response: I'll help you list the files and then read build.xml...
Action result: {success=true, data=[...]}
--- Kết thúc thực thi Agent ---
```
