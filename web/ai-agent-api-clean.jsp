<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ai.agents.AquariumAgent"%>
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
            // Process with AquariumAgent
            if ("real".equals(mode)) {
                try {
                    AquariumAgent agent = new AquariumAgent();
                    if (agent.isReady()) {
                        responseMessage = agent.processQuestion(userMessage);
                        success = true;
                    } else {
                        errorMessage = "AI Agent chưa sẵn sàng";
                    }
                } catch (Exception agentError) {
                    errorMessage = "Lỗi xử lý với AquariumAgent: " + agentError.getMessage();
                }
            } else {
                // Demo mode
                responseMessage = "🤖 **Demo Mode Response**\\n\\n" +
                                 "Bạn vừa hỏi: \\\"" + userMessage + "\\\"\\n\\n" +
                                 "Đây là response demo. Để sử dụng AI Agent thật với Module4, " +
                                 "hãy chọn chế độ 'Real Mode'.";
                success = true;
            }
        }
        
    } catch (Exception e) {
        errorMessage = "Lỗi server: " + e.getMessage();
    }
    
    /**
     * Làm sạch response từ agent, loại bỏ debug logs
     */
    String cleanResponse(String rawResponse) {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            return "Xin lỗi, tôi không thể trả lời lúc này.";
        }
        
        // Loại bỏ các dòng log/debug/technical
        String cleanedResponse = rawResponse
            .replaceAll("🚀 Real Mode.*?===.*?===", "")
            .replaceAll("Agent thinking\\.\\.\\.", "")
            .replaceAll("Agent response:", "")
            .replaceAll("✅.*?JSON", "")
            .replaceAll("Found \\d+ products for keyword:.*?$", "")
            .replaceAll("Action result:.*?\\}", "")
            .replaceAll("⚠️.*?$", "")
            .replaceAll("```.*?```", "")
            .replaceAll("searchProducts\\(\".*?\"\\)", "")
            .replaceAll("✅.*?thành công!", "")
            .replaceAll("📂 Current working directory:.*?$", "")
            .replaceAll("? Found.*?$", "")
            .replaceAll("Error invoking.*?null", "")
            .replaceAll("java\\.lang\\.NullPointerException", "")
            .replaceAll("Prompt details:.*", "")
            .trim();

        // Lấy nội dung có ý nghĩa cuối cùng
        String[] lines = cleanedResponse.split("\\n");
        StringBuilder result = new StringBuilder();
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() > 10 && 
                !trimmed.contains("Agent") && 
                !trimmed.contains("===") &&
                !trimmed.contains("JSON") &&
                !trimmed.contains("parse action") &&
                !trimmed.contains("Converted") &&
                !trimmed.startsWith("?") &&
                !trimmed.startsWith("✅") &&
                !trimmed.startsWith("Message:")) {
                result.append(trimmed).append("\\n");
            }
        }
        
        String finalResult = result.toString().trim();
        return finalResult.isEmpty() ? "Cảm ơn bạn đã sử dụng dịch vụ tư vấn!" : finalResult;
    }
    
    // Clean response if success
    if (success && responseMessage != null) {
        responseMessage = cleanResponse(responseMessage);
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
