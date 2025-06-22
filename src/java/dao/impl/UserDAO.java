package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.User;
import utils.db.DBContext;
import utils.PasswordEncryption;

public class UserDAO {
    
    /**
     * Kiểm tra đăng nhập với mã hóa mật khẩu
     */
    public User checkLogin(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE (username = ? OR email = ?) AND is_deleted = 0 AND status = 'active'";
        
        System.out.println("UserDAO.checkLogin - Username: " + username);
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    System.out.println("Found user, checking password...");
                    
                    // Kiểm tra mật khẩu với mã hóa
                    if (PasswordEncryption.checkPassword(password, storedPassword)) {
                        user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        user.setPhone(rs.getString("phone"));
                        user.setFullName(rs.getString("full_name"));
                        user.setRole(rs.getString("role"));
                        user.setStatus(rs.getString("status"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        user.setLastLogin(rs.getTimestamp("last_login"));
                        user.setAvatar(rs.getString("avatar"));
                        user.setIsDeleted(rs.getBoolean("is_deleted"));
                        
                        // Cập nhật last_login
                        updateLastLogin(user.getUserId());
                        
                        System.out.println("Password match! Login successful for: " + user.getUsername());
                    } else {
                        System.out.println("Password mismatch for user: " + username);
                    }
                } else {
                    System.out.println("No user found with username: " + username);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return user;
    }
    
    /**
     * Đăng ký người dùng mới
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO Users (username, password, email, phone, full_name, role, status, created_at, is_deleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Mã hóa mật khẩu trước khi lưu
            String hashedPassword = PasswordEncryption.hashPassword(user.getPassword());
            
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getFullName());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setBoolean(8, user.isIsDeleted());
            
            int result = ps.executeUpdate();
            System.out.println("User registration result: " + result);
            return result > 0;
            
        } catch ( SQLException e) {
            System.out.println("Database error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error checking email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
      /**
     * Cập nhật thời gian đăng nhập cuối
     */
    public void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET last_login = GETDATE() WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.executeUpdate();
            
        } catch ( SQLException e) {
            System.out.println("Error updating last login: " + e.getMessage());
        }
    }
    
    /**
     * Lấy thông tin user theo ID
     */
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE user_id = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setLastLogin(rs.getTimestamp("last_login"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setIsDeleted(rs.getBoolean("is_deleted"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return user;
    }
    
    /**
     * Lưu reset token cho user
     */
    public boolean saveResetToken(String email, String resetToken) {
        String sql = "UPDATE Users SET reset_token = ?, reset_token_expiry = DATEADD(MINUTE, 15, GETDATE()) " +
                     "WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, resetToken);
            ps.setString(2, email);
            
            int result = ps.executeUpdate();
            System.out.println("Reset token saved for email: " + email);
            return result > 0;
            
        } catch ( SQLException e) {
            System.out.println("Database error saving reset token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xác thực reset token
     */
    public User validateResetToken(String email, String resetToken) {
        String sql = "SELECT * FROM Users WHERE email = ? AND reset_token = ? " +
                     "AND reset_token_expiry > GETDATE() AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, resetToken);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setLastLogin(rs.getTimestamp("last_login"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setIsDeleted(rs.getBoolean("is_deleted"));
                    
                    System.out.println("Reset token validated for email: " + email);
                    return user;
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error validating reset token: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Reset mật khẩu user
     */
    public boolean resetPassword(String email, String newPassword) {
        String sql = "UPDATE Users SET password = ?, reset_token = NULL, reset_token_expiry = NULL " +
                     "WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Mã hóa mật khẩu mới
            String hashedPassword = PasswordEncryption.hashPassword(newPassword);
            
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("Password reset successful for email: " + email);
                return true;
            }
            
        } catch ( SQLException e) {
            System.out.println("Database error resetting password: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Kiểm tra email có tồn tại không
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setLastLogin(rs.getTimestamp("last_login"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setIsDeleted(rs.getBoolean("is_deleted"));
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Xóa reset token đã hết hạn
     */
    public void cleanupExpiredResetTokens() {
        String sql = "UPDATE Users SET reset_token = NULL, reset_token_expiry = NULL " +
                     "WHERE reset_token_expiry < GETDATE()";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Cleaned up " + result + " expired reset tokens");
            }
            
        } catch (SQLException e) {
            System.out.println("Error cleaning up expired tokens: " + e.getMessage());
        }
    }
    
    /**
     * Cập nhật Google ID cho user
     */
    public boolean updateGoogleId(int userId, String googleId) {
        String sql = "UPDATE Users SET google_id = ? WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, googleId);
            ps.setInt(2, userId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Database error updating Google ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật avatar cho user
     */
    public boolean updateAvatar(int userId, String avatar) {
        String sql = "UPDATE Users SET avatar = ? WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, avatar);
            ps.setInt(2, userId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Database error updating avatar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
      /**
     * Tạo user mới từ Google OAuth
     */
    public boolean createGoogleUser(User user) {
        String sql = "INSERT INTO Users (username, email, full_name, google_id, avatar, role, status, created_at, is_deleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getGoogleId());
            ps.setString(5, user.getAvatar());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setBoolean(8, user.isIsDeleted());
            
            int result = ps.executeUpdate();
            System.out.println("Google user creation result: " + result);
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Database error creating Google user: " + e.getMessage());
            
            // Kiểm tra nếu là lỗi Primary Key violation
            if (e.getMessage().contains("PRIMARY KEY constraint")) {
                System.out.println("Primary key violation detected. Attempting to fix identity and retry...");
                
                // Thử fix identity và retry 1 lần
                if (fixUsersIdentity()) {
                    return retryCreateGoogleUser(user);
                }
            }
            
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Fix Users table identity value
     */
    private boolean fixUsersIdentity() {
        String checkSql = "SELECT ISNULL(MAX(user_id), 0) as max_id FROM Users";
        String resetSql = "DBCC CHECKIDENT ('Users', RESEED, ?)";
        
        try (Connection conn = DBContext.getConnection()) {
            // Lấy max user_id hiện tại
            int maxId = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maxId = rs.getInt("max_id");
                }
            }
            
            System.out.println("Current max user_id: " + maxId + ", resetting identity to: " + maxId);
            
            // Reset identity
            try (PreparedStatement ps = conn.prepareStatement(resetSql)) {
                ps.setInt(1, maxId);
                ps.execute();
                System.out.println("Successfully reset Users identity to: " + maxId);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error fixing Users identity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retry creating Google user after fixing identity
     */
    private boolean retryCreateGoogleUser(User user) {
        String sql = "INSERT INTO Users (username, email, full_name, google_id, avatar, role, status, created_at, is_deleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getGoogleId());
            ps.setString(5, user.getAvatar());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setBoolean(8, user.isIsDeleted());
            
            int result = ps.executeUpdate();
            System.out.println("Google user creation retry result: " + result);
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Database error on retry creating Google user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy tổng số người dùng active (không bị xóa)
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM Users WHERE is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting total users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get all users from database
     */
    public List<User> getAllUsers() throws ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE is_deleted = 0 ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                user.setAvatar(rs.getString("avatar"));
                user.setIsDeleted(rs.getBoolean("is_deleted"));
                
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Database error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
}