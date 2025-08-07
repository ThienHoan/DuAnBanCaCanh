package controller.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.impl.UserDAOImpl;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet để kiểm tra và reset giá trị IDENTITY của bảng Users
 */
public class ResetIdentityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        UserDAOImpl userDAO = new UserDAOImpl();
        int currentValue = userDAO.getCurrentIdentityValue();
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Reset Identity Value</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1, h2 { color: #333; }");
            out.println("form { margin: 20px 0; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }");
            out.println(".success { color: green; font-weight: bold; }");
            out.println(".error { color: red; font-weight: bold; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>SQL Server Identity Management</h1>");
            
            String action = request.getParameter("action");
            String newValueStr = request.getParameter("newValue");
            
            if ("reset".equals(action) && newValueStr != null && !newValueStr.isEmpty()) {
                try {
                    int newValue = Integer.parseInt(newValueStr);
                    boolean success = userDAO.resetIdentity(newValue);
                    
                    if (success) {
                        out.println("<p class='success'>Đã reset giá trị IDENTITY thành công về " + newValue + "</p>");
                        currentValue = userDAO.getCurrentIdentityValue();
                    } else {
                        out.println("<p class='error'>Không thể reset giá trị IDENTITY</p>");
                    }
                } catch (NumberFormatException e) {
                    out.println("<p class='error'>Giá trị không hợp lệ: " + newValueStr + "</p>");
                }
            }
            
            out.println("<h2>Thông tin hiện tại</h2>");
            out.println("<p>Giá trị IDENTITY hiện tại của bảng Users: <strong>" + currentValue + "</strong></p>");
            
            out.println("<h2>Reset giá trị IDENTITY</h2>");
            out.println("<form method='get'>");
            out.println("<input type='hidden' name='action' value='reset'>");
            out.println("<label for='newValue'>Giá trị mới:</label>");
            out.println("<input type='number' name='newValue' id='newValue' value='5' min='0' required>");
            out.println("<button type='submit'>Reset IDENTITY</button>");
            out.println("</form>");
            
            out.println("<p><a href='" + request.getContextPath() + "/admin-dashboard'>Quay lại Dashboard</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
