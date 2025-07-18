package controller.client;

import dao.impl.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.User;
import utils.EmailUtil;

/**
 * Servlet xử lý quên mật khẩu
 */
// @WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    /**
     * Xử lý GET request - hiển thị trang quên mật khẩu
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Chuyển hướng đến trang quên mật khẩu
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    /**
     * Xử lý POST request - xử lý yêu cầu quên mật khẩu
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set encoding để hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Lấy email từ form
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email!");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("Forgot password request for email: " + email);
        
        UserDAO userDAO = new UserDAO();
        
        // Kiểm tra email có tồn tại không
        User user = userDAO.getUserByEmail(email.trim());
        
        if (user != null) {
            // Tạo mã reset
            String resetCode = EmailUtil.generateResetCode();
            
            // Lưu reset token vào database
            boolean tokenSaved = userDAO.saveResetToken(email, resetCode);
            
            if (tokenSaved) {
                // Gửi email
                boolean emailSent = EmailUtil.sendResetPasswordEmail(email, resetCode);
                
                if (emailSent) {
                    System.out.println("Reset code sent successfully to: " + email);
                    
                    // Redirect đến trang reset password với thông báo thành công
                    request.getSession().setAttribute("resetEmail", email);
                    request.getSession().setAttribute("successMessage", 
                        "Mã reset mật khẩu đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư!");
                    
                    response.sendRedirect("reset-password");
                } else {
                    System.out.println("Failed to send email to: " + email);
                    request.setAttribute("error", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau!");
                    request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
                }
            } else {
                System.out.println("Failed to save reset token for: " + email);
                request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau!");
                request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            }
        } else {
            // Không tìm thấy email - hiển thị lỗi rõ ràng
            System.out.println("Email not found: " + email);
            request.setAttribute("error", "Email này không tồn tại trong hệ thống!");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        }
    }
} 