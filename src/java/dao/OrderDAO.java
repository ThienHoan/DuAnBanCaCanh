/* OrderDAO.java - Unchanged, included for completeness */
package dao;

import model.Order;
import model.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cart;
import model.CartItem;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private Connection conn;

    public OrderDAO() {
        conn = DBConnection.getConnection();
        if (conn == null) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu");
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu");
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ? AND is_deleted = 0 ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setDiscountAmount(rs.getDouble("discount_amount"));
                    order.setShippingFee(rs.getDouble("shipping_fee"));
                    order.setTax(rs.getDouble("tax"));
                    order.setFinalAmount(rs.getDouble("final_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    order.setShippingAddressId(rs.getInt("shipping_address_id"));
                    order.setBillingAddressId(rs.getInt("billing_address_id"));
                    order.setNotes(rs.getString("notes"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setDeleted(rs.getBoolean("is_deleted"));
                    order.setItems(getOrderItems(order.getOrderId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE is_deleted = 0 ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setDiscountAmount(rs.getDouble("discount_amount"));
                    order.setShippingFee(rs.getDouble("shipping_fee"));
                    order.setTax(rs.getDouble("tax"));
                    order.setFinalAmount(rs.getDouble("final_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    order.setShippingAddressId(rs.getInt("shipping_address_id"));
                    order.setBillingAddressId(rs.getInt("billing_address_id"));
                    order.setNotes(rs.getString("notes"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setDeleted(rs.getBoolean("is_deleted"));
                    order.setItems(getOrderItems(order.getOrderId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
        }
        return orders;
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
                    item.setDeleted(rs.getBoolean("is_deleted"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage(), e);
        }
        return items;
    }

    public boolean confirmShipped(int orderId) {
        String sql = "UPDATE Orders SET status = 'shipping', updated_at = GETDATE() WHERE order_id = ? AND status IN ('pending', 'confirmed', 'processing')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xác nhận giao hàng: " + e.getMessage(), e);
            throw new RuntimeException("Lỗi khi xác nhận giao hàng: " + e.getMessage(), e);
        }
    }

    public boolean confirmReceived(int orderId) {
        String sql = "UPDATE Orders SET status = 'delivered', updated_at = GETDATE() WHERE order_id = ? AND status = 'shipping'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xác nhận nhận hàng: " + e.getMessage(), e);
            throw new RuntimeException("Lỗi khi xác nhận nhận hàng: " + e.getMessage(), e);
        }
    }

    public int createOrder(int userId, Cart cart, String orderNumber, int addressId, String paymentMethod, String notes, double discountAmount) {
        String orderSql = "INSERT INTO Orders (user_id, order_number, status, total_amount, discount_amount, shipping_fee, tax, final_amount, payment_method, payment_status, shipping_address_id, billing_address_id, notes, created_at, updated_at) VALUES (?, ?, 'pending', ?, ?, 20000, 0, ?, ?, 'pending', ?, ?, ?, GETDATE(), GETDATE())";
        String orderItemSql = "INSERT INTO Order_items (order_id, product_id, product_name, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String paymentSql = "INSERT INTO Payments (order_id, payment_method, amount, status, payment_date) VALUES (?, ?, ?, ?, GETDATE())";
        String inventorySql = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        String inventoryLogSql = "INSERT INTO Inventory_logs (product_id, quantity_before, quantity_after, change_type, reason, reference_id, reference_type, created_at) VALUES (?, ?, ?, 'decrease', ?, ?, 'order', GETDATE())";

        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            double price = item.getProduct().getSalePrice() != null ? item.getProduct().getSalePrice() : item.getProduct().getPrice();
            totalAmount += price * item.getQuantity();
        }
        double finalAmount = totalAmount - discountAmount + 20000;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement orderPs = conn.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                orderPs.setInt(1, userId);
                orderPs.setString(2, orderNumber);
                orderPs.setDouble(3, totalAmount);
                orderPs.setDouble(4, discountAmount);
                orderPs.setDouble(5, finalAmount);
                orderPs.setString(6, paymentMethod);
                orderPs.setInt(7, addressId);
                orderPs.setInt(8, addressId);
                orderPs.setString(9, notes.isEmpty() ? null : notes);
                orderPs.executeUpdate();

                try (ResultSet generatedKeys = orderPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        try (PreparedStatement itemPs = conn.prepareStatement(orderItemSql)) {
                            for (CartItem item : cart.getItems()) {
                                double price = item.getProduct().getSalePrice() != null ? item.getProduct().getSalePrice() : item.getProduct().getPrice();
                                itemPs.setInt(1, orderId);
                                itemPs.setInt(2, item.getProductId());
                                itemPs.setString(3, item.getProduct().getName());
                                itemPs.setInt(4, item.getQuantity());
                                itemPs.setDouble(5, price);
                                itemPs.setDouble(6, price * item.getQuantity());
                                itemPs.executeUpdate();
                            }
                        }

                        try (PreparedStatement paymentPs = conn.prepareStatement(paymentSql)) {
                            paymentPs.setInt(1, orderId);
                            paymentPs.setString(2, paymentMethod);
                            paymentPs.setDouble(3, finalAmount);
                            paymentPs.setString(4, "cod".equals(paymentMethod) ? "pending" : "pending");
                            paymentPs.executeUpdate();
                        }

                        try (PreparedStatement invPs = conn.prepareStatement(inventorySql); PreparedStatement logPs = conn.prepareStatement(inventoryLogSql)) {
                            for (CartItem item : cart.getItems()) {
                                String checkSql = "SELECT quantity FROM Products WHERE product_id = ?";
                                int currentQuantity = 0;
                                try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                                    checkPs.setInt(1, item.getProductId());
                                    ResultSet rs = checkPs.executeQuery();
                                    if (rs.next()) {
                                        currentQuantity = rs.getInt("quantity");
                                        if (currentQuantity < item.getQuantity()) {
                                            throw new SQLException("Không đủ hàng cho sản phẩm: " + item.getProduct().getName());
                                        }
                                    }
                                }

                                invPs.setInt(1, item.getQuantity());
                                invPs.setInt(2, item.getProductId());
                                invPs.setInt(3, item.getQuantity());
                                invPs.executeUpdate();

                                logPs.setInt(1, item.getProductId());
                                logPs.setInt(2, currentQuantity);
                                logPs.setInt(3, currentQuantity - item.getQuantity());
                                logPs.setString(4, "Bán hàng (đơn " + orderNumber + ")");
                                logPs.setInt(5, orderId);
                                logPs.executeUpdate();
                            }
                        }

                        conn.commit();
                        return orderId;
                    }
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed: " + rollbackEx.getMessage(), rollbackEx);
            }
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo đơn hàng", e);
            throw new RuntimeException("Lỗi khi tạo đơn hàng: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit: " + e.getMessage(), e);
            }
        }
        return 0;
    }

    public boolean cancelOrder(int orderId) {
        String updateOrderSql = "UPDATE Orders SET status = 'cancelled', updated_at = GETDATE() WHERE order_id = ? AND status IN ('pending', 'confirmed')";
        String checkItemsSql = "SELECT product_id, quantity FROM Order_items WHERE order_id = ? AND is_deleted = 0";
        String updateInventorySql = "UPDATE Products SET quantity = quantity + ? WHERE product_id = ? AND is_deleted = 0";
        String logInventorySql = "INSERT INTO Inventory_logs (product_id, quantity_before, quantity_after, change_type, reason, reference_id, reference_type, created_at) VALUES (?, ?, ?, 'increase', ?, ?, 'order', GETDATE())";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(updateOrderSql)) {
                ps.setInt(1, orderId);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement checkPs = conn.prepareStatement(checkItemsSql)) {
                checkPs.setInt(1, orderId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    try (PreparedStatement invPs = conn.prepareStatement(updateInventorySql);
                         PreparedStatement logPs = conn.prepareStatement(logInventorySql)) {
                        while (rs.next()) {
                            int productId = rs.getInt("product_id");
                            int quantity = rs.getInt("quantity");

                            String checkStockSql = "SELECT quantity FROM Products WHERE product_id = ? AND is_deleted = 0";
                            int currentQuantity = 0;
                            try (PreparedStatement stockPs = conn.prepareStatement(checkStockSql)) {
                                stockPs.setInt(1, productId);
                                try (ResultSet stockRs = stockPs.executeQuery()) {
                                    if (stockRs.next()) {
                                        currentQuantity = stockRs.getInt("quantity");
                                    }
                                }
                            }

                            invPs.setInt(1, quantity);
                            invPs.setInt(2, productId);
                            invPs.executeUpdate();

                            logPs.setInt(1, productId);
                            logPs.setInt(2, currentQuantity);
                            logPs.setInt(3, currentQuantity + quantity);
                            logPs.setString(4, "Hủy đơn hàng (đơn " + orderId + ")");
                            logPs.setInt(5, orderId);
                            logPs.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed: " + rollbackEx.getMessage(), rollbackEx);
            }
            LOGGER.log(Level.SEVERE, "Lỗi khi hủy đơn hàng: " + e.getMessage(), e);
            throw new RuntimeException("Lỗi khi hủy đơn hàng: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit: " + e.getMessage(), e);
            }
        }
    }

    public Map<String, String> getOrderCheckoutInfo(String orderNumber) {
        String sql = "SELECT shipping_address_id, payment_method, notes FROM Orders WHERE order_number = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> info = new HashMap<>();
                    info.put("addressId", String.valueOf(rs.getInt("shipping_address_id")));
                    info.put("paymentMethod", rs.getString("payment_method"));
                    info.put("notes", rs.getString("notes") != null ? rs.getString("notes") : "");
                    return info;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi khi lấy thông tin đơn hàng: " + e.getMessage(), e);
        }
        return null;
    }
    public boolean confirmOrder(int orderId) {
    String sql = "UPDATE Orders SET status = 'confirmed', updated_at = GETDATE() WHERE order_id = ? AND status = 'pending'";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, orderId);
        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Lỗi khi xác nhận đơn hàng: " + e.getMessage(), e);
        throw new RuntimeException("Lỗi khi xác nhận đơn hàng: " + e.getMessage(), e);
    }
    }
    public List<Order> searchOrders(String keyword, String status, int userId, String role) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Orders WHERE is_deleted = 0");
        
        // Điều kiện cho role
        if (!"admin".equals(role)) {
            sql.append(" AND user_id = ?");
        }
        
        // Điều kiện tìm kiếm theo keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND order_number LIKE ?");
        }
        
        // Điều kiện lọc theo trạng thái
        if (status != null && !status.equals("all")) {
            sql.append(" AND status = ?");
        }
        
        sql.append(" ORDER BY created_at DESC");
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            // Set tham số cho user_id nếu không phải admin
            if (!"admin".equals(role)) {
                ps.setInt(paramIndex++, userId);
            }
            
            // Set tham số cho keyword nếu có
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            
            // Set tham số cho status nếu có
            if (status != null && !status.equals("all")) {
                ps.setString(paramIndex++, status);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setOrderNumber(rs.getString("order_number"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setDiscountAmount(rs.getDouble("discount_amount"));
                    order.setShippingFee(rs.getDouble("shipping_fee"));
                    order.setTax(rs.getDouble("tax"));
                    order.setFinalAmount(rs.getDouble("final_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                    order.setShippingAddressId(rs.getInt("shipping_address_id"));
                    order.setBillingAddressId(rs.getInt("billing_address_id"));
                    order.setNotes(rs.getString("notes"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
                    order.setUpdatedAt(rs.getTimestamp("updated_at"));
                    order.setDeleted(rs.getBoolean("is_deleted"));
                    order.setItems(getOrderItems(order.getOrderId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm đơn hàng: " + e.getMessage(), e);
        }
        return orders;
    }
}