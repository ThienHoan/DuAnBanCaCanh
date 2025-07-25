# THƯ VIỆN CẦN THIẾT CHO MODULE4 VỚI PERPLEXITY

## TÓM TẮT TÌNH HÌNH

### LỖI HIỆN TẠI
```
java.lang.ClassNotFoundException: com.fasterxml.jackson.module.kotlin.ExtensionsKt
```

### NGUYÊN NHÂN
- Module4 đang dùng OpenAI Client (openai-java-2.12.0.jar) 
- OpenAI Client được viết bằng Kotlin → cần Kotlin libraries
- Nhưng bạn muốn dùng Perplexity thay vì OpenAI

## GIẢI PHÁP 1: SỬA CODE ĐỂ DÙNG PERPLEXITY

### Sửa file `src/java/ai/LLM.java`
Thay thế OpenAI Client bằng Perplexity API call:

```java
// Thay thế phần này:
// OpenAIClient client = OpenAIOkHttpClient.fromEnv();

// Bằng Perplexity API call:
String perplexityResponse = callPerplexityAPI(prompt);
```

### Cấu hình Perplexity API
```java
private String callPerplexityAPI(Prompt prompt) {
    // HTTP request tới Perplexity API
    // Sử dụng các thư viện HTTP có sẵn như OkHttp hoặc Apache HttpClient
}
```

## GIẢI PHÁP 2: THÊM ĐẦY ĐỦ THƯ VIỆN KOTLIN (NẾU VẪN MUỐN DÙNG OPENAI)

### Danh sách thư viện cần tải:

#### 1. Core Dependencies (ĐÃ CÓ)
```
✅ slf4j-simple-2.0.6.jar
✅ reflections-0.10.2.jar  
✅ jackson-core-2.19.1.jar
✅ jackson-databind-2.19.1.jar
✅ jackson-annotations-2.19.1.jar
```

#### 2. Missing Core Dependencies (CẦN TẢI)
```
❌ slf4j-api-2.0.6.jar
Link: https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar

❌ javassist-3.29.2-GA.jar  
Link: https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar
```

#### 3. Kotlin Dependencies (CHỈ CẦN NẾU DÙNG OPENAI)
```
❌ kotlin-stdlib-1.9.10.jar
Link: https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib/1.9.10/kotlin-stdlib-1.9.10.jar

❌ kotlin-stdlib-common-1.9.10.jar
Link: https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib-common/1.9.10/kotlin-stdlib-common-1.9.10.jar

❌ kotlin-reflect-1.9.10.jar
Link: https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-reflect/1.9.10/kotlin-reflect-1.9.10.jar

❌ jackson-module-kotlin-2.15.2.jar
Link: https://repo1.maven.org/maven2/com/fasterxml/jackson/module/jackson-module-kotlin/2.15.2/jackson-module-kotlin-2.15.2.jar

❌ annotations-13.0.jar
Link: https://repo1.maven.org/maven2/org/jetbrains/annotations/13.0/annotations-13.0.jar
```

## KHUYẾN NGHỊ: SỬA CODE ĐỂ DÙNG PERPLEXITY

### Lý do:
1. **Đơn giản hơn** - Không cần Kotlin dependencies
2. **Nhẹ hơn** - Ít thư viện hơn  
3. **Kiểm soát tốt hơn** - Tự viết API call
4. **Ổn định hơn** - Không phụ thuộc OpenAI SDK

### Thư viện tối thiểu cần thiết:
```
✅ slf4j-api-2.0.6.jar           (cho logging)
✅ slf4j-simple-2.0.6.jar        (đã có)
✅ javassist-3.29.2-GA.jar       (cho Reflections)
✅ reflections-0.10.2.jar        (đã có)
✅ jackson-core-2.19.1.jar       (đã có)
✅ jackson-databind-2.19.1.jar   (đã có)
✅ jackson-annotations-2.19.1.jar (đã có)
✅ okhttp-4.10.0.jar             (đã có - cho HTTP calls)
```

## SCRIPT TẢI THƯ VIỆN TỐI THIỂU

```powershell
# Tải chỉ 2 thư viện còn thiếu
$libPath = "web\WEB-INF\lib"

$requiredLibs = @(
    @{
        name = "SLF4J API"
        filename = "slf4j-api-2.0.6.jar"
        url = "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar"
    },
    @{
        name = "Javassist"
        filename = "javassist-3.29.2-GA.jar"
        url = "https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar"
    }
)

foreach ($lib in $requiredLibs) {
    $filePath = Join-Path $libPath $lib.filename
    if (-not (Test-Path $filePath)) {
        Write-Host "Downloading $($lib.name)..." -ForegroundColor Yellow
        Invoke-WebRequest -Uri $lib.url -OutFile $filePath
        Write-Host "✅ Downloaded: $($lib.filename)" -ForegroundColor Green
    }
}
```

## BƯỚC TIẾP THEO

### Option A: Sửa LLM.java để dùng Perplexity
1. Tải 2 thư viện tối thiểu: slf4j-api, javassist
2. Sửa `ai/LLM.java` để call Perplexity API
3. Test module4 với Perplexity

### Option B: Dùng OpenAI (nhiều thư viện hơn)
1. Tải tất cả 7 thư viện Kotlin + Jackson
2. Giữ nguyên code LLM.java  
3. Cấu hình OpenAI API key

Bạn muốn chọn option nào?
