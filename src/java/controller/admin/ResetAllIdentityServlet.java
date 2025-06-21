package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import utils.db.DBContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet để reset IDENTITY values cho tất cả các bảng chính
 */
public class ResetAllIdentityServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ResetAllIdentityServlet.class.getName());
    
    // Định nghĩa các bảng và giá trị reset mong muốn
    private static final Map<String, Integer> TABLES_TO_RESET = new HashMap<>();
    
    static {
        TABLES_TO_RESET.put("Users", 5);
        TABLES_TO_RESET.put("blog_categories", 5);
        TABLES_TO_RESET.put("blog_posts", 5);
        TABLES_TO_RESET.put("Products", 5);
        TABLES_TO_RESET.put("Orders", 5);
        TABLES_TO_RESET.put("Categories", 5);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ admin mới có quyền truy cập");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Reset All Identity Values</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println(".success { color: green; font-weight: bold; }");
            out.println(".error { color: red; font-weight: bold; }");
            out.println(".info { color: blue; font-weight: bold; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Reset All Identity Values</h1>");
            
            String action = request.getParameter("action");
            
            if ("reset".equals(action)) {
                performReset(out);
            } else {
                showCurrentValues(out);
                showResetForm(out);
            }
            
            out.println("<p><a href='" + request.getContextPath() + "/admin-dashboard'>Quay lại Dashboard</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void showCurrentValues(PrintWriter out) {
        out.println("<h2>Giá trị IDENTITY hiện tại</h2>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Tên bảng</th>");
        out.println("<th>Giá trị IDENTITY hiện tại</th>");
        out.println("<th>Giá trị sẽ reset về</th>");
        out.println("<th>Trạng thái</th>");
        out.println("</tr>");
        
        for (Map.Entry<String, Integer> entry : TABLES_TO_RESET.entrySet()) {
            String tableName = entry.getKey();
            int resetValue = entry.getValue();
            
            int currentValue = getCurrentIdentityValue(tableName);
            String status = currentValue > resetValue ? "Cần reset" : "OK";
            String statusClass = currentValue > resetValue ? "error" : "success";
            
            out.println("<tr>");
            out.println("<td>" + tableName + "</td>");
            out.println("<td>" + (currentValue >= 0 ? currentValue : "N/A") + "</td>");
            out.println("<td>" + resetValue + "</td>");
            out.println("<td class='" + statusClass + "'>" + status + "</td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
    }
    
    private void showResetForm(PrintWriter out) {
        out.println("<h2>Reset Identity Values</h2>");
        out.println("<div style='background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0;'>");
        out.println("<strong>Cảnh báo:</strong> Thao tác này sẽ reset giá trị IDENTITY của tất cả các bảng chính về giá trị nhỏ hơn.");
        out.println("<br>Chỉ thực hiện khi bạn chắc chắn muốn reset các ID về giá trị nhỏ.");
        out.println("</div>");
        
        out.println("<form method='get' onsubmit='return confirm(\"Bạn có chắc chắn muốn reset tất cả identity values? Thao tác này không thể hoàn tác!\");'>");
        out.println("<input type='hidden' name='action' value='reset'>");
        out.println("<button type='submit' style='background-color: #dc3545; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;'>Reset All Identity Values</button>");
        out.println("</form>");
    }
    
    private void performReset(PrintWriter out) {
        out.println("<h2>Kết quả Reset</h2>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Tên bảng</th>");
        out.println("<th>Giá trị trước reset</th>");
        out.println("<th>Giá trị sau reset</th>");
        out.println("<th>Kết quả</th>");
        out.println("</tr>");
        
        for (Map.Entry<String, Integer> entry : TABLES_TO_RESET.entrySet()) {
            String tableName = entry.getKey();
            int resetValue = entry.getValue();
            
            int beforeValue = getCurrentIdentityValue(tableName);
            boolean success = resetIdentity(tableName, resetValue);
            int afterValue = getCurrentIdentityValue(tableName);
            
            String result = success ? "Thành công" : "Thất bại";
            String resultClass = success ? "success" : "error";
            
            out.println("<tr>");
            out.println("<td>" + tableName + "</td>");
            out.println("<td>" + (beforeValue >= 0 ? beforeValue : "N/A") + "</td>");
            out.println("<td>" + (afterValue >= 0 ? afterValue : "N/A") + "</td>");
            out.println("<td class='" + resultClass + "'>" + result + "</td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
        
        out.println("<p class='info'>Reset hoàn tất! Các bản ghi mới sẽ sử dụng ID từ giá trị đã reset.</p>");
    }
    
    private int getCurrentIdentityValue(String tableName) {
        String sql = "SELECT IDENT_CURRENT(?) as current_identity";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tableName);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("current_identity");
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error getting current identity value for table: " + tableName, e);
        }
        return -1;
    }
    
    private boolean resetIdentity(String tableName, int newValue) {
        String sql = "DBCC CHECKIDENT (?, RESEED, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tableName);
            ps.setInt(2, newValue);
            ps.execute();
            
            LOGGER.info("Identity value reset for table " + tableName + " to " + newValue);
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error resetting identity value for table: " + tableName, e);
            return false;
        }
    }
    
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        User user = (User) session.getAttribute("user");
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }
}
