package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entity.Order;
import utils.db.DBContext;

public class OrderDAO {
    
    /**
     * Lấy tổng số đơn hàng
     */
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting total orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Lấy tổng doanh thu từ các đơn hàng đã hoàn thành
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM Orders " +
                     "WHERE status = 'completed' AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Lấy doanh thu theo tháng của năm hiện tại
     */
    public Map<String, BigDecimal> getMonthlyRevenue() {
        Map<String, BigDecimal> monthlyRevenue = new HashMap<>();
        
        String sql = "SELECT " +
                     "    MONTH(created_at) as month, " +
                     "    COALESCE(SUM(total_amount), 0) as revenue " +
                     "FROM Orders " +
                     "WHERE YEAR(created_at) = YEAR(GETDATE()) " +
                     "    AND status = 'completed' " +
                     "    AND is_deleted = 0 " +
                     "GROUP BY MONTH(created_at) " +
                     "ORDER BY MONTH(created_at)";
        
        // Khởi tạo 12 tháng với giá trị 0
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (String month : months) {
            monthlyRevenue.put(month, BigDecimal.ZERO);
        }
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    BigDecimal revenue = rs.getBigDecimal("revenue");
                    monthlyRevenue.put(months[month - 1], revenue);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting monthly revenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        return monthlyRevenue;
    }
    
    /**
     * Lấy danh sách đơn hàng gần đây
     */
    public List<Order> getRecentOrders(int limit) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT TOP (?) o.*, u.full_name, u.username " +
                     "FROM Orders o " +
                     "LEFT JOIN Users u ON o.user_id = u.user_id " +
                     "WHERE o.is_deleted = 0 " +
                     "ORDER BY o.created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderDate(rs.getTimestamp("created_at"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    // Note: shipping_address_id is just an ID, not the actual address text
                    // For now, set it as empty string to avoid null issues
                    order.setShippingAddress("");
                    order.setNotes(rs.getString("notes"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setIsDeleted(rs.getBoolean("is_deleted"));
                    
                    // Thêm thông tin user
                    order.setCustomerName(rs.getString("full_name"));
                    order.setCustomerUsername(rs.getString("username"));
                    
                    orders.add(order);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting recent orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }
    
    /**
     * Lấy thống kê đơn hàng theo trạng thái
     */
    public Map<String, Integer> getOrderStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count " +
                     "FROM Orders " +
                     "WHERE is_deleted = 0 " +
                     "GROUP BY status";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("status");
                    int count = rs.getInt("count");
                    statistics.put(status, count);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting order statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return statistics;
    }
    
    /**
     * Lấy doanh thu hôm nay
     */
    public BigDecimal getTodayRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM Orders " +
                     "WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE) " +
                     "    AND status = 'completed' " +
                     "    AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting today revenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Lấy số đơn hàng hôm nay
     */
    public int getTodayOrders() {
        String sql = "SELECT COUNT(*) FROM Orders " +
                     "WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE) " +
                     "    AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting today orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Lấy số đơn hàng pending
     */
    public int getPendingOrders() {
        String sql = "SELECT COUNT(*) FROM Orders " +
                     "WHERE status = 'pending' AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting pending orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Lấy tăng trưởng doanh thu so với tháng trước
     */
    public BigDecimal getRevenueGrowth() {
        String sql = "SELECT " +
                     "    COALESCE(SUM(CASE WHEN MONTH(created_at) = MONTH(GETDATE()) THEN total_amount END), 0) as current_month, " +
                     "    COALESCE(SUM(CASE WHEN MONTH(created_at) = MONTH(DATEADD(MONTH, -1, GETDATE())) THEN total_amount END), 0) as last_month " +
                     "FROM Orders " +
                     "WHERE YEAR(created_at) = YEAR(GETDATE()) " +
                     "    AND status = 'completed' " +
                     "    AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal currentMonth = rs.getBigDecimal("current_month");
                    BigDecimal lastMonth = rs.getBigDecimal("last_month");
                    
                    if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
                        return currentMonth.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("100") : BigDecimal.ZERO;
                    }
                    
                    return currentMonth.subtract(lastMonth)
                            .divide(lastMonth, 2, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal("100"));
                }
            }
        } catch ( SQLException e) {
            System.out.println("Database error getting revenue growth: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get all orders from database
     */
    public List<Order> getAllOrders() throws ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE is_deleted = 0 ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
              while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                // Note: shipping_address_id is just an ID, not the actual address text
                // For now, set it as empty string to avoid null issues
                order.setShippingAddress("");
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setIsDeleted(rs.getBoolean("is_deleted"));
                
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Database error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }
}
