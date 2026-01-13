package dao.impl;

import dao.interfaces.WishlistDAO;
import dao.interfaces.WishlistDAO.WishlistStatistics;
import model.entity.Wishlist;
import utils.db.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WishlistDAOImpl implements WishlistDAO {
    private static final Logger LOGGER = Logger.getLogger(WishlistDAOImpl.class.getName());
    
    @Override
    public boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.severe("Error adding to wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.severe("Error removing from wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeWishlistItem(int wishlistId, int userId) {
        String sql = "DELETE FROM wishlist WHERE wishlist_id = ? AND user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, wishlistId);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.severe("Error removing wishlist item: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error checking wishlist: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public List<Wishlist> getWishlistByUser(int userId) {
        List<Wishlist> wishlist = new ArrayList<>();
        String sql = "SELECT * FROM v_wishlist_details WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Wishlist item = new Wishlist();
                    item.setWishlistId(rs.getInt("wishlist_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    // Product information from view
                    item.setProductName(rs.getString("product_name"));
                    item.setProductDescription(rs.getString("description"));
                    item.setProductPrice(rs.getDouble("price"));
                    
                    // Handle nullable columns safely
                    Double salePrice = rs.getDouble("sale_price");
                    if (!rs.wasNull()) {
                        item.setSalePrice(salePrice);
                    }
                    
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setProductStatus(rs.getString("product_status"));
                    item.setCategoryName(rs.getString("category_name"));
                    
                    Boolean featured = rs.getBoolean("featured");
                    if (!rs.wasNull()) {
                        item.setFeatured(featured);
                    }
                    
                    wishlist.add(item);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error getting wishlist: " + e.getMessage());
        }
        
        return wishlist;
    }
    
    @Override
    public int getWishlistCount(int userId) {
        String sql = "SELECT COUNT(*) FROM wishlist w " +
                    "JOIN Products p ON w.product_id = p.product_id " +
                    "WHERE w.user_id = ? AND p.status = 'active' AND p.is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error getting wishlist count: " + e.getMessage());
        }
        
        return 0;
    }
    
    @Override
    public boolean clearWishlist(int userId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected >= 0; // Success even if no rows deleted
            
        } catch (SQLException e) {
            LOGGER.severe("Error clearing wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<WishlistStatistics> getMostWishedProducts(int limit) {
        List<WishlistStatistics> stats = new ArrayList<>();
        String sql = "SELECT TOP (?) p.product_id, p.name as product_name, p.price, " +
                    "COUNT(w.wishlist_id) as wishlist_count " +
                    "FROM Products p " +
                    "JOIN wishlist w ON p.product_id = w.product_id " +
                    "WHERE p.status = 'active' AND p.is_deleted = 0 " +
                    "GROUP BY p.product_id, p.name, p.price " +
                    "ORDER BY wishlist_count DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WishlistStatistics stat = new WishlistStatistics(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "", // image_url - có thể thêm sau
                        rs.getDouble("price"),
                        rs.getInt("wishlist_count")
                    );
                    stats.add(stat);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error getting most wished products: " + e.getMessage());
        }
        
        return stats;
    }
}
