// 1. Enhanced CategoryDAO with full CRUD operations
package dao.impl;

import model.entity.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utils.db.DBContext;

public class CategoryDAO {
    
    public CategoryDAO() {
        // Connection sẽ được tạo trong mỗi method để tránh timeout
    }public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories WHERE is_deleted = 0 ORDER BY display_order ASC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Category category = mapResultSetToCategory(rs);
                categories.add(category);
            }        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public Category getCategoryById(int categoryId) {
        Category category = null;
        String query = "SELECT * FROM Categories WHERE category_id = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }    public boolean addCategory(Category category) {
        String query = "INSERT INTO Categories (parent_id, name, description, status, display_order, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, GETDATE(), GETDATE(), 0)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setObject(1, category.getParentId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.setString(4, category.getStatus());
            ps.setInt(5, category.getDisplayOrder());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }public boolean updateCategory(Category category) {        String query = "UPDATE Categories SET parent_id = ?, name = ?, description = ?, status = ?, display_order = ?, updated_at = GETDATE() WHERE category_id = ? AND is_deleted = 0";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setObject(1, category.getParentId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.setString(4, category.getStatus());
            ps.setInt(5, category.getDisplayOrder());
            ps.setInt(6, category.getCategoryId());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    public boolean deleteCategory(int categoryId) {
        String query = "UPDATE Categories SET is_deleted = 1, updated_at = GETDATE() WHERE category_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, categoryId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    public List<Category> getParentCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories WHERE parent_id IS NULL AND is_deleted = 0 ORDER BY display_order ASC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Category category = mapResultSetToCategory(rs);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setStatus(rs.getString("status"));
        category.setDisplayOrder(rs.getInt("display_order"));
        category.setCreatedAt(rs.getString("created_at"));
        category.setUpdatedAt(rs.getString("updated_at"));
        category.setIsDeleted(rs.getInt("is_deleted"));
        return category;
    }    // Test method to check database connection
    public boolean testConnection() {
        try (Connection conn = DBContext.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
