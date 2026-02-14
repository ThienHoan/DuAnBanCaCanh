# Hướng dẫn cài đặt thư viện còn thiếu cho Module4

## Thư viện hiện có
Dự án đã có các thư viện sau trong `web/WEB-INF/lib/`:
- ✅ reflections-0.10.2.jar
- ✅ slf4j-simple-2.0.6.jar  
- ✅ jackson-core-2.19.1.jar
- ✅ jackson-databind-2.19.1.jar
- ✅ jackson-annotations-2.19.1.jar

## Thư viện còn thiếu

### 1. SLF4J API
```
Tên file: slf4j-api-2.0.6.jar
Link tải: https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar
Mô tả: API cốt lõi cho SLF4J logging
```

### 2. Javassist
```
Tên file: javassist-3.29.2-GA.jar
Link tải: https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar
Mô tả: Java bytecode manipulation library (cần cho Reflections)
```

## Cách cài đặt

### Bước 1: Tải các file JAR
1. Mở browser và tải 2 file JAR từ links ở trên
2. Hoặc sử dụng wget/curl nếu có:

```bash
# Tải slf4j-api
wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar

# Tải javassist
wget https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar
```

### Bước 2: Copy vào thư mục lib
Copy 2 file JAR vào thư mục:
```
d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh\web\WEB-INF\lib\
```

### Bước 3: Restart server
1. Stop Tomcat server
2. Clean và rebuild project nếu cần
3. Start lại Tomcat server

## Kiểm tra sau khi cài đặt

### Test 1: Truy cập test page
```
http://localhost:8080/DuAnBanCaCanh/test-module4-integration.jsp
```

### Test 2: Kiểm tra AI Agent
```
http://localhost:8080/DuAnBanCaCanh/ai-agent.jsp
```

### Test 3: Kiểm tra console log
Xem Tomcat console log để đảm bảo không có ClassNotFoundException

## Troubleshooting

### Nếu vẫn lỗi ClassNotFoundException:
1. Kiểm tra file JAR đã được copy đúng vị trí chưa
2. Restart Tomcat hoàn toàn
3. Xóa cache: `work/Catalina/localhost/DuAnBanCaCanh/`
4. Rebuild project từ NetBeans

### Nếu lỗi NoClassDefFoundError:
1. Kiểm tra version compatibility
2. Có thể cần thêm một số dependency khác

### Fallback option:
Nếu module4 vẫn không hoạt động, có thể sử dụng `ai-agent-api.jsp` thay thế mà không cần module4.

## Danh sách đầy đủ thư viện cần thiết cho Module4

```
slf4j-api-2.0.6.jar          ❌ (cần tải)
slf4j-simple-2.0.6.jar       ✅ (đã có)
javassist-3.29.2-GA.jar      ❌ (cần tải)
reflections-0.10.2.jar       ✅ (đã có)
jackson-core-2.19.1.jar      ✅ (đã có)
jackson-databind-2.19.1.jar  ✅ (đã có)
jackson-annotations-2.19.1.jar ✅ (đã có)
```

## Module4 JAR (nếu cần)
Nếu dự án không có module4 JAR, cần thêm:
```
module4-core.jar (hoặc tương tự)
```

Tuy nhiên, trong dự án này, module4 có vẻ được implement trực tiếp trong source code.
