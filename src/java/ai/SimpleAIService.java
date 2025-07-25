//package ai;
//
//import ai.tools.AquariumTools;
//// Import các lớp chính xác từ thư viện Vertex AI
//import com.google.cloud.vertexai.VertexAI;
//import com.google.cloud.vertexai.generativeai.GenerativeModel;
//import com.google.cloud.vertexai.generativeai.ResponseHandler;
//// Thay thế import sai bằng import đúng
//import com.google.cloud.vertexai.api.GenerateContentResponse;
//import dao.impl.ProductDAO;
//import java.io.IOException;
//import java.text.Normalizer;
//import java.util.ArrayList;
//import java.util.List;
//import model.entity.Product;
//
///**
// * PHIÊN BẢN CUỐI CÙNG, ĐÚNG VÀ HOÀN CHỈNH
// */
//public class SimpleAIService {
//    
//    private final VertexAI vertexAIClient; 
//    private final AquariumTools aquariumTools;
//    private final ProductDAO productDAO;
//
//    public SimpleAIService() {
//        // !!! QUAN TRỌNG: Thay "your-gcp-project-id" bằng ID dự án Google Cloud của bạn
//        String projectId = "fishshop-462614"; 
//        String location = "asia-southeast1"; 
//        
//        this.vertexAIClient = new VertexAI(projectId, location);
//        
//        this.aquariumTools = new AquariumTools();
//        this.productDAO = new ProductDAO();
//    }
//
//    public String getAIResponse(String userQuestion, List<String> chatHistory) {
//        if (vertexAIClient == null) {
//            return "Lỗi cấu hình: Không thể kết nối đến Vertex AI.";
//        }
//         try {
//            String normalizedQuestion = Normalizer.normalize(userQuestion, Normalizer.Form.NFC).toLowerCase();
//            List<String> allProductNames = productDAO.getAllProductNames();
//            for (String productName : allProductNames) {
//                String normalizedProductName = Normalizer.normalize(productName, Normalizer.Form.NFC).toLowerCase();
//                List<String> searchTerms = new ArrayList<>();
//                searchTerms.add(normalizedProductName);
//                if (normalizedProductName.contains(" ")) {
//                    String[] words = normalizedProductName.split(" ");
//                    if (words.length >= 2) {
//                        searchTerms.add(words[0] + " " + words[1]);
//                    }
//                }
//                for (String term : searchTerms) {
//                    if (normalizedQuestion.contains(term)) {
//                        List<Product> foundProducts = productDAO.getProductsByName(productName);
//                        return callSmartProductResponseAI(userQuestion, foundProducts, chatHistory);
//                    }
//                }
//            }
//            return callGenericVertexAI(userQuestion, chatHistory);
//        } catch (Exception e) { 
//            e.printStackTrace(); 
//            return "Xin lỗi, đã có lỗi xảy ra khi xử lý yêu cầu của tôi."; 
//        }
//    }
//
//    private String callSmartProductResponseAI(String userQuestion, List<Product> products, List<String> history) throws Exception {
//        StringBuilder promptBuilder = new StringBuilder();
//        promptBuilder.append("Bối cảnh: Bạn là một nhân viên tư vấn nhiệt tình và chuyên nghiệp của một cửa hàng cá cảnh. ");
//        promptBuilder.append("Nhiệm vụ của bạn là trả lời câu hỏi của khách hàng một cách ngắn gọn, chính xác, và thân thiện, dựa trên thông tin sản phẩm được cung cấp và lịch sử trò chuyện. ");
//        promptBuilder.append("Không tự bịa thêm thông tin không có trong danh sách. Trả lời bằng tiếng Việt.\n\n");
//        promptBuilder.append("--- Thông tin sản phẩm liên quan ---\n");
//        for (Product p : products) {
//            promptBuilder.append("Tên: ").append(p.getName()).append("\n");
//            promptBuilder.append("Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
//            if (p.getShortDescription() != null && !p.getShortDescription().isEmpty()) {
//                promptBuilder.append("Mô tả: ").append(p.getShortDescription()).append("\n");
//            }
//            promptBuilder.append("---\n");
//        }
//        promptBuilder.append("\n--- Lịch sử trò chuyện ---\n");
//        for (String msg : history) {
//            promptBuilder.append(msg).append("\n");
//        }
//        promptBuilder.append("\n--- Câu hỏi mới của khách hàng ---\n");
//        promptBuilder.append(userQuestion);
//
//        String modelName = "gemini-2.5-flash";
//        
//        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
//        GenerateContentResponse response = model.generateContent(promptBuilder.toString());
//
//        return ResponseHandler.getText(response);
//    }
//    
//    private String callGenericVertexAI(String userQuestion, List<String> history) throws Exception {
//        StringBuilder promptBuilder = new StringBuilder();
//        promptBuilder.append("Bối cảnh: Bạn là trợ lý AI của một cửa hàng cá cảnh. ");
//        promptBuilder.append("Nhiệm vụ của bạn là trả lời câu hỏi của khách hàng một cách thân thiện và hữu ích. ");
//        promptBuilder.append("Nếu bạn không biết câu trả lời, hãy nói rằng bạn sẽ kiểm tra lại với nhân viên cửa hàng. Trả lời bằng tiếng Việt.\n\n");
//        promptBuilder.append("--- Lịch sử trò chuyện ---\n");
//        for (String msg : history) {
//            promptBuilder.append(msg).append("\n");
//        }
//        promptBuilder.append("\n--- Câu hỏi mới của khách hàng ---\n");
//        promptBuilder.append(userQuestion);
//
//        String modelName = "gemini-2.5-flash";
//        
//        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
//        GenerateContentResponse response = model.generateContent(promptBuilder.toString());
//        
//        return ResponseHandler.getText(response);
//    }
//}
