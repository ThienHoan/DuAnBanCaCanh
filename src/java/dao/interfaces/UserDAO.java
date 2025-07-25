package dao.interfaces;

import model.entity.User;
import java.util.List;

/**
 * Interface for User Data Access Object
 */
public interface UserDAO {
    
    // Basic CRUD operations
    /**
     * Tạo user mới
     */
    boolean createUser(User user);
    
    /**
     * Lấy user theo ID
     */
    User getUserById(int userId);
    
    /**
     * Lấy user theo username
     */
    User getUserByUsername(String username);
    
    /**
     * Lấy user theo email
     */
    User getUserByEmail(String email);
    
    /**
     * Cập nhật thông tin user
     */
    boolean updateUser(User user);
    
    /**
     * Xóa user (soft delete)
     */
    boolean deleteUser(int userId);
    
    /**
     * Khôi phục user đã xóa
     */
    boolean restoreUser(int userId);
    
    // Admin operations
    /**
     * Lấy tất cả users (cho admin)
     */
    List<User> getAllUsers();
    
    /**
     * Lấy users với phân trang
     */
    List<User> getUsersWithPagination(int offset, int limit);
    
    /**
     * Lấy tổng số users
     */
    int getTotalUsersCount();
    
    /**
     * Tìm kiếm users theo từ khóa
     */
    List<User> searchUsers(String keyword);
    
    /**
     * Lấy users theo role
     */
    List<User> getUsersByRole(String role);
    
    /**
     * Lấy users theo status
     */
    List<User> getUsersByStatus(String status);
    
    /**
     * Cập nhật status của user
     */
    boolean updateUserStatus(int userId, String status);
    
    /**
     * Cập nhật role của user
     */
    boolean updateUserRole(int userId, String role);
    
    /**
     * Cập nhật chỉ mật khẩu của user
     */
    boolean updateUserPassword(int userId, String hashedPassword);
    
    /**
     * Cập nhật last login time
     */
    boolean updateLastLogin(int userId);
    
    /**
     * Kiểm tra username đã tồn tại
     */
    boolean isUsernameExists(String username);
    
    /**
     * Kiểm tra email đã tồn tại
     */
    boolean isEmailExists(String email);
    
    /**
     * Đổi mật khẩu user
     */
    boolean changePassword(int userId, String newPasswordHash);
    
    /**
     * Lấy thống kê users
     */
    UserStatistics getUserStatistics();
    
    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private int totalUsers;
        private int activeUsers;
        private int inactiveUsers;
        private int adminUsers;
        private int customerUsers;
        private int newUsersThisMonth;
        
        // Constructors
        public UserStatistics() {}
        
        public UserStatistics(int totalUsers, int activeUsers, int inactiveUsers, 
                            int adminUsers, int customerUsers, int newUsersThisMonth) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.inactiveUsers = inactiveUsers;
            this.adminUsers = adminUsers;
            this.customerUsers = customerUsers;
            this.newUsersThisMonth = newUsersThisMonth;
        }
        
        // Getters and Setters
        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
        
        public int getActiveUsers() { return activeUsers; }
        public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
        
        public int getInactiveUsers() { return inactiveUsers; }
        public void setInactiveUsers(int inactiveUsers) { this.inactiveUsers = inactiveUsers; }
        
        public int getAdminUsers() { return adminUsers; }
        public void setAdminUsers(int adminUsers) { this.adminUsers = adminUsers; }
        
        public int getCustomerUsers() { return customerUsers; }
        public void setCustomerUsers(int customerUsers) { this.customerUsers = customerUsers; }
        
        public int getNewUsersThisMonth() { return newUsersThisMonth; }
        public void setNewUsersThisMonth(int newUsersThisMonth) { this.newUsersThisMonth = newUsersThisMonth; }
    }
}
