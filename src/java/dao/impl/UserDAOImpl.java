package dao.impl;

import dao.interfaces.UserDAO;
import model.entity.User;
import utils.db.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of UserDAO interface
 */
public class UserDAOImpl implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());
    
    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, phone, full_name, role, status, avatar, google_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getFullName());
            ps.setString(6, user.getRole() != null ? user.getRole() : "customer");
            ps.setString(7, user.getStatus() != null ? user.getStatus() : "active");
            ps.setString(8, user.getAvatar());
            ps.setString(9, user.getGoogleId());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    }
                }
                LOGGER.info("User created successfully: " + user.getUsername());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: " + user.getUsername(), e);
        }
        return false;
    }
    
    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: " + userId, e);
        }
        return null;
    }
    
    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username: " + username, e);
        }
        return null;
    }
    
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
        }
        return null;
    }
      @Override    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, full_name = ?, " +
                     "role = ?, status = ?, avatar = ?, google_id = ?, password = ? " +
                     "WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());
            ps.setString(7, user.getAvatar());
            ps.setString(8, user.getGoogleId());
            ps.setString(9, user.getPassword());
            ps.setInt(10, user.getUserId());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("User updated successfully: " + user.getUsername());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUsername(), e);
        }
        return false;
    }
    
    @Override    public boolean deleteUser(int userId) {
        String sql = "UPDATE users SET is_deleted = 1 WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("User soft deleted successfully: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + userId, e);
        }
        return false;
    }
      @Override
    public boolean restoreUser(int userId) {
        String sql = "UPDATE users SET is_deleted = 0 WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("User restored successfully: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error restoring user: " + userId, e);
        }
        return false;
    }
    
    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users WHERE is_deleted = 0 ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            LOGGER.info("Retrieved " + users.size() + " users");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        return users;
    }
    
    @Override
    public List<User> getUsersWithPagination(int offset, int limit) {
        String sql = "SELECT * FROM users WHERE is_deleted = 0 ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
            LOGGER.info("Retrieved " + users.size() + " users with pagination");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users with pagination", e);
        }
        return users;
    }
    
    @Override
    public int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total users count", e);
        }
        return 0;
    }
    
    @Override
    public List<User> searchUsers(String keyword) {
        String sql = "SELECT * FROM users WHERE is_deleted = 0 AND " +
                     "(username LIKE ? OR email LIKE ? OR full_name LIKE ? OR phone LIKE ?) " +
                     "ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
            LOGGER.info("Found " + users.size() + " users matching keyword: " + keyword);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching users with keyword: " + keyword, e);
        }
        return users;
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ? AND is_deleted = 0 ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, role);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by role: " + role, e);
        }
        return users;
    }
    
    @Override
    public List<User> getUsersByStatus(String status) {
        String sql = "SELECT * FROM users WHERE status = ? AND is_deleted = 0 ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by status: " + status, e);
        }
        return users;
    }
    
    @Override    public boolean updateUserStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, userId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("User status updated successfully: " + userId + " -> " + status);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user status: " + userId, e);
        }
        return false;
    }
    
    @Override    public boolean updateUserRole(int userId, String role) {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, role);
            ps.setInt(2, userId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("User role updated successfully: " + userId + " -> " + role);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user role: " + userId, e);
        }
        return false;
    }
    
    @Override
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating last login: " + userId, e);
        }
        return false;
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking username exists: " + username, e);
        }
        return false;
    }
    
    @Override
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking email exists: " + email, e);
        }
        return false;
    }
      @Override
    public boolean changePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                LOGGER.info("Password changed successfully for user: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error changing password for user: " + userId, e);
        }
        return false;
    }
    
    @Override
    public UserStatistics getUserStatistics() {
        String sql = "SELECT " +
                     "COUNT(*) as total_users, " +
                     "SUM(CASE WHEN status = 'active' THEN 1 ELSE 0 END) as active_users, " +
                     "SUM(CASE WHEN status = 'inactive' THEN 1 ELSE 0 END) as inactive_users, " +
                     "SUM(CASE WHEN role = 'admin' THEN 1 ELSE 0 END) as admin_users, " +
                     "SUM(CASE WHEN role = 'customer' THEN 1 ELSE 0 END) as customer_users, " +
                     "SUM(CASE WHEN created_at >= DATEADD(MONTH, -1, GETDATE()) THEN 1 ELSE 0 END) as new_users_this_month " +
                     "FROM users WHERE is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return new UserStatistics(
                    rs.getInt("total_users"),
                    rs.getInt("active_users"),
                    rs.getInt("inactive_users"),
                    rs.getInt("admin_users"),
                    rs.getInt("customer_users"),
                    rs.getInt("new_users_this_month")
                );
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user statistics", e);
        }
        return new UserStatistics();
    }
    
    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setAvatar(rs.getString("avatar"));
        user.setGoogleId(rs.getString("google_id"));
        user.setIsDeleted(rs.getBoolean("is_deleted"));
        return user;
    }
    
    /**
     * Lấy giá trị hiện tại của IDENTITY trong bảng Users
     * @return Giá trị IDENTITY hiện tại
     */
    public int getCurrentIdentityValue() {
        String sql = "SELECT IDENT_CURRENT('Users') as current_identity";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("current_identity");
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting current identity value", e);
        }
        return -1;
    }
    
    /**
     * Reset giá trị IDENTITY của bảng Users
     * @param newValue Giá trị mới cho IDENTITY
     * @return true nếu reset thành công, false nếu có lỗi
     */
    public boolean resetIdentity(int newValue) {
        String sql = "DBCC CHECKIDENT ('Users', RESEED, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, newValue);
            ps.execute();
            
            LOGGER.info("Identity value reset to " + newValue);
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error resetting identity value", e);
            return false;
        }
    }    /**
     * Cập nhật chỉ mật khẩu của user
     */
    public boolean updateUserPassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        System.out.println("=== DEBUG: updateUserPassword called ===");
        System.out.println("User ID: " + userId);
        System.out.println("Hashed password length: " + (hashedPassword != null ? hashedPassword.length() : "null"));
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            System.out.println("Database connection established");
            
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            
            System.out.println("Executing update query...");
            int result = ps.executeUpdate();
            System.out.println("Update result: " + result + " rows affected");
            
            if (result > 0) {
                LOGGER.info("Password updated successfully for user ID: " + userId);
                System.out.println("Password update successful");
                return true;
            } else {
                System.out.println("No rows were updated - user ID might not exist");
            }
            
        } catch (SQLException e) {
            System.out.println("SQL Exception in updateUserPassword: " + e.getMessage());
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error updating password for user ID: " + userId, e);
        }
        
        System.out.println("Password update failed");
        return false;
    }
}
