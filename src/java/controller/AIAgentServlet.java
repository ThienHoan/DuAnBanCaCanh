package controller;

import ai.SimpleAIService;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * PHIÊN BẢN HOÀN CHỈNH - Đã bổ sung lại phương thức sendErrorResponse.
 */
@WebServlet(name = "AIAgentServlet", urlPatterns = {"/ai-agent"})
public class AIAgentServlet extends HttpServlet {
    
    private SimpleAIService aiService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        // ... (phần này giữ nguyên)
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<String> chatHistory = (List<String>) session.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }
        
        String jsonRequest = null;
        try (java.io.InputStream inputStream = request.getInputStream();
             java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            jsonRequest = new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            sendErrorResponse(response, "Không thể đọc dữ liệu yêu cầu.");
            return;
        }
        
        try {
            Map<String, String> requestData = gson.fromJson(jsonRequest, Map.class);
            String userMessage = requestData.get("request");
            
            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(response, "Yêu cầu không được để trống");
                return;
            }
            
            String agentResponse = aiService.getAIResponse(userMessage, chatHistory);
            
            chatHistory.add("User: " + userMessage);
            chatHistory.add("AI: " + agentResponse);
            session.setAttribute("chatHistory", chatHistory);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("response", agentResponse);
            
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(responseData));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("❌ Error processing AI request: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi xử lý yêu cầu: " + e.getMessage());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("ai-agent.jsp");
    }
    
    // **PHƯƠNG THỨC ĐÃ ĐƯỢC BỔ SUNG LẠI**
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("success", false);
        errorData.put("error", errorMessage);
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(errorData));
        out.flush();
    }
}
