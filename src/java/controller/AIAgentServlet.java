package controller;

import ai.SimpleAIService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entity.User;
import utils.SessionUtils;

/**
 * Servlet được nâng cấp để sử dụng SimpleAIService với Vertex AI.
 * Hỗ trợ mua hàng trực tiếp qua chat.
 */
@WebServlet(name = "AIAgentServlet", urlPatterns = {"/ai-agent"})
public class AIAgentServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    private static final String AI_CHAT_HISTORY_KEY = "aiChatHistory";
    private static final String AI_SERVICE_KEY = "aiService";
    private static final String AI_CONTEXT_KEY = "aiContext";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String userQuestion = request.getParameter("question");
        if (userQuestion != null && !userQuestion.trim().isEmpty()) {
            // Lấy thông tin người dùng từ session
            HttpSession session = request.getSession();
            boolean isLoggedIn = session.getAttribute("userId") != null;
            Integer userId = isLoggedIn ? (Integer) session.getAttribute("userId") : null;
            String userEmail = isLoggedIn ? (String) session.getAttribute("userEmail") : null;
            String userName = isLoggedIn ? (String) session.getAttribute("userName") : null;
            
            // Lấy hoặc tạo mới chatHistory
            List<String> chatHistory = (List<String>) session.getAttribute("chatHistory");
            if (chatHistory == null) {
                chatHistory = new ArrayList<>();
                session.setAttribute("chatHistory", chatHistory);
            }
            
            // Lấy hoặc tạo mới context
            Map<String, Object> context = (Map<String, Object>) session.getAttribute("aiContext");
            if (context == null) {
                context = new HashMap<>();
                session.setAttribute("aiContext", context);
            }
            
            // Thêm thông tin người dùng vào context
            context.put("isLoggedIn", isLoggedIn);
            if (userId != null) context.put("userId", userId);
            if (userEmail != null) context.put("userEmail", userEmail);
            if (userName != null) context.put("userName", userName);
            
            // Thêm câu hỏi vào lịch sử chat
            chatHistory.add(userQuestion);
            
            // Gọi AI service để lấy câu trả lời
            SimpleAIService aiService = new SimpleAIService();
            String aiResponse = aiService.getAIResponse(userQuestion, chatHistory, context, session);
            
            // Thêm câu trả lời vào lịch sử chat
            chatHistory.add(aiResponse);
            
            // Giới hạn kích thước lịch sử chat
            while (chatHistory.size() > 20) {
                chatHistory.remove(0);
            }
            
            // Lưu lại context và chatHistory
            session.setAttribute("chatHistory", chatHistory);
            session.setAttribute("aiContext", context);
            
            // Trả về câu trả lời
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(aiResponse);
        } else {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Vui lòng nhập câu hỏi.");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("ai-agent.jsp");
    }
    
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("success", false);
        errorData.put("error", errorMessage);
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(errorData));
        out.flush();
    }
}