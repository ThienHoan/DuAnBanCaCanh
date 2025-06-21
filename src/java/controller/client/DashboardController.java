package controller.client;

import dao.impl.UserDAOImpl;
import dao.interfaces.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;

import java.io.IOException;

/**
 * Controller xử lý dashboard người dùng
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Kiểm tra đăng nhập
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
          try {
            // Luôn lấy thông tin user mới nhất từ DB để cập nhật avatar
            User updatedUser = userDAO.getUserById(currentUser.getUserId());
            if (updatedUser != null) {
                // Cập nhật session với thông tin mới nhất
                session.setAttribute("user", updatedUser);
                request.setAttribute("user", updatedUser);
                
                // Log để debug
                System.out.println("Dashboard - Updated user avatar: " + updatedUser.getAvatar());
            } else {
                request.setAttribute("user", currentUser);
            }
            
            // Load thống kê dashboard
            loadDashboardStats(request, updatedUser != null ? updatedUser : currentUser);
              // Forward đến trang dashboard
            request.getRequestDispatcher("client/account/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải dashboard");
            request.getRequestDispatcher("client/account/dashboard.jsp").forward(request, response);
        }
    }
    
    /**
     * Load các thống kê cho dashboard
     */
    private void loadDashboardStats(HttpServletRequest request, User user) {
        try {
            // Tính phần trăm hoàn thiện profile
            int completionPercentage = calculateProfileCompletion(user);
            request.setAttribute("profileCompletion", completionPercentage);
            
            // Có thể thêm các thống kê khác ở đây:
            // - Số đơn hàng
            // - Số sản phẩm yêu thích
            // - Hoạt động gần đây
            
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, set giá trị mặc định
            request.setAttribute("profileCompletion", 50);
        }
    }
    
    /**
     * Tính phần trăm hoàn thiện profile
     */
    private int calculateProfileCompletion(User user) {
        int totalFields = 5; // Tổng số trường cần thiết
        int completedFields = 0;
        
        // Kiểm tra các trường bắt buộc
        if (user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
            completedFields++;
        }
        
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            completedFields++;
        }
        
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            completedFields++;
        }
        
        // Kiểm tra các trường tùy chọn
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            completedFields++;
        }
        
        if (user.getAvatar() != null && !user.getAvatar().trim().isEmpty()) {
            completedFields++;
        }
        
        return (completedFields * 100) / totalFields;
    }
}
