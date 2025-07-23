package controller.client;

import ai.SimpleAIService;
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
import com.google.gson.Gson;

/**
 * Servlet cho client-side AI agent - phiên bản tương thích với client site
 * Hỗ trợ mua hàng trực tiếp qua chat và giải đáp thắc mắc của khách hàng
 */
@WebServlet(name = "ClientAIAgentServlet", urlPatterns = {"/client/ai-agent"})
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
            User user = SessionUtils.getUser(session);
            boolean isLoggedIn = SessionUtils.isLoggedIn(session);
            Integer userId = isLoggedIn ? user.getUserId() : null;
            String userEmail = isLoggedIn ? user.getEmail() : null;
            String userName = isLoggedIn ? user.getUsername() : null;
            
            // Lấy hoặc tạo mới chatHistory
            List<String> chatHistory = (List<String>) session.getAttribute(AI_CHAT_HISTORY_KEY);
            if (chatHistory == null) {
                chatHistory = new ArrayList<>();
                session.setAttribute(AI_CHAT_HISTORY_KEY, chatHistory);
            }
            
            // Lấy hoặc tạo mới context
            Map<String, Object> context = (Map<String, Object>) session.getAttribute(AI_CONTEXT_KEY);
            if (context == null) {
                context = new HashMap<>();
                session.setAttribute(AI_CONTEXT_KEY, context);
            }
            
            // Thêm thông tin người dùng vào context
            context.put("isLoggedIn", isLoggedIn);
            if (userId != null) context.put("userId", userId);
            if (userEmail != null) context.put("userEmail", userEmail);
            if (userName != null) context.put("userName", userName);
            
            // Thêm câu hỏi vào lịch sử chat
            chatHistory.add("User: " + userQuestion);
            
            // Lấy hoặc tạo mới AI service
            SimpleAIService aiService = (SimpleAIService) session.getAttribute(AI_SERVICE_KEY);
            if (aiService == null) {
                aiService = new SimpleAIService();
                session.setAttribute(AI_SERVICE_KEY, aiService);
            }
            
            try {
                // Gọi AI service để lấy câu trả lời
                String aiResponse = aiService.getAIResponse(userQuestion, chatHistory, context, session);
                
                // Thêm câu trả lời vào lịch sử chat
                chatHistory.add("AI: " + aiResponse);
                
                // Giới hạn kích thước lịch sử chat
                while (chatHistory.size() > 20) {
                    chatHistory.remove(0);
                }
                
                // Lưu lại context và chatHistory
                session.setAttribute(AI_CHAT_HISTORY_KEY, chatHistory);
                session.setAttribute(AI_CONTEXT_KEY, context);
                
                // Chuẩn bị JSON response
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("success", true);
                responseMap.put("response", aiResponse);
                
                // Trả về câu trả lời dạng JSON
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(responseMap));
                out.flush();
                
            } catch (Exception e) {
                // Log lỗi
                e.printStackTrace();
                
                // Trả về thông báo lỗi
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Có lỗi xảy ra khi xử lý yêu cầu: " + e.getMessage());
                
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(errorResponse));
                out.flush();
            }
        } else {
            // Trả về thông báo lỗi nếu không có câu hỏi
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Vui lòng nhập câu hỏi.");
            
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(errorResponse));
            out.flush();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng về trang AI agent khi truy cập bằng GET
        response.sendRedirect(request.getContextPath() + "/client/ai-agent.jsp");
    }
}
