package controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SePayConfig;

/**
 * Servlet để cấu hình URL ngrok cho webhook
 */
@WebServlet(name = "NgrokConfigController", urlPatterns = {"/admin/ngrok-config"})
public class NgrokConfigController extends HttpServlet {

    /**
     * Hiển thị form cấu hình ngrok
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Cấu hình Ngrok URL</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }");
            out.println(".container { max-width: 800px; margin: 0 auto; }");
            out.println("h1 { color: #333; }");
            out.println("form { background: #f9f9f9; padding: 20px; border-radius: 5px; }");
            out.println("label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println("input[type='text'] { width: 100%; padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }");
            out.println("button { background: #4CAF50; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }");
            out.println("button:hover { background: #45a049; }");
            out.println(".info { background: #e7f3fe; border-left: 6px solid #2196F3; padding: 10px; margin-bottom: 15px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Cấu hình Ngrok URL cho Webhook</h1>");
            
            out.println("<div class='info'>");
            out.println("<p><strong>URL Webhook hiện tại:</strong> " + SePayConfig.getWebhookUrl() + "</p>");
            out.println("<p>Hướng dẫn:</p>");
            out.println("<ol>");
            out.println("<li>Chạy lệnh <code>ngrok http 8080</code> trong terminal</li>");
            out.println("<li>Sao chép URL Forwarding (ví dụ: <code>https://233fa915fd4d.ngrok-free.app</code>)</li>");
            out.println("<li>Dán URL vào ô bên dưới và nhấn Cập nhật</li>");
            out.println("</ol>");
            out.println("</div>");
            
            out.println("<form action='ngrok-config' method='post'>");
            out.println("<label for='ngrokUrl'>Ngrok URL:</label>");
            out.println("<input type='text' id='ngrokUrl' name='ngrokUrl' placeholder='https://your-ngrok-url.ngrok-free.app' required>");
            out.println("<button type='submit'>Cập nhật Webhook URL</button>");
            out.println("</form>");
            
            // Hiển thị thông báo nếu có
            String message = (String) request.getSession().getAttribute("message");
            if (message != null) {
                out.println("<div style='margin-top: 20px; padding: 10px; background-color: #dff0d8; border-radius: 4px;'>");
                out.println("<p>" + message + "</p>");
                out.println("</div>");
                request.getSession().removeAttribute("message");
            }
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Xử lý cập nhật URL ngrok
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ngrokUrl = request.getParameter("ngrokUrl");
        
        if (ngrokUrl != null && !ngrokUrl.isEmpty()) {
            // Cập nhật URL webhook
            SePayConfig.updateWebhookUrlWithNgrok(ngrokUrl);
            
            // Đặt thông báo thành công
            request.getSession().setAttribute("message", "Webhook URL đã được cập nhật thành công thành: " + SePayConfig.getWebhookUrl());
        } else {
            // Đặt thông báo lỗi
            request.getSession().setAttribute("message", "Lỗi: Ngrok URL không được để trống!");
        }
        
        // Chuyển hướng về trang cấu hình
        response.sendRedirect(request.getContextPath() + "/admin/ngrok-config");
    }
} 