//package controller;
//
//import ai.Agent;
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//import utils.ConfigUtil;
//
///**
// * Servlet được nâng cấp để sử dụng Agent có khả năng Function Calling.
// */
//@WebServlet(name = "AIAgentServlet", urlPatterns = {"/ai-agent"})
//public class AIAgentServlet extends HttpServlet {
//    
//    private final Gson gson = new Gson();
//    private static final String AI_AGENT_SESSION_KEY = "aiAgentSession";
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        
//        HttpSession session = request.getSession();
//        
//        Agent agent = (Agent) session.getAttribute(AI_AGENT_SESSION_KEY);
//        if (agent == null) {
//            try {
//                String apiKey = new ConfigUtil().get("gemini.api.key");
//                String modelName = "gemini-1.5-flash-001"; 
//                
//                if (apiKey == null || apiKey.trim().isEmpty()) {
//                    throw new IOException("Gemini API key is not configured in config.properties.");
//                }
//
//                agent = new Agent(apiKey, modelName);
//                session.setAttribute(AI_AGENT_SESSION_KEY, agent);
//            } catch (Exception e) {
//                log("FATAL: Could not initialize AI Agent.", e);
//                sendErrorResponse(response, "Could not initialize AI service: " + e.getMessage());
//                return;
//            }
//        }
//        
//        String jsonRequest = request.getReader().lines().collect(java.util.stream.Collectors.joining(System.lineSeparator()));
//        
//        try {
//            Map<String, String> requestData = gson.fromJson(jsonRequest, Map.class);
//            String userMessage = requestData.get("request");
//            
//            if (userMessage == null || userMessage.trim().isEmpty()) {
//                sendErrorResponse(response, "Request cannot be empty");
//                return;
//            }
//            
//            String agentResponse = agent.chat(userMessage);
//            
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("success", true);
//            responseData.put("response", agentResponse);
//            
//            PrintWriter out = response.getWriter();
//            out.print(gson.toJson(responseData));
//            out.flush();
//            
//        } catch (Exception e) {
//            log("Error processing AI request: " + e.getMessage(), e);
//            sendErrorResponse(response, "Error processing request: " + e.getMessage());
//        }
//    }
//    
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.sendRedirect("ai-agent.jsp");
//    }
//    
//    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
//        Map<String, Object> errorData = new HashMap<>();
//        errorData.put("success", false);
//        errorData.put("error", errorMessage);
//        
//        PrintWriter out = response.getWriter();
//        out.print(gson.toJson(errorData));
//        out.flush();
//    }
//}