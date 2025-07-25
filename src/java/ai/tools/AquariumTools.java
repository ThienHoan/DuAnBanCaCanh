//package ai.tools;
//
//import dao.impl.ProductDAO;
//import dao.impl.ProductDetailDAO;
//import model.entity.Product;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * Tool chuyên tư vấn về cá cảnh, thức ăn, và thiết bị thủy sinh.
// * PHIÊN BẢN ĐÃ ĐƯỢC CẬP NHẬT ĐỂ TƯƠNG THÍCH VỚI GOOGLE GENAI SDK.
// */
//public class AquariumTools {
//
//    private final ProductDAO productDAO;
//    private final ProductDetailDAO detailDAO;
//
//    public AquariumTools() {
//        this.productDAO = new ProductDAO();
//        this.detailDAO = new ProductDetailDAO();
//    }
//
//    /**
//     * Tư vấn chọn cá cảnh phù hợp dựa trên kinh nghiệm người chơi (beginner, intermediate, advanced).
//     * @param experience Kinh nghiệm của người chơi. Các giá trị có thể là: beginner, intermediate, advanced
//     * @return Map chứa lời khuyên và danh sách sản phẩm gợi ý.
//     */
//    public Map<String, Object> fishRecommendation(String experience) {
//        if (experience == null || experience.trim().isEmpty()) {
//            experience = "beginner";
//        }
//
//        String careLevel;
//        switch (experience.toLowerCase()) {
//            case "intermediate":
//                careLevel = "Trung bình";
//                break;
//            case "advanced":
//                careLevel = "Khó";
//                break;
//            default:
//                careLevel = "Dễ";
//        }
//
//        List<Product> recommendedProducts = productDAO.getProductsByCareLevel(careLevel);
//        StringBuilder result = new StringBuilder("🐠 Dựa trên kinh nghiệm của bạn, đây là các gợi ý từ cửa hàng chúng tôi:\n\n");
//
//        if (recommendedProducts.isEmpty()) {
//            result.append("Rất tiếc, hiện tại chúng tôi không có sản phẩm nào phù hợp với mức độ kinh nghiệm '").append(careLevel).append("'.");
//        } else {
//            for (Product p : recommendedProducts) {
//                result.append("**").append(p.getName()).append("**\n");
//                result.append("💰 Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
//                if (p.getShortDescription() != null && !p.getShortDescription().isEmpty()) {
//                     result.append("📝 Mô tả: ").append(p.getShortDescription()).append("\n");
//                }
//                result.append("\n");
//            }
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("advice", result.toString());
//        response.put("recommendations", recommendedProducts);
//        return response;
//    }
//
//    /**
//     * Tra cứu một sản phẩm cụ thể trong database và trả về thông tin chi tiết.
//     * @param productName Tên sản phẩm cần tìm, ví dụ: 'cá guppy', 'máy lọc nước', 'thuốc trị nấm'
//     * @return Map chứa kết quả tìm kiếm.
//     */
//    public Map<String, Object> productInquiry(String productName) {
//        if (productName == null || productName.trim().isEmpty()) {
//            return Map.of("found", false, "response", "Vui lòng cho biết bạn muốn hỏi về sản phẩm nào.");
//        }
//    
//        List<Product> foundProducts = productDAO.getProductsByName(productName);
//    
//        Map<String, Object> response = new HashMap<>();
//    
//        if (foundProducts.isEmpty()) {
//            response.put("found", false);
//            response.put("response", "Rất tiếc, hiện tại cửa hàng chúng tôi chưa có hoặc đã hết sản phẩm '" + productName + "'.");
//        } else {
//            StringBuilder result = new StringBuilder("✅ Có bạn nhé! Cửa hàng đang có các sản phẩm '" + productName + "' sau:\n\n");
//            for (Product p : foundProducts) {
//                result.append("**").append(p.getName()).append("**\n");
//                result.append("💰 Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
//                if (p.getShortDescription() != null && !p.getShortDescription().isEmpty()) {
//                     result.append("📝 Mô tả: ").append(p.getShortDescription()).append("\n");
//                }
//                result.append("\n");
//            }
//            response.put("found", true);
//            response.put("response", result.toString());
//            response.put("products", foundProducts);
//        }
//    
//        return response;
//    }
//
//    /**
//     * Tư vấn các loại thức ăn phù hợp cho một loại cá cụ thể.
//     * @param fishType Tên loại cá cần tư vấn thức ăn, ví dụ: 'betta', 'guppy', 'cá vàng'
//     * @return Map chứa lời khuyên và danh sách sản phẩm gợi ý.
//     */
//    public Map<String, Object> feedingAdvice(String fishType) {
//        if (fishType == null || fishType.trim().isEmpty()) {
//            return Map.of("advice", "Vui lòng cho biết bạn muốn hỏi về thức ăn cho loại cá nào?");
//        }
//
//        int foodCategoryId = 3; 
//        List<Product> foodProducts = productDAO.getProductsByCategory(foodCategoryId);
//
//        List<Product> suitableFoods = foodProducts.stream()
//                .filter(p -> p.getName().toLowerCase().contains(fishType.toLowerCase()) || 
//                             (p.getDescription() != null && p.getDescription().toLowerCase().contains(fishType.toLowerCase())))
//                .collect(Collectors.toList());
//
//        StringBuilder advice = new StringBuilder("🍽️ **Hướng dẫn cho ăn cá " + fishType + " với các sản phẩm từ cửa hàng:**\n\n");
//        
//        if (suitableFoods.isEmpty()) {
//            advice.append("Hiện tại cửa hàng chưa có thức ăn chuyên dụng cho '").append(fishType).append("'. Tuy nhiên, bạn có thể tham khảo các loại cám tổng hợp.\n");
//        } else {
//            advice.append("Đây là các sản phẩm thức ăn phù hợp có sẵn tại cửa hàng:\n");
//            for (Product food : suitableFoods) {
//                advice.append("• **").append(food.getName()).append("** - Giá: ").append(String.format("%,.0f", food.getPrice())).append(" VNĐ\n");
//            }
//        }
//
//        advice.append("\n⏰ **Lịch cho ăn đề nghị:**\n");
//        advice.append("• Cá trưởng thành: Cho ăn 2 lần/ngày (sáng và chiều).\n");
//        advice.append("• Lượng thức ăn: Vừa đủ để cá ăn hết trong vòng 2-3 phút.\n");
//        advice.append("• Nên cho cá nhịn ăn 1 ngày mỗi tuần để hệ tiêu hóa được nghỉ ngơi.\n");
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("advice", advice.toString());
//        response.put("suggested_products", suitableFoods);
//        return response;
//    }
//    
//    /**
//     * Chẩn đoán bệnh dựa trên triệu chứng được mô tả và gợi ý các sản phẩm điều trị từ cửa hàng.
//     * @param symptoms Mô tả chi tiết về triệu chứng của cá, ví dụ: 'cá có đốm trắng', 'cá bị nấm trắng ở vây'
//     * @return Map chứa kết quả chẩn đoán và sản phẩm gợi ý.
//     */
//    public Map<String, Object> diseaseDiagnosis(String symptoms) {
//        if (symptoms == null || symptoms.trim().isEmpty()) {
//            return Map.of("advice", "Vui lòng mô tả triệu chứng của cá để tôi có thể chẩn đoán.");
//        }
//
//        StringBuilder diagnosis = new StringBuilder("🩺 **Chẩn đoán và hướng dẫn điều trị:**\n\n");
//        String keyword = "";
//        String diseaseName = "Không xác định";
//        String s = symptoms.toLowerCase();
//
//        if (s.contains("đốm trắng") || s.contains("ich")) {
//            diseaseName = "Bệnh đốm trắng (Ich)";
//            keyword = "trị đốm trắng";
//            diagnosis.append("🦠 **Chẩn đoán có thể:** ").append(diseaseName).append(".\n");
//            diagnosis.append("📋 **Triệu chứng:** Các đốm trắng nhỏ li ti như hạt muối bám trên thân và vây cá.\n");
//            diagnosis.append("💊 **Hướng dẫn:** Tăng nhiệt độ bể lên 30°C và sử dụng các sản phẩm đặc trị.\n\n");
//        } else if (s.contains("nấm") || s.contains("màng trắng")) {
//            diseaseName = "Bệnh nấm thủy mi";
//            keyword = "trị nấm";
//            diagnosis.append("🦠 **Chẩn đoán có thể:** ").append(diseaseName).append(".\n");
//            diagnosis.append("📋 **Triệu chứng:** Trên thân cá xuất hiện các túm bông màu trắng như nấm mốc.\n");
//            diagnosis.append("💊 **Hướng dẫn:** Cách ly cá bệnh, sử dụng muối hột và các loại thuốc trị nấm.\n\n");
//        } else {
//            diagnosis.append("Rất tiếc, tôi chưa thể chẩn đoán rõ ràng với mô tả này. Bạn có thể cung cấp thêm hình ảnh hoặc mô tả chi tiết hơn không?\n");
//        }
//
//        List<Product> treatmentProducts = new ArrayList<>();
//        if (!keyword.isEmpty()) {
//            int treatmentCategoryId = 4;
//            List<Product> allTreatments = productDAO.getProductsByCategory(treatmentCategoryId);
//            String finalKeyword = keyword;
//            treatmentProducts = allTreatments.stream()
//                .filter(p -> p.getName().toLowerCase().contains(finalKeyword))
//                .collect(Collectors.toList());
//        }
//
//        if (!treatmentProducts.isEmpty()) {
//            diagnosis.append("🛒 **Sản phẩm điều trị gợi ý từ cửa hàng:**\n");
//            for (Product p : treatmentProducts) {
//                diagnosis.append("• **").append(p.getName()).append("** - Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
//            }
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("diagnosis_result", diagnosis.toString());
//        response.put("suggested_treatments", treatmentProducts);
//        return response;
//    }
//}
