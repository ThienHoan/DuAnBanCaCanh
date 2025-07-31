package dao.impl;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;
import utils.db.DBContext;

public class CartDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Get or create cart for user
    public Cart getOrCreateCartByUserId(int userId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "SELECT * FROM Carts WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String createdAt = rs.getTimestamp("created_at").toLocalDateTime().format(FORMATTER);
                    String updatedAt = rs.getTimestamp("updated_at").toLocalDateTime().format(FORMATTER);
                    return new Cart(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            createdAt,
                            updatedAt
                    );
                } else {
                    return createCart(userId);
                }
            }
        }
    }

    // Create new cart
    public Cart createCart(int userId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "INSERT INTO Carts (user_id, created_at, updated_at) VALUES (?, GETDATE(), GETDATE())";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int cartId = generatedKeys.getInt(1);
                    return getCartById(cartId);
                }
                throw new SQLException("Creating cart failed, no ID obtained.");
            }
        }
    }

    public Cart getCartById(int cartId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "SELECT * FROM Carts WHERE cart_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String createdAt = rs.getTimestamp("created_at").toLocalDateTime().format(FORMATTER);
                    String updatedAt = rs.getTimestamp("updated_at").toLocalDateTime().format(FORMATTER);
                    return new Cart(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            createdAt,
                            updatedAt
                    );
                }
            }
            return null;
        }
    }
        
    
    
    // Thêm method này vào class CartDAO để test insert cứng
public boolean testInsertHardcode() throws SQLException {
    try (Connection connection = DBContext.getConnection()) {
        String sql = "INSERT INTO Cart_items (cart_id, product_id, quantity, added_at) VALUES (1, 9, 7, GETDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int result = stmt.executeUpdate();
            System.out.println("Insert result: " + result);
            return result > 0;
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
        throw e;
    }
}
    
    
    // Add item to cart
    public boolean addItemToCart(int cartId, int productId, int quantity) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String checkSql = "SELECT cart_item_id, quantity FROM Cart_items WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, cartId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int existingQuantity = rs.getInt("quantity");
                    int cartItemId = rs.getInt("cart_item_id");
                    return updateCartItemQuantity(cartItemId, existingQuantity + quantity);
                } else {
                    String insertSql = "INSERT INTO Cart_items (cart_id, product_id, quantity, added_at) VALUES (?, ?, ?, GETDATE())";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, cartId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, quantity);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        }
    }

    // Get cart items with product details
    public List<CartItem> getCartItemsByCartId(int cartId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "SELECT ci.*, p.name as product_name, p.price as product_price " +
                    "FROM Cart_items ci " +
                    "JOIN Products p ON ci.product_id = p.product_id " +
                    "WHERE ci.cart_id = ?";

            List<CartItem> items = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String addedAt = rs.getTimestamp("added_at").toLocalDateTime().format(FORMATTER);
                    CartItem item = new CartItem(
                            rs.getInt("cart_item_id"),
                            rs.getInt("cart_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            addedAt
                    );
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getDouble("product_price"));
                    items.add(item);
                }
            }
            return items;
        }
    }

    // Update cart item quantity
        // Update cart item quantity
    public boolean updateCartItemQuantity(int cartItemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return removeCartItem(cartItemId);
        }
        try (Connection connection = DBContext.getConnection()) {
            String sql = "UPDATE Cart_items SET quantity = ? WHERE cart_item_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setInt(2, cartItemId);
                return stmt.executeUpdate() > 0;
            }
        }
    }

    
    public int getCurrentQuantityInCart(int userId, int productId) throws SQLException {
    String query = "SELECT ci.quantity FROM Cart_items ci " +
                   "JOIN Carts c ON ci.cart_id = c.cart_id " +
                   "WHERE c.user_id = ? AND ci.product_id = ?";
    try (Connection connection = DBContext.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setInt(1, userId);
        ps.setInt(2, productId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("quantity");
            }
            return 0;
        }
    }
}
    
    
    
    // Remove cart item
    public boolean removeCartItem(int cartItemId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "DELETE FROM Cart_items WHERE cart_item_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartItemId);
                return stmt.executeUpdate() > 0;
            }
        }
    }

    // Clear all items from cart
    public boolean clearCart(int cartId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "DELETE FROM Cart_items WHERE cart_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartId);
                return stmt.executeUpdate() >= 0;
            }
        }
    }

    /**
     * Xóa tất cả các mục trong giỏ hàng
     * 
     * @param cartId ID của giỏ hàng
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean clearCartItems(int cartId) {
        String sql = "DELETE FROM Cart_items WHERE cart_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cartId);
            
            int rowsAffected = ps.executeUpdate();
            
            System.out.println("DEBUG - Cleared " + rowsAffected + " items from cart " + cartId);
            
            return rowsAffected >= 0; // Trả về true ngay cả khi không có mục nào bị xóa
            
        } catch (SQLException e) {
            System.out.println("DEBUG - Error clearing cart items: " + e.getMessage());
            return false;
        }
    }

    // Get cart item count
    public int getCartItemCount(int cartId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "SELECT COALESCE(SUM(quantity), 0) as total FROM Cart_items WHERE cart_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
            return 0;
        }
    }

    // Get cart total price
    public double getCartTotal(int cartId) throws SQLException {
        try (Connection connection = DBContext.getConnection()) {
            String sql = "SELECT COALESCE(SUM(ci.quantity * p.price), 0) as total " +
                    "FROM Cart_items ci " +
                    "JOIN Products p ON ci.product_id = p.product_id " +
                    "WHERE ci.cart_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, cartId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
            return 0.0;
        }
    }

    // Add item to cart using userId - THAY ĐỔI: Thêm phương thức mới để hỗ trợ thêm vào giỏ hàng bằng userId
    public boolean addItemToCartByUserId(int userId, int productId, int quantity) throws SQLException {
        Cart cart = getOrCreateCartByUserId(userId);
        return addItemToCart(cart.getCartId(), productId, quantity);
    }
}
