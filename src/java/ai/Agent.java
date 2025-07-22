package ai;

import ai.tools.AquariumTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Lớp Agent đơn giản hóa sử dụng HttpURLConnection thay vì Google GenAI SDK
 */
public class Agent {

    private final String apiKey;
    private final String modelName;
    private final AquariumTools aquariumTools;
    
    public Agent(String apiKey, String modelName) {
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.aquariumTools = new AquariumTools();
    }
    
    /**
     * Phương thức chính để chat với AI
     */
    public String chat(String userMessage) throws IOException {
        // Xử lý đơn giản: kiểm tra từ khóa trong tin nhắn để quyết định gọi function nào
        if (userMessage.toLowerCase().contains("cá") && 
            (userMessage.toLowerCase().contains("mới chơi") || 
             userMessage.toLowerCase().contains("beginner") || 
             userMessage.toLowerCase().contains("kinh nghiệm"))) {
            return processFunction("fishRecommendation", "beginner");
        } 
        else if (userMessage.toLowerCase().contains("sản phẩm") || 
                 userMessage.toLowerCase().contains("product")) {
            // Tìm tên sản phẩm trong tin nhắn (đơn giản hóa)
            String productName = extractProductName(userMessage);
            return processFunction("productInquiry", productName);
        }
        else if (userMessage.toLowerCase().contains("thức ăn") || 
                 userMessage.toLowerCase().contains("feed")) {
            // Tìm loại cá trong tin nhắn
            String fishType = extractFishType(userMessage);
            return processFunction("feedingAdvice", fishType);
        }
        else if (userMessage.toLowerCase().contains("bệnh") || 
                 userMessage.toLowerCase().contains("triệu chứng") || 
                 userMessage.toLowerCase().contains("symptom")) {
            // Lấy mô tả triệu chứng
            return processFunction("diseaseDiagnosis", userMessage);
        }
        else {
            // Gọi API bên ngoài nếu cần
            return callExternalAPI(userMessage);
        }
    }
    
    /**
     * Xử lý các function call dựa trên loại function
     */
    private String processFunction(String functionName, String param) {
        Map<String, Object> result = new HashMap<>();
        
        switch (functionName) {
            case "fishRecommendation":
                result = aquariumTools.fishRecommendation(param);
                break;
            case "productInquiry":
                result = aquariumTools.productInquiry(param);
                break;
            case "feedingAdvice":
                result = aquariumTools.feedingAdvice(param);
                break;
            case "diseaseDiagnosis":
                result = aquariumTools.diseaseDiagnosis(param);
                break;
            default:
                result.put("error", "Unknown function: " + functionName);
        }
        
        // Chuyển kết quả thành chuỗi có định dạng
        return formatResult(result);
    }
    
    /**
     * Gọi API bên ngoài (ví dụ: OpenAI) nếu cần
     */
    private String callExternalAPI(String message) {
        try {
            // Đọc cấu hình API từ file (nếu có)
            Properties props = new Properties();
            try {
                props.load(new FileInputStream("config.properties"));
                // Nếu có API key trong file cấu hình, sử dụng nó
                String configApiKey = props.getProperty("api.key");
                if (configApiKey != null && !configApiKey.isEmpty()) {
                    // Sử dụng API key từ file
                }
            } catch (IOException e) {
                // Sử dụng API key mặc định
            }
            
            // Mẫu gọi API đơn giản
            URL url = new URL("https://api.example.com/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);
            
            // Tạo request body
            String jsonInputString = "{\"model\": \"" + modelName + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Đọc response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            
            // Xử lý response đơn giản
            String responseStr = response.toString();
            if (responseStr.contains("\"content\":")) {
                // Trích xuất nội dung phản hồi (đơn giản hóa)
                int start = responseStr.indexOf("\"content\":") + 11;
                int end = responseStr.indexOf("\"", start);
                return responseStr.substring(start, end);
            }
            
            return responseStr;
        } catch (Exception e) {
            return "Xin lỗi, tôi không thể xử lý yêu cầu này ngay bây giờ. Lỗi: " + e.getMessage();
        }
    }
    
    /**
     * Trích xuất tên sản phẩm từ tin nhắn
     */
    private String extractProductName(String message) {
        // Đơn giản hóa: lấy từ sau "sản phẩm" hoặc "product"
        if (message.toLowerCase().contains("sản phẩm")) {
            int index = message.toLowerCase().indexOf("sản phẩm") + 8;
            return message.substring(index).trim();
        } else if (message.toLowerCase().contains("product")) {
            int index = message.toLowerCase().indexOf("product") + 7;
            return message.substring(index).trim();
        }
        return "unknown";
    }
    
    /**
     * Trích xuất loại cá từ tin nhắn
     */
    private String extractFishType(String message) {
        // Đơn giản hóa: lấy từ sau "cá"
        if (message.toLowerCase().contains("cá")) {
            int index = message.toLowerCase().indexOf("cá") + 2;
            return message.substring(index).trim();
        }
        return "unknown";
    }
    
    /**
     * Format kết quả thành chuỗi có định dạng
     */
    private String formatResult(Map<String, Object> result) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
