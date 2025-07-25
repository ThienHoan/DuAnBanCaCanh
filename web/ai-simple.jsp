<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ai.LLM"%>
<%@page import="ai.Message"%>
<%@page import="java.util.*"%>

<%
    // Set response headers với UTF-8 encoding rõ ràng
    response.setContentType("application/json; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    // Đảm bảo request cũng được decode UTF-8
    request.setCharacterEncoding("UTF-8");
    
    // Initialize variables
    boolean success = false;
    String responseMessage = "";
    String errorMessage = "";
    String userMessage = "";
    String mode = "demo";
    
    try {
        // Get parameters
        userMessage = request.getParameter("request");
        String modeParam = request.getParameter("mode");
        if (modeParam != null) {
            mode = modeParam;
        }
        
        // Validate input
        if (userMessage == null || userMessage.trim().isEmpty()) {
            errorMessage = "Message không được để trống";
        } else {
            // Process with LLM directly
            if ("real".equals(mode)) {
                try {
                    LLM llm = new LLM();
                    
                    // Create messages for LLM
                    List<Message> messages = new ArrayList<>();
                    messages.add(new Message("system", 
                        "Bạn là chuyên gia tư vấn cá cảnh hàng đầu tại cửa hàng bán cá cảnh. " +
                        "Nhiệm vụ: Tư vấn chuyên nghiệp, thân thiện và hữu ích cho khách hàng về cá cảnh. " +
                        "Ngôn ngữ: Tiếng Việt tự nhiên, dễ hiểu. " +
                        "Hãy trả lời ngắn gọn, hữu ích và có format markdown đẹp."));
                    messages.add(new Message("user", userMessage));
                    
                    responseMessage = llm.generateResponse(messages);
                    success = true;
                } catch (Exception llmError) {
                    errorMessage = "Lỗi xử lý với LLM: " + llmError.getMessage();
                }
            } else {
                // Demo mode
                responseMessage = "🤖 **Demo Mode Response**\\n\\n" +
                                 "Bạn vừa hỏi: \\\"" + userMessage + "\\\"\\n\\n" +
                                 "Đây là response demo. Để sử dụng AI Agent thật, " +
                                 "hãy chọn chế độ 'Real Mode'.";
                success = true;
            }
        }
        
    } catch (Exception e) {
        errorMessage = "Lỗi server: " + e.getMessage();
    }
    
    // Generate JSON response
    String jsonResponse;
    if (success) {
        jsonResponse = String.format(
            "{\"success\": true, \"response\": \"%s\", \"mode\": \"%s\"}", 
            responseMessage.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", ""), 
            mode
        );
    } else {
        jsonResponse = String.format(
            "{\"success\": false, \"error\": \"%s\", \"mode\": \"%s\"}", 
            errorMessage.replace("\"", "\\\""), 
            mode
        );
    }
    
    out.print(jsonResponse);
%>
        if (userMessage == null || userMessage.trim().isEmpty()) {
            errorMessage = "Message không được để trống";
        } else {
            if ("real".equals(mode)) {
                try {
                    // Sử dụng LLM trực tiếp
                    LLM llm = new LLM();
                    
                    // Tạo system prompt cho câu hỏi so sánh cá
                    String systemPrompt = "Bạn là chuyên gia tư vấn cá cảnh. Hãy trả lời bằng tiếng Việt một cách chuyên nghiệp và chi tiết.";
                    
                    if (userMessage.toLowerCase().contains("so sánh") || 
                        userMessage.toLowerCase().contains("guppy") && userMessage.toLowerCase().contains("cardinal")) {
                        systemPrompt = "Bạn là chuyên gia cá cảnh. Hãy so sánh chi tiết các loại cá theo yêu cầu. " +
                                      "Đưa ra thông tin về: đặc điểm ngoại hình, độ khó nuôi, yêu cầu môi trường, giá cả, " +
                                      "và tư vấn cho người mới bắt đầu. Trả lời bằng tiếng Việt.";
                    }
                    
                    // Tạo messages
                    List<Message> messages = new ArrayList<>();
                    messages.add(new Message("system", systemPrompt));
                    messages.add(new Message("user", userMessage));
                    
                    LLM.Prompt prompt = new LLM.Prompt(messages);
                    responseMessage = llm.generateResponse(prompt);
                    
                    if (responseMessage != null && !responseMessage.trim().isEmpty()) {
                        success = true;
                    } else {
                        errorMessage = "Không thể tạo phản hồi";
                    }
                    
                } catch (Exception e) {
                    errorMessage = "Lỗi khi xử lý với LLM: " + e.getMessage();
                }
            } else {
                // Demo mode
                if (userMessage.toLowerCase().contains("so sánh") && 
                    userMessage.toLowerCase().contains("guppy") && 
                    userMessage.toLowerCase().contains("cardinal")) {
                    
                    responseMessage = "🐠 **So sánh Cá Guppy và Cá Cardinal (Demo Mode)**\n\n" +
                                     "## Cá Guppy\n" +
                                     "🎨 **Đặc điểm:**\n" +
                                     "• Kích thước: 3-6cm\n" +
                                     "• Màu sắc: Rất đa dạng, đực có đuôi dài rực rỡ\n" +
                                     "• Tính cách: Hiền lành, dễ nuôi\n\n" +
                                     "⭐ **Độ khó nuôi:** Dễ (phù hợp người mới)\n" +
                                     "🏠 **Yêu cầu bể:** 20L+ cho nhóm nhỏ\n" +
                                     "🌡️ **Nhiệt độ:** 22-28°C\n" +
                                     "💰 **Giá:** 10,000-50,000 VNĐ/con\n\n" +
                                     "## Cá Cardinal\n" +
                                     "🎨 **Đặc điểm:**\n" +
                                     "• Kích thước: 3-4cm\n" +
                                     "• Màu sắc: Xanh neon và đỏ đặc trưng\n" +
                                     "• Tính cách: Sống đàn, cần nhóm 6+ con\n\n" +
                                     "⭐ **Độ khó nuôi:** Trung bình\n" +
                                     "🏠 **Yêu cầu bể:** 60L+ cho đàn\n" +
                                     "🌡️ **Nhiệt độ:** 24-26°C (ổn định)\n" +
                                     "💰 **Giá:** 15,000-30,000 VNĐ/con\n\n" +
                                     "## Tư vấn cho người mới\n" +
                                     "🌟 **Guppy** - Lựa chọn tốt hơn cho người mới bắt đầu\n" +
                                     "✅ Dễ nuôi, chịu đựng tốt\n" +
                                     "✅ Sinh sản dễ dàng\n" +
                                     "✅ Bể nhỏ cũng được\n\n" +
                                     "🌟 **Cardinal** - Đẹp hơn nhưng khó hơn\n" +
                                     "⚠️ Cần nước ổn định\n" +
                                     "⚠️ Phải nuôi đàn\n" +
                                     "⚠️ Bể lớn hơn\n\n" +
                                     "*Đây là Demo Mode. Để có tư vấn chi tiết hơn, hãy chọn Real Mode.*";
                } else {
                    responseMessage = "🤖 **Demo Mode**\n\n" +
                                     "Bạn vừa hỏi: \"" + userMessage + "\"\n\n" +
                                     "Đây là phản hồi demo. Để sử dụng AI thật, hãy chọn Real Mode.";
                }
                success = true;
            }
        }
        
    } catch (Exception e) {
        errorMessage = "Lỗi server: " + e.getMessage();
        e.printStackTrace();
    }
    
    // Tạo JSON response
    String jsonResponse;
    if (success) {
        jsonResponse = String.format(
            "{\"success\": true, \"response\": \"%s\", \"mode\": \"%s\"}", 
            responseMessage.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", ""), 
            mode
        );
    } else {
        jsonResponse = String.format(
            "{\"success\": false, \"error\": \"%s\", \"mode\": \"%s\"}", 
            errorMessage.replace("\"", "\\\""), 
            mode
        );
    }
    
    out.print(jsonResponse);
%>
