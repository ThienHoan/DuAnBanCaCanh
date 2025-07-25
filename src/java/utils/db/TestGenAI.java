package utils.db;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

/**
 * Lớp kiểm thử (Test Class) để xác nhận kết nối đến Vertex AI.
 * PHIÊN BẢN CUỐI CÙNG - Sửa lỗi dựa trên danh sách phương thức thực tế.
 */
public class TestGenAI {

    public static void main(String[] args) {
        // 1. Lấy API key từ thuộc tính hệ thống
        String apiKey = System.getProperty("GOOGLE_API_KEY");

        // 2. In ra để kiểm tra
        System.out.println("Đang kiểm tra API Key...");
        System.out.println("API Key tìm thấy: " + apiKey);

        // 3. Kiểm tra key có tồn tại không
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("LỖI: Không tìm thấy GOOGLE_API_KEY!");
            System.err.println("Vui lòng kiểm tra lại cấu hình 'VM Options' trong Project Properties của NetBeans.");
            return; 
        }
        
        try {
            // 4. Khởi tạo Client của Google
            Client client = Client.builder().apiKey(apiKey).build();
            System.out.println("✅ Khởi tạo Google GenAI Client thành công!");

            // 5. Chuẩn bị model name và câu hỏi (prompt)
            String modelName = "gemini-2.0-flash"; // Model đã bị deprecated, nhưng chúng ta sẽ sửa sau nếu cần
            String promptText = "Hãy tư vấn cách chăm sóc cá Betta";
            
            System.out.println("\nĐang gửi yêu cầu đến Vertex AI với model: " + modelName);
            
            // 6. **SỬA LỖI Ở ĐÂY**: Gọi API với đúng 3 tham số, truyền `null` cho config
            //    Dựa trên hình ảnh bạn gửi, đây là phương thức đúng.
            GenerateContentResponse response = client.models
                    .generateContent(modelName, promptText, null);
            
            // 7. In kết quả trả về từ AI
            System.out.println("\n--- Câu trả lời từ AI ---");
            System.out.println(response.text());
            System.out.println("-------------------------");
            System.out.println("\n✅ Kiểm tra thành công!");

        } catch (Exception e) {
            // Xử lý nếu có lỗi trong quá trình khởi tạo hoặc gọi API
            System.err.println("\n❌ Đã xảy ra lỗi nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
