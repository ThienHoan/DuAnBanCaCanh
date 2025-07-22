package dao.impl.pOrder;

import dao.impl.CartDAO;
import dao.impl.ProductDAO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.entity.Address;
import model.entity.Coupon;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;
import model.entity.pOrder.Order;
import model.entity.pOrder.OrderItem;
import model.entity.pOrder.Payment;
import utils.db.DBContext;
import model.entity.Product;
import utils.InventoryLogUtil;

public class OrderDAOImpl {
    private static final Logger LOGGER = Logger.getLogger(OrderDAOImpl.class.getName());
    private final Connection conn;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderDAOImpl() {
        try {
            conn = DBContext.getConnection();
            cartDAO = new CartDAO();
            productDAO = new ProductDAO();
        } catch (Exception e) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu: " + e.getMessage());
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu", e);
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ? AND is_deleted = 0 ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setOrderItems(getOrderItems(order.getOrderId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setOrderItems(getOrderItems(order.getOrderId()));
                    return order;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }

    public Order getOrderByOrderNumber(String orderNumber) {
        String sql = "SELECT * FROM Orders WHERE order_number = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setOrderItems(getOrderItems(order.getOrderId()));
                    return order;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }

    private List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM Order_items WHERE order_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("order_item_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setSubtotal(rs.getDouble("subtotal"));
                    item.setIsDeleted(rs.getBoolean("is_deleted"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage(), e);
        }
        return items;
    }

    /**
     * Get order items for a specific order - public method for external use
     * @param orderId The order ID
     * @return List of order items
     */
    public List<OrderItem> getOrderItemsById(int orderId) {
        return getOrderItems(orderId);
    }

    public int createOrder(int userId, int cartId, String orderNumber, int addressId, String paymentMethod, String notes, BigDecimal discountAmount) throws SQLException {
        String orderSql = "INSERT INTO Orders (user_id, order_number, status, total_amount, discount_amount, shipping_fee, tax, final_amount, payment_method, payment_status, shipping_address_id, billing_address_id, notes, created_at, updated_at, is_deleted) VALUES (?, ?, 'pending', ?, ?, ?, ?, ?, ?, 'pending', ?, ?, ?, GETDATE(), GETDATE(), 0)";
        String orderItemSql = "INSERT INTO Order_items (order_id, product_id, product_name, quantity, unit_price, subtotal, is_deleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
        String paymentSql = "INSERT INTO Payments (order_id, payment_method, transaction_id, amount, status, payment_date, payment_details) VALUES (?, ?, ?, ?, ?, GETDATE(), ?)";
        
        // REMOVED: Inventory update SQL - inventory will only be updated when admin confirms shipping
        // This prevents overload during order creation and ensures inventory is only updated when goods are actually shipped

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cartId);
        
        for (CartItem item : cartItems) {
            double price = item.getProductPrice();
            totalAmount = totalAmount.add(BigDecimal.valueOf(price * item.getQuantity()));
        }
        
        // Default values
        BigDecimal shippingFee = new BigDecimal("20000");
        // Calculate tax (5% of cart total)
        BigDecimal tax = totalAmount.multiply(new BigDecimal("0.05"))
                .setScale(2, java.math.RoundingMode.HALF_UP); // Round to 2 decimal places
        
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        
        BigDecimal finalAmount = totalAmount.subtract(discountAmount).add(shippingFee).add(tax)
                .setScale(2, java.math.RoundingMode.HALF_UP); // Round to 2 decimal places

        try {
            conn.setAutoCommit(false);
            int orderId;

            try (PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderPs.setInt(1, userId);
                orderPs.setString(2, orderNumber);
                orderPs.setBigDecimal(3, totalAmount);
                orderPs.setBigDecimal(4, discountAmount);
                orderPs.setBigDecimal(5, shippingFee);
                orderPs.setBigDecimal(6, tax);
                orderPs.setBigDecimal(7, finalAmount);
                orderPs.setString(8, paymentMethod);
                orderPs.setInt(9, addressId);
                orderPs.setInt(10, addressId);
                orderPs.setString(11, notes);
                orderPs.executeUpdate();

                try (ResultSet generatedKeys = orderPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);

                        // Insert order items
                        try (PreparedStatement itemPs = conn.prepareStatement(orderItemSql)) {
                            for (CartItem item : cartItems) {
                                double price = item.getProductPrice();
                                itemPs.setInt(1, orderId);
                                itemPs.setInt(2, item.getProductId());
                                itemPs.setString(3, item.getProductName());
                                itemPs.setInt(4, item.getQuantity());
                                itemPs.setDouble(5, price);
                                itemPs.setDouble(6, price * item.getQuantity());
                                itemPs.executeUpdate();
                            }
                        }

                        // Insert payment record
                        try (PreparedStatement paymentPs = conn.prepareStatement(paymentSql)) {
                            String transactionId = "TXN" + orderNumber;
                            String paymentStatus = "pending";
                            String paymentDetails = "Payment method: " + paymentMethod;
                            
                            paymentPs.setInt(1, orderId);
                            paymentPs.setString(2, paymentMethod);
                            paymentPs.setString(3, transactionId);
                            paymentPs.setBigDecimal(4, finalAmount);
                            paymentPs.setString(5, paymentStatus);
                            paymentPs.setString(6, paymentDetails);
                            paymentPs.executeUpdate();
                        }

                        // Empty the cart
                        cartDAO.clearCart(cartId);

                        // REMOVED: updateInventoryAfterOrderCreation(orderId, cartItems, conn);
                        // Inventory will only be updated when admin confirms shipping to prevent overload

                        conn.commit();
                        return orderId;
                    } else {
                        conn.rollback();
                        throw new SQLException("Không thể tạo đơn hàng, không có ID được trả về.");
                    }
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Overloaded method to accept Cart object instead of cartId
    public int createOrder(int userId, Cart cart, String orderNumber, int addressId, String paymentMethod, String notes, BigDecimal discountAmount) throws SQLException {
        // Get the cart ID from the cart object
        int cartId = cart.getCartId();
        
        // Call the existing method
        return createOrder(userId, cartId, orderNumber, addressId, paymentMethod, notes, discountAmount);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ?, updated_at = GETDATE() WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            int result = ps.executeUpdate();
            
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage(), e);
            return false;
        }
    }
    
        /**
     * REMOVED: updateInventoryAfterOrderCreation method
     * Inventory will only be updated when admin confirms shipping to prevent overload during order creation
     * and ensure inventory is only updated when goods are actually shipped
     */



    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        String sql = "UPDATE Orders SET payment_status = ?, updated_at = GETDATE() WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentStatus);
            ps.setInt(2, orderId);
            int result = ps.executeUpdate();
            
            // REMOVED: updateInventoryAfterPayment(orderId);
            // Inventory will only be updated when admin confirms shipping to prevent duplicate logs
            // and ensure inventory is only updated when goods are actually shipped
            
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái thanh toán: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * REMOVED: updateInventoryAfterPayment method
     * Inventory will only be updated when admin confirms shipping to prevent duplicate logs
     * and ensure inventory is only updated when goods are actually shipped
     */

    public boolean cancelOrder(int orderId) {
        try {
            conn.setAutoCommit(false);
            
            // Update order status
            String updateOrderSql = "UPDATE Orders SET status = 'cancelled', updated_at = GETDATE() WHERE order_id = ? AND status IN ('pending', 'confirmed', 'processing')";
            try (PreparedStatement ps = conn.prepareStatement(updateOrderSql)) {
                ps.setInt(1, orderId);
                int rows = ps.executeUpdate();
                
                if (rows > 0) {
                    // REMOVED: Restore inventory for cancelled order
                    // Inventory is only updated when admin confirms shipping, so no need to restore here
                    // This prevents inventory inconsistencies and simplifies the cancellation process
                    
                    conn.commit();
                    LOGGER.log(Level.INFO, "Đã hủy đơn hàng #" + orderId + " thành công");
                    return true;
                } else {
                    conn.rollback();
                    LOGGER.log(Level.WARNING, "Không thể hủy đơn hàng #" + orderId + " - trạng thái không hợp lệ");
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi khi rollback transaction: " + ex.getMessage(), ex);
            }
            LOGGER.log(Level.SEVERE, "Lỗi khi hủy đơn hàng: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đặt lại autocommit: " + e.getMessage(), e);
            }
        }
    }

    public String generateOrderNumber() {
        return "ORD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public String validateCartStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            int availableStock = productDAO.getProductQuantity(item.getProductId());
            if (availableStock < item.getQuantity()) {
                return "Sản phẩm '" + item.getProductName() + "' chỉ còn " + availableStock + " sản phẩm trong kho.";
            }
        }
        return null;
    }
    
