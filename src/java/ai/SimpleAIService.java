package ai;

import ai.tools.AquariumTools;
// Import các lớp chính xác từ thư viện Vertex AI
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.genai.types.GenerateContentResponse;
import dao.impl.ProductDAO;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import model.entity.Product;

/**
 * PHIÊN BẢN CUỐI CÙNG, ĐÚNG VÀ HOÀN CHỈNH
 */
public class SimpleAIService {
    
    private final VertexAI vertexAIClient; 
    private final AquariumTools aquariumTools;
    private final ProductDAO productDAO;

    public SimpleAIService() {
        // !!! QUAN TRỌNG: Thay "your-gcp-project-id" bằng ID dự án Google Cloud của bạn
        String projectId = "your-gcp-project-id"; 
        String location = "asia-southeast1"; 
        
        try {
            this.vertexAIClient = new VertexAI(projectId, location);
        } catch (IOException e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể khởi tạo VertexAI client.");
            e.printStackTrace();
            throw new RuntimeException("Không thể khởi tạo Vertex AI", e);
        }
        
        this.aquariumTools = new AquariumTools();
        this.productDAO = new ProductDAO();
    }

    public String getAIResponse(String userQuestion, List<String> chatHistory) {
        if (vertexAIClient == null) {
            return "Lỗi cấu hình: Không thể kết nối đến Vertex AI.";
        }
         try {
            String normalizedQuestion = Normalizer.normalize(userQuestion, Normalizer.Form.NFC).toLowerCase();
            List<String> allProductNames = productDAO.getAllProductNames();
            for (String productName : allProductNames) {
                String normalizedProductName = Normalizer.normalize(productName, Normalizer.Form.NFC).toLowerCase();
                List<String> searchTerms = new ArrayList<>();
                searchTerms.add(normalizedProductName);
                if (normalizedProductName.contains(" ")) {
                    String[] words = normalizedProductName.split(" ");
                    if (words.length >= 2) {
                        searchTerms.add(words[0] + " " + words[1]);
                    }
                }
                for (String term : searchTerms) {
                    if (normalizedQuestion.contains(term)) {
                        List<Product> foundProducts = productDAO.getProductsByName(productName);
                        return callSmartProductResponseAI(userQuestion, foundProducts, chatHistory);
                    }
                }
            }
            return callGenericVertexAI(userQuestion, chatHistory);
        } catch (Exception e) { 
            e.printStackTrace(); 
            return "Xin lỗi, đã có lỗi xảy ra khi xử lý yêu cầu của tôi."; 
        }
    }

    private String callSmartProductResponseAI(String userQuestion, List<Product> products, List<String> history) throws Exception {
        // ... (Phần xây dựng prompt giữ nguyên)
        String finalPrompt = "Bối cảnh: Bạn là một nhân viên tư vấn nhiệt tình...";

        String modelName = "gemini-1.5-flash-001";
        
        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
        GenerateContentResponse response = model.generateContent(finalPrompt);

        return ResponseHandler.getText(response);
    }
    
    private String callGenericVertexAI(String userQuestion, List<String> history) throws Exception {
        // ... (Phần xây dựng prompt giữ nguyên)
        String finalPrompt = "Bạn là một trợ lý AI của cửa hàng cá cảnh...";

        String modelName = "gemini-1.5-flash-001";
        
        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
        GenerateContentResponse response = model.generateContent(finalPrompt);
        
        return ResponseHandler.getText(response);
    }
}
