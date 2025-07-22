package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Notification;
import utils.db.DBContext;

/**
 * DAO để xử lý thông báo
 */
public class NotificationDAO {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationDAO.class.getName());
    
    /**
     * Tạo thông báo thanh toán thành công
     * 
     * @param userId ID của người dùng
     * @param orderId ID của đơn hàng
     * @param message Nội dung thông báo
     * @return true nếu tạo thành công, false nếu có lỗi
     */
    public boolean createPaymentSuccessNotification(int userId, int orderId, String message) {
        String sql = "INSERT INTO Notifications (user_id, reference_id, reference_type, message, type, is_read, created_at) "
                + "VALUES (?, ?, 'order', ?, 'payment_success', 0, GETDATE())";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, orderId);
            ps.setString(3, message);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating payment success notification", e);
            System.out.println("DEBUG - Error creating notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy danh sách thông báo chưa đọc của người dùng
     * 
     * @param userId ID của người dùng
     * @return Danh sách thông báo chưa đọc
     */
    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        
        String sql = "SELECT * FROM Notifications WHERE user_id = ? AND is_read = 0 ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationId(rs.getInt("notification_id"));
                    notification.setUserId(rs.getInt("user_id"));
                    notification.setReferenceId(rs.getInt("reference_id"));
                    notification.setReferenceType(rs.getString("reference_type"));
                    notification.setMessage(rs.getString("message"));
                    notification.setType(rs.getString("type"));
                    notification.setRead(rs.getBoolean("is_read"));
                    notification.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    notifications.add(notification);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unread notifications", e);
            System.out.println("DEBUG - Error getting unread notifications: " + e.getMessage());
        }
        
        return notifications;
    }
    
    /**
     * Lấy danh sách thông báo mới nhất của người dùng, bất kể đã đọc hay chưa
     * 
     * @param userId ID của người dùng
     * @param limit Số lượng thông báo tối đa cần lấy
     * @return Danh sách thông báo mới nhất
     */
    public List<Notification> getRecentNotifications(int userId, int limit) {
        List<Notification> notifications = new ArrayList<>();
        
        String sql = "SELECT TOP " + limit + " * FROM Notifications WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationId(rs.getInt("notification_id"));
                    notification.setUserId(rs.getInt("user_id"));
                    notification.setReferenceId(rs.getInt("reference_id"));
                    notification.setReferenceType(rs.getString("reference_type"));
                    notification.setMessage(rs.getString("message"));
                    notification.setType(rs.getString("type"));
                    notification.setRead(rs.getBoolean("is_read"));
                    notification.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    notifications.add(notification);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent notifications", e);
            System.out.println("DEBUG - Error getting recent notifications: " + e.getMessage());
        }
        
        return notifications;
    }
    
    /**
     * Đánh dấu thông báo đã đọc
     * 
     * @param notificationId ID của thông báo
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET is_read = 1 WHERE notification_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking notification as read", e);
            return false;
        }
    }
    
    /**
     * Đánh dấu tất cả thông báo của người dùng đã đọc
     * 
     * @param userId ID của người dùng
     * @return số thông báo đã cập nhật
     */
    public int markAllAsRead(int userId) {
        String sql = "UPDATE Notifications SET is_read = 1 WHERE user_id = ? AND is_read = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            return ps.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking all notifications as read", e);
            return 0;
        }
    }

    /**
     * Đánh dấu tất cả thông báo thanh toán thành công của người dùng là đã đọc
     * 
     * @param userId ID của người dùng
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean markAllPaymentNotificationsAsRead(int userId) {
        String sql = "UPDATE Notifications SET is_read = 1 WHERE user_id = ? AND type = 'payment_success'";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int rowsAffected = ps.executeUpdate();
            
            System.out.println("DEBUG - Marked " + rowsAffected + " payment notifications as read for user " + userId);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking all payment notifications as read", e);
            System.out.println("DEBUG - Error marking all payment notifications as read: " + e.getMessage());
            return false;
        }
    }
} 