    /**
     * Validate stock for shipping confirmation - more strict validation
     * This is called when admin confirms shipping to ensure stock is still available
     */
    public String validateOrderStockForShipping(int orderId) {
        try {
            List<OrderItem> orderItems = getOrderItems(orderId);
            for (OrderItem item : orderItems) {
                int availableStock = productDAO.getProductQuantity(item.getProductId());
                if (availableStock < item.getQuantity()) {
                    return "Sản phẩm '" + item.getProductName() + "' chỉ còn " + availableStock + " sản phẩm trong kho, không đủ để giao hàng.";
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra stock cho giao hàng: " + e.getMessage(), e);
            return "Có lỗi xảy ra khi kiểm tra stock.";
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setShippingFee(rs.getBigDecimal("shipping_fee"));
        order.setTax(rs.getBigDecimal("tax"));
        order.setFinalAmount(rs.getBigDecimal("final_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setShippingAddressId(rs.getInt("shipping_address_id"));
        order.setBillingAddressId(rs.getInt("billing_address_id"));
        order.setNotes(rs.getString("notes"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        order.setIsDeleted(rs.getBoolean("is_deleted"));
        return order;
    }

    public Map<String, Object> createVNPayUrl(String orderNumber, BigDecimal amount, String returnUrl, String ipAddr) {
        // VNPay configuration
        String vnp_TmnCode = "25SYU20K";
        String vnp_HashSecret = "T29F4QPB100BW89WAVJXB3PQNRZ4F2P1";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        
        Map<String, Object> result = new HashMap<>();
        Map<String, String> vnp_Params = new HashMap<>();
        
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.multiply(new BigDecimal("100")).longValue()));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderNumber);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderNumber);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddr);
        
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        result.put("vnp_Params", vnp_Params);
        result.put("vnp_Url", vnp_Url);
        result.put("vnp_HashSecret", vnp_HashSecret);
        
        return result;
    }
    
    /**
     * Lấy tất cả đơn hàng trong hệ thống
     * @return Danh sách đơn hàng
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username, u.full_name, u.email FROM Orders o " +
                    "LEFT JOIN Users u ON o.user_id = u.user_id " +
                    "WHERE o.is_deleted = 0 ORDER BY o.created_at DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                
                // Set thông tin khách hàng
                order.setCustomerUsername(rs.getString("username"));
                order.setCustomerName(rs.getString("full_name"));
                order.setCustomerEmail(rs.getString("email"));
                
                // Lấy danh sách sản phẩm trong đơn hàng
                order.setOrderItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }
        
        return orders;
    }
    
    /**
     * Tìm kiếm và lọc đơn hàng theo các tiêu chí
     * @param keyword Từ khóa tìm kiếm
     * @param status Trạng thái đơn hàng
     * @param userId ID người dùng (nếu là khách hàng)
     * @param role Vai trò người dùng (admin hoặc customer)
     * @return Danh sách đơn hàng phù hợp với điều kiện
     */
    public List<Order> searchOrders(String keyword, String status, int userId, String role) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT o.*, u.username, u.full_name, u.email FROM Orders o " +
            "LEFT JOIN Users u ON o.user_id = u.user_id " +
            "WHERE o.is_deleted = 0");
        
        // Điều kiện cho role
        if (!"admin".equals(role)) {
            sql.append(" AND o.user_id = ?");
        }
        
        // Điều kiện tìm kiếm theo keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (o.order_number LIKE ? OR u.full_name LIKE ? OR u.username LIKE ?)");
        }
        
