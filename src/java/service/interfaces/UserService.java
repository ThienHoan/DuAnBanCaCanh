package service.interfaces;

import dao.interfaces.UserDAO.UserStatistics;
import model.entity.User;
import java.util.List;

/**
 * Interface for User Service Layer
 */
public interface UserService {
    
    // Public user operations
    /**
     * Đăng ký user mới
     */
    boolean registerUser(User user);
    
    /**
     * Đăng nhập
     */
    User login(String username, String password);
    
    /**
     * Đăng nhập bằng Google
     */
    User loginWithGoogle(String googleId, String email, String fullName, String avatar);
    
    /**
     * Lấy thông tin user theo ID
     */
    User getUserById(int userId);
    
    /**
     * Cập nhật profile user
     */
    boolean updateProfile(User user);
    
    /**
     * Đổi mật khẩu
     */
    boolean changePassword(int userId, String oldPassword, String newPassword);
    
    /**
     * Kiểm tra username có tồn tại không
     */
    boolean isUsernameExists(String username);
    
    /**
     * Kiểm tra email có tồn tại không
     */
    boolean isEmailExists(String email);
    
    /**
     * Cập nhật last login
     */
    boolean updateLastLogin(int userId);
    
    // Admin operations
    /**
     * Lấy tất cả users (admin only)
     */
    List<User> getAllUsers(int adminUserId);
    
    /**
     * Lấy users với phân trang (admin only)
     */
    List<User> getUsersWithPagination(int page, int pageSize, int adminUserId);
    
    /**
     * Tìm kiếm users (admin only)
     */
    List<User> searchUsers(String keyword, int adminUserId);
    
    /**
     * Lấy users theo role (admin only)
     */
    List<User> getUsersByRole(String role, int adminUserId);
    
    /**
     * Lấy users theo status (admin only)
     */
    List<User> getUsersByStatus(String status, int adminUserId);
    
    /**
     * Tạo user mới (admin only)
     */
    boolean createUser(User user, int adminUserId);
    
    /**
     * Cập nhật thông tin user (admin only)
     */
    boolean updateUser(User user, int adminUserId);
    
    /**
     * Xóa user (admin only)
     */
    boolean deleteUser(int userId, int adminUserId);
    
    /**
     * Khôi phục user (admin only)
     */
    boolean restoreUser(int userId, int adminUserId);
    
    /**
     * Cập nhật status user (admin only)
     */
    boolean updateUserStatus(int userId, String status, int adminUserId);
    
    /**
     * Cập nhật role user (admin only)
     */
    boolean updateUserRole(int userId, String role, int adminUserId);
    
    /**
     * Reset mật khẩu user (admin only)
     */
    boolean resetPassword(int userId, String newPassword, int adminUserId);
    
    /**
     * Lấy tổng số trang
     */
    int getTotalPages(int pageSize, int adminUserId);
    
    /**
     * Lấy thống kê users (admin only)
     */
    UserStatistics getUserStatistics(int adminUserId);
    
    /**
     * Kiểm tra user có phải admin không
     */
    boolean isUserAdmin(int userId);
    
    /**
     * Validate user data
     */
    boolean validateUserData(User user);
    
    /**
     * Validate password strength
     */
    boolean validatePassword(String password);
}
