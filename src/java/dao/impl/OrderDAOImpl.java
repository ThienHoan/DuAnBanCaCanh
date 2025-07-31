package dao.impl;

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
        System.out.println("DEBUG - getOrderByOrderNumber - searching for: " + orderNumber);
        String sql = "SELECT * FROM Orders WHERE order_number = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setOrderItems(getOrderItems(order.getOrderId()));
                    
                    // Lấy cartId từ database
                    // Commented out code that was previously in a try-catch block:
                    // order.setCartId(rs.getInt("cart_id"));
                    
                    System.out.println("DEBUG - getOrderByOrderNumber - found order: " + order.getOrderId() + ", status: " + order.getStatus());
                    return order;
                }
            }
            System.out.println("DEBUG - getOrderByOrderNumber - no order found for: " + orderNumber);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin đơn hàng: " + e.getMessage(), e);
            System.out.println("DEBUG - getOrderByOrderNumber - SQLException: " + e.getMessage());
        }
        return null;
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
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, orderNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Order order = mapResultSetToOrder(rs);
                        order.setOrderItems(getOrderItems(order.getOrderId()));
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
            System.out.println("DEBUG - updateOrderStatus - orderId: " + orderId + ", status: " + status);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            int result = ps.executeUpdate();
            System.out.println("DEBUG - updateOrderStatus - rows affected: " + result);
            
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage(), e);
            System.out.println("DEBUG - updateOrderStatus - SQLException: " + e.getMessage());
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
                    // === BẮT ĐẦU: Khôi phục lại số lượng sản phẩm khi hủy đơn ===
                    List<OrderItem> orderItems = getOrderItems(orderId);
                    if (orderItems != null && !orderItems.isEmpty()) {
                        ProductDAO productDAO = new ProductDAO();
                        for (OrderItem item : orderItems) {
                            int productId = item.getProductId();
                            int quantity = item.getQuantity();
                            int currentQuantity = productDAO.getProductQuantity(productId);
                            int newQuantity = currentQuantity + quantity;
                            try (Connection conn2 = utils.db.DBContext.getConnection();
                                 PreparedStatement ps2 = conn2.prepareStatement("UPDATE Products SET quantity = ? WHERE product_id = ?")) {
                                ps2.setInt(1, newQuantity);
                                ps2.setInt(2, productId);
                                ps2.executeUpdate();
                            } catch (Exception ex) {
                                LOGGER.log(Level.WARNING, "Không thể khôi phục tồn kho cho sản phẩm " + productId + ": " + ex.getMessage(), ex);
                            }
                        }
                    }
                    // === KẾT THÚC: Khôi phục lại số lượng sản phẩm khi hủy đơn ===
                    
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

    /**
     * Cập nhật ghi chú cho đơn hàng
     * 
     * @param orderId ID của đơn hàng
     * @param notes Ghi chú mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateOrderNotes(int orderId, String notes) {
        String sql = "UPDATE Orders SET notes = ?, updated_at = GETDATE() WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notes);
            ps.setInt(2, orderId);
            int result = ps.executeUpdate();
            
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật ghi chú đơn hàng: " + e.getMessage(), e);
            return false;
        }
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
        
        // Tính toán các giá trị
        BigDecimal shippingFee = new BigDecimal("20000"); // 20,000 VNĐ
        BigDecimal taxRate = new BigDecimal("0.05"); // 5%
        BigDecimal tax = totalAmount.multiply(taxRate);
        BigDecimal finalAmount = totalAmount.add(shippingFee).add(tax);
        
        String sql = "INSERT INTO Orders (user_id, order_number, shipping_address_id, status, payment_method, payment_status, tax, shipping_fee, discount_amount, notes, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, orderNumber);
            ps.setInt(3, addressId);
            ps.setString(4, "pending");
            ps.setString(5, "bank_transfer");
            ps.setString(6, "pending");
            ps.setBigDecimal(7, tax);
            ps.setBigDecimal(8, shippingFee);
            ps.setBigDecimal(9, BigDecimal.ZERO); // Không có giảm giá
            ps.setString(10, notes);
            
            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    
                    // Tạo chi tiết đơn hàng
                    try { // Added try-catch here
                        createOrderItems(orderId, cartId);
                    } catch (Exception e) {
                        System.out.println("DEBUG - Error creating order items but continuing: " + e.getMessage());
                        e.printStackTrace();
                    }
                    
                    System.out.println("DEBUG - Temporary order created with ID: " + orderId);
                    return orderId;
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
            // Lấy danh sách sản phẩm từ giỏ hàng - sử dụng truy vấn trực tiếp với tên bảng chính xác
            String selectCartItemsSql = "SELECT ci.*, p.name as product_name, p.price as product_price " +
                    "FROM Cart_items ci " + // Tên bảng chính xác là Cart_items, không phải CartItems
                    "JOIN Products p ON ci.product_id = p.product_id " +
                    "WHERE ci.cart_id = ?";
            
            String insertOrderItemSql = "INSERT INTO Order_items (order_id, product_id, product_name, quantity, unit_price, subtotal, is_deleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
            
            int count = 0;
            try (PreparedStatement psSelect = conn.prepareStatement(selectCartItemsSql)) {
                psSelect.setInt(1, cartId);
                
                try (ResultSet rs = psSelect.executeQuery()) {
                    PreparedStatement psInsert = conn.prepareStatement(insertOrderItemSql);
                    
                    while (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");
                        BigDecimal price = rs.getBigDecimal("product_price");
                        BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
                        String productName = rs.getString("product_name");
                        
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
                    
                    if (count > 0) {
                        int[] results = psInsert.executeBatch();
                        System.out.println("DEBUG - Created " + results.length + " order items");
                        return true;
                    }
                    
                    psInsert.close();
                }
            }
            
            return count > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo chi tiết đơn hàng: " + e.getMessage(), e);
            System.out.println("DEBUG - Error creating order items: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy tên sản phẩm từ ID sản phẩm
     * 
     * @param productId ID sản phẩm
     * @return Tên sản phẩm
     */
    private String getProductName(int productId) {
        try {
            String sql = "SELECT name FROM Products WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("DEBUG - Error getting product name: " + e.getMessage());
        }
        return "Sản phẩm #" + productId;
    }
} 