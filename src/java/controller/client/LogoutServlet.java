package controller.client;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.SessionUtils;

/**
 * Servlet xử lý đăng xuất
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    /**
     * Xử lý GET request - đăng xuất người dùng
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session hiện tại
        HttpSession session = request.getSession(false);
        
        // Nếu session tồn tại, hủy session
        if (session != null) {
            System.out.println("Logging out user session");
            session.invalidate();
        }
        
        // KHÔNG xóa remember me cookies khi đăng xuất
        // Điều này cho phép form đăng nhập vẫn hiển thị thông tin đã lưu
        
        // Chuyển hướng về trang đăng nhập
        response.sendRedirect("login");
    }

    /**
     * Xử lý POST request - chuyển hướng đến doGet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 