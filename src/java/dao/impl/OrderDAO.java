package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.pOrder.Order;
import utils.db.DBContext;

public class OrderDAO {
    
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    
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
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    order.setShippingAddressId(rs.getInt("shipping_address_id"));
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
                order.setOrderNumber(rs.getString("order_number"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setShippingAddressId(rs.getInt("shipping_address_id"));
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
    
    /**
     * Tạo đơn hàng tạm thời cho thanh toán SePay
     * 
     * @param userId người dùng tạo đơn hàng
     * @param cartId giỏ hàng
     * @param orderNumber số đơn hàng (tempOrderNumber)
     * @param addressId địa chỉ giao hàng
     * @param notes ghi chú
     * @param totalAmount tổng tiền đơn hàng
     * @return id của đơn hàng
     */
    public int createTemporaryOrder(int userId, int cartId, String orderNumber, int addressId, String notes, BigDecimal totalAmount) {
        System.out.println("DEBUG - Creating temporary order: " + orderNumber);
        try {
            // Lấy total từ CartDAO
            CartDAO cartDAO = new CartDAO();
            double cartTotal = cartDAO.getCartTotal(cartId);
            BigDecimal discountAmount = BigDecimal.ZERO;
            
            // Tính thuế (5% of cartTotal)
            BigDecimal tax = new BigDecimal(cartTotal * 0.05);
            
            // Phí vận chuyển cố định
            BigDecimal shippingFee = new BigDecimal(20000);
            
            // Tính tổng tiền đơn hàng
            BigDecimal finalTotal = new BigDecimal(cartTotal).add(tax).add(shippingFee).subtract(discountAmount);
            
            // Tạo câu SQL
            String sql = "INSERT INTO Orders (user_id, order_number, shipping_address_id, status, payment_method, payment_status, tax, shipping_fee, discount_amount, notes, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
            
            // Kết nối database
            Connection conn = DBContext.getConnection();
            
            // Thực thi SQL với PreparedStatement để lấy generated key
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setString(2, orderNumber);
                ps.setInt(3, addressId);
                ps.setString(4, "pending"); // Trạng thái chờ xử lý
                ps.setString(5, "bank_transfer"); // Phương thức thanh toán qua SePay
                ps.setString(6, "pending"); // Trạng thái thanh toán chờ xử lý
                ps.setBigDecimal(7, tax);
                ps.setBigDecimal(8, shippingFee);
                ps.setBigDecimal(9, discountAmount);
                ps.setString(10, notes);
                
                // Thực thi SQL
                int result = ps.executeUpdate();
                
                // Lấy ID được sinh ra
                if (result > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int orderId = rs.getInt(1);
                        
                        // Tạo chi tiết đơn hàng
                        createOrderItems(orderId, cartId);
                        
                        System.out.println("DEBUG - Temporary order created with ID: " + orderId);
                        return orderId;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo đơn hàng tạm thời: " + e.getMessage(), e);
            System.out.println("DEBUG - Error creating temporary order: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Tạo chi tiết đơn hàng từ giỏ hàng
     * 
     * @param orderId ID đơn hàng
     * @param cartId ID giỏ hàng
     * @return true nếu tạo thành công, false nếu có lỗi
     */
    private boolean createOrderItems(int orderId, int cartId) {
        System.out.println("DEBUG - Creating order items for order: " + orderId + " from cart: " + cartId);
        try {
            // Lấy kết nối database
            Connection conn = DBContext.getConnection();
            
            // Lấy danh sách sản phẩm từ giỏ hàng
            String selectCartItemsSql = "SELECT * FROM Cart_items WHERE cart_id = ?"; // Tên bảng chính xác là Cart_items, không phải CartItems
            
            String insertOrderItemSql = "INSERT INTO Order_items (order_id, product_id, product_name, quantity, unit_price, subtotal, is_deleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
            
            try (PreparedStatement psSelect = conn.prepareStatement(selectCartItemsSql)) {
                psSelect.setInt(1, cartId);
                
                try (ResultSet rs = psSelect.executeQuery()) {
                    PreparedStatement psInsert = conn.prepareStatement(insertOrderItemSql);
                    int count = 0;
                    
                    while (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");
                        
                        // Lấy thông tin sản phẩm từ bảng Products
                        String productSql = "SELECT name, price FROM Products WHERE product_id = ?";
                        try (PreparedStatement psProduct = conn.prepareStatement(productSql)) {
                            psProduct.setInt(1, productId);
                            
                            try (ResultSet rsProduct = psProduct.executeQuery()) {
                                if (rsProduct.next()) {
                                    String productName = rsProduct.getString("name");
                                    BigDecimal price = rsProduct.getBigDecimal("price");
                                    BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
                                    
                                    psInsert.setInt(1, orderId);
                                    psInsert.setInt(2, productId);
                                    psInsert.setString(3, productName);
                                    psInsert.setInt(4, quantity);
                                    psInsert.setBigDecimal(5, price);
                                    psInsert.setBigDecimal(6, subtotal);
                                    
                                    psInsert.addBatch();
                                    count++;
                                    System.out.println("DEBUG - Added item to batch: " + productName + ", quantity: " + quantity);
                                }
                            }
                        }
                    }
                    
                    if (count > 0) {
                        int[] results = psInsert.executeBatch();
                        System.out.println("DEBUG - Created " + results.length + " order items");
                        return true;
                    }
                    
                    psInsert.close();
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.out.println("DEBUG - Error creating order items: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy thông tin đơn hàng theo số đơn hàng (không phân biệt hoa thường)
     * 
     * @param orderNumber số đơn hàng cần tìm
     * @return đơn hàng nếu tìm thấy, null nếu không tìm thấy
     */
    public Order getOrderByOrderNumberCaseInsensitive(String orderNumber) {
        System.out.println("DEBUG - getOrderByOrderNumberCaseInsensitive - searching for: " + orderNumber);
        try {
            String sql = "SELECT * FROM Orders WHERE LOWER(order_number) = LOWER(?) AND is_deleted = 0";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, orderNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Order order = new Order();
                        order.setOrderId(rs.getInt("order_id"));
                        order.setUserId(rs.getInt("user_id"));
                        order.setOrderNumber(rs.getString("order_number"));
                        order.setStatus(rs.getString("status"));
                        order.setPaymentMethod(rs.getString("payment_method"));
                        order.setPaymentStatus(rs.getString("payment_status"));
                        order.setShippingAddressId(rs.getInt("shipping_address_id"));
                        order.setTax(rs.getBigDecimal("tax"));
                        order.setShippingFee(rs.getBigDecimal("shipping_fee"));
                        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
                        order.setNotes(rs.getString("notes"));
                        order.setCreatedAt(rs.getTimestamp("created_at"));
                        order.setUpdatedAt(rs.getTimestamp("updated_at"));
                        System.out.println("DEBUG - getOrderByOrderNumberCaseInsensitive - found order with ID: " + order.getOrderId());
                        return order;
                    } else {
                        System.out.println("DEBUG - getOrderByOrderNumberCaseInsensitive - no order found for: " + orderNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG - getOrderByOrderNumberCaseInsensitive - error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy thông tin đơn hàng theo số đơn hàng
     * 
     * @param orderNumber số đơn hàng cần tìm
     * @return đơn hàng nếu tìm thấy, null nếu không tìm thấy
     */
    public Order getOrderByOrderNumber(String orderNumber) {
        System.out.println("DEBUG - getOrderByOrderNumber - searching for: " + orderNumber);
        try {
            String sql = "SELECT * FROM Orders WHERE order_number = ? AND is_deleted = 0";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, orderNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Order order = new Order();
                        order.setOrderId(rs.getInt("order_id"));
                        order.setUserId(rs.getInt("user_id"));
                        order.setOrderNumber(rs.getString("order_number"));
                        order.setStatus(rs.getString("status"));
                        order.setPaymentMethod(rs.getString("payment_method"));
                        order.setPaymentStatus(rs.getString("payment_status"));
                        order.setShippingAddressId(rs.getInt("shipping_address_id"));
                        order.setTax(rs.getBigDecimal("tax"));
                        order.setShippingFee(rs.getBigDecimal("shipping_fee"));
                        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
                        order.setNotes(rs.getString("notes"));
                        order.setCreatedAt(rs.getTimestamp("created_at"));
                        order.setUpdatedAt(rs.getTimestamp("updated_at"));
                        System.out.println("DEBUG - getOrderByOrderNumber - found order with ID: " + order.getOrderId());
                        return order;
                    } else {
                        System.out.println("DEBUG - getOrderByOrderNumber - no order found for: " + orderNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DEBUG - getOrderByOrderNumber - error: " + e.getMessage());
        }
        return null;
    }
}
