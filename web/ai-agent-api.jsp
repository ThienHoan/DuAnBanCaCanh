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
                                 "hãy chọn chế độ 'Real Mode'.\\n\\n" +
                                 "💡 **Gợi ý**: Thử hỏi về tư vấn cá cảnh, thiết lập bể, hoặc chăm sóc cá!";
                success = true;
            }
        }
        
    } catch (Exception e) {
        errorMessage = "Lỗi server: " + e.getMessage();
        e.printStackTrace();
    }
    
    // Build JSON response manually (since we don't have Gson)
    StringBuilder jsonResponse = new StringBuilder();
    jsonResponse.append("{");
    jsonResponse.append("\"success\": ").append(success).append(",");
    
    if (success) {
        jsonResponse.append("\"response\": \"").append(responseMessage.replace("\"", "\\\"").replace("\n", "\\n")).append("\",");
        jsonResponse.append("\"mode\": \"").append(mode).append("\",");
        jsonResponse.append("\"user_message\": \"").append(userMessage.replace("\"", "\\\"")).append("\",");
        jsonResponse.append("\"timestamp\": ").append(System.currentTimeMillis());
    } else {
        jsonResponse.append("\"error\": \"").append(errorMessage.replace("\"", "\\\"")).append("\",");
        jsonResponse.append("\"timestamp\": ").append(System.currentTimeMillis());
    }
    
    jsonResponse.append("}");
    
    // Output JSON
    out.print(jsonResponse.toString());
    out.flush();
%>
