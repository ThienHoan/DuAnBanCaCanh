package controller.client;

import dao.impl.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import utils.EmailUtil;

/**
 * Servlet xử lý reset mật khẩu
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    /**
     * Xử lý GET request - hiển thị trang reset mật khẩu
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Chuyển hướng đến trang reset mật khẩu
        request.getRequestDispatcher("reset-password.jsp").forward(request, response);
    }

    /**
     * Xử lý POST request - xử lý reset mật khẩu
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set encoding để hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Lấy thông tin từ form
        String email = request.getParameter("email");
        String resetCode = request.getParameter("resetCode");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        System.out.println("Reset password attempt for email: " + email);
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (email == null || email.trim().isEmpty()) {
            errors.append("Vui lòng nhập địa chỉ email. ");
        }
        
        if (resetCode == null || resetCode.trim().isEmpty()) {
            errors.append("Vui lòng nhập mã xác thực. ");
        } else if (resetCode.trim().length() != 6) {
            errors.append("Mã xác thực phải có 6 chữ số. ");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.append("Vui lòng nhập mật khẩu mới. ");
        } else if (newPassword.length() < 6) {
            errors.append("Mật khẩu mới phải có ít nhất 6 ký tự. ");
        }
        
        if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
            errors.append("Xác nhận mật khẩu không khớp. ");
        }
        
        // Nếu có lỗi validation
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("enteredEmail", email);
            request.setAttribute("enteredResetCode", resetCode);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        
        // Cleanup expired tokens trước khi validate
        userDAO.cleanupExpiredResetTokens();
        
        // Xác thực reset token
        User user = userDAO.validateResetToken(email.trim(), resetCode.trim());
        
        if (user != null) {
            // Token hợp lệ, reset mật khẩu
            boolean resetSuccess = userDAO.resetPassword(email.trim(), newPassword);
            
            if (resetSuccess) {
                System.out.println("Password reset successful for: " + email);
                
                // Gửi email thông báo
                EmailUtil.sendPasswordChangedNotification(email, user.getFullName());
                
                // Xóa thông tin trong session
                HttpSession session = request.getSession();
                session.removeAttribute("resetEmail");
                session.removeAttribute("successMessage");
                
                // Redirect về trang login với thông báo thành công
                request.setAttribute("success", 
                    "Mật khẩu đã được đặt lại thành công! Vui lòng đăng nhập với mật khẩu mới.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                
            } else {
                System.out.println("Failed to reset password for: " + email);
                request.setAttribute("error", "Có lỗi xảy ra khi đặt lại mật khẩu. Vui lòng thử lại!");
                request.setAttribute("enteredEmail", email);
                request.setAttribute("enteredResetCode", resetCode);
                request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            }
            
        } else {
            // Token không hợp lệ hoặc đã hết hạn
            System.out.println("Invalid or expired token for email: " + email);
            request.setAttribute("error", "Mã xác thực không đúng hoặc đã hết hạn. Vui lòng yêu cầu mã mới!");
            request.setAttribute("enteredEmail", email);
            request.setAttribute("enteredResetCode", resetCode);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }
} 