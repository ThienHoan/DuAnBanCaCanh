package dao.impl;

import model.entity.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import util.db.Db;

public class CategoryDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories ORDER BY display_order ASC";

        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setImage(rs.getString("image"));
                category.setStatus(rs.getString("status"));
                category.setDisplayOrder(rs.getInt("display_order"));

                category.setCreatedAt(rs.getString("created_at"));
                category.setUpdatedAt(rs.getString("updated_at"));

                category.setIsDeleted(rs.getInt("is_deleted"));
                
                categories.add(category);
            }
        } catch (SQLException e) {

        }
        return categories;
    }
    
    public Category getCategoryById(int categoryId) {
        Category category = null;
        String query = "SELECT * FROM Categories WHERE category_id = ? AND is_deleted = 0";

        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();

            if (rs.next()) {
                category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setImage(rs.getString("image"));
                category.setStatus(rs.getString("status"));
                category.setDisplayOrder(rs.getInt("display_order"));

                category.setCreatedAt(rs.getString("created_at"));
                category.setUpdatedAt(rs.getString("updated_at"));

                category.setIsDeleted(rs.getInt("is_deleted"));
            }
        } catch (SQLException e) {
            // Log the error (consistent with getAllCategories, which has an empty catch block)
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return category;
    }
}