        // Điều kiện lọc theo trạng thái
        if (status != null && !status.equals("all") && !status.trim().isEmpty()) {
            sql.append(" AND o.status = ?");
        }
        
        sql.append(" ORDER BY o.created_at DESC");
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            // Set tham số cho user_id nếu không phải admin
            if (!"admin".equals(role)) {
                ps.setInt(paramIndex++, userId);
            }
            
            // Set tham số cho keyword nếu có
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            // Set tham số cho status nếu có
            if (status != null && !status.equals("all") && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    
                    // Set thông tin khách hàng
                    order.setCustomerUsername(rs.getString("username"));
                    order.setCustomerName(rs.getString("full_name"));
                    order.setCustomerEmail(rs.getString("email"));
                    
                    // Lấy danh sách sản phẩm trong đơn hàng
                    order.setOrderItems(getOrderItems(order.getOrderId()));
                    
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm đơn hàng: " + e.getMessage(), e);
        }
        
        return orders;
    }
    
    /**
     * Lấy thông tin sản phẩm chi tiết cho các sản phẩm trong đơn hàng
     * @param orderId ID đơn hàng
     * @return Danh sách sản phẩm với thông tin chi tiết
     */
    public List<OrderItem> getOrderItemsWithDetails(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, pi.image_url " +
                     "FROM Order_items oi " +
                     "LEFT JOIN Products p ON oi.product_id = p.product_id " +
                     "LEFT JOIN Product_images pi ON oi.product_id = pi.product_id AND pi.is_main = 1 AND pi.is_deleted = 0 " +
                     "WHERE oi.order_id = ? AND oi.is_deleted = 0";
                    
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("order_item_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setSubtotal(rs.getDouble("subtotal"));
                    item.setIsDeleted(rs.getBoolean("is_deleted"));
                    
                    // Thêm URL hình ảnh sản phẩm
                    String imageUrl = rs.getString("image_url");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        item.setImageUrl(imageUrl);
                    }
                    
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage(), e);
        }
        return items;
    }
    
    /**
     * Đánh dấu đơn hàng đã hoàn tiền (cho đơn hàng VNPay đã hủy)
     * @param orderId ID của đơn hàng
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean markPaymentRefunded(int orderId) {
        try {
            // Thực hiện truy vấn trực tiếp không sử dụng transaction
            // Cập nhật trạng thái thanh toán trong bảng Orders - sử dụng 'paid' thay vì 'refunded'
            // vì bảng Orders chỉ chấp nhận 'pending', 'paid', 'failed'
            String updateOrderSql = "UPDATE Orders SET payment_status = 'paid' WHERE order_id = ?";
            try (PreparedStatement orderPs = conn.prepareStatement(updateOrderSql)) {
                orderPs.setInt(1, orderId);
                int orderRows = orderPs.executeUpdate();
                LOGGER.log(Level.INFO, "Cập nhật trạng thái thanh toán đơn hàng: " + orderRows + " hàng bị ảnh hưởng");
                
                if (orderRows > 0) {
                    // Nếu cập nhật Orders thành công, thử cập nhật Payments
                    try {
                        // Bảng Payments chấp nhận giá trị 'refunded'
                        String updatePaymentSql = "UPDATE Payments SET status = 'refunded' WHERE order_id = ?";
                        try (PreparedStatement paymentPs = conn.prepareStatement(updatePaymentSql)) {
                            paymentPs.setInt(1, orderId);
                            int paymentRows = paymentPs.executeUpdate();
                            LOGGER.log(Level.INFO, "Cập nhật trạng thái thanh toán: " + paymentRows + " hàng bị ảnh hưởng");
                        }
                    } catch (SQLException e) {
                        // Nếu cập nhật Payments thất bại, vẫn trả về true vì Orders đã được cập nhật
                        LOGGER.log(Level.WARNING, "Không thể cập nhật bảng Payments, nhưng Orders đã được cập nhật: " + e.getMessage());
                    }
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đánh dấu hoàn tiền: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Kiểm tra xem một đơn hàng đã được hoàn tiền trong bảng Payments chưa
     * @param orderId ID của đơn hàng
     * @return true nếu đã hoàn tiền, false nếu chưa hoàn tiền hoặc có lỗi
     */
    public boolean isPaymentRefunded(int orderId) {
        String sql = "SELECT status FROM Payments WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return "refunded".equals(status);
                }
                return false; // Không tìm thấy bản ghi payment
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra trạng thái hoàn tiền: " + e.getMessage(), e);
            return false;
        }
    }
} 