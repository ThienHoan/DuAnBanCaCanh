package dao.impl;

import model.entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import util.db.Db;

public class ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                      "FROM Products";
        
        try {
            conn = new Db().getConnection(); // Open connection using DBContext
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id") != 0 ? rs.getInt("category_id") : 0,
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("short_description"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO,
                    rs.getInt("quantity"),
                    rs.getString("sku"),
                    rs.getString("status"),
                    rs.getInt("featured"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"),
                    rs.getInt("is_deleted")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return products;
    }
    
    
    public boolean toggleIsDeleted(int productId) {
        String query = "UPDATE Products SET is_deleted = CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END, " +
                      "updated_at = GETDATE() WHERE product_id = ?";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean createProduct(Product product) {
        String query = "INSERT INTO Products (category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, product.getCategoryId(), java.sql.Types.INTEGER);
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getShortDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getSalePrice());
            ps.setInt(7, product.getQuantity());
            ps.setString(8, product.getSku());
            ps.setString(9, product.getStatus());
            ps.setInt(10, product.getFeatured() != null ? product.getFeatured() : 0);
            ps.setString(11, product.getCreatedAt());
            ps.setString(12, product.getUpdatedAt());
            ps.setInt(13, product.getIsDeleted() != null ? product.getIsDeleted() : 0);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean updateProduct(Product product) {
        String query = "UPDATE Products SET category_id = ?, name = ?, description = ?, short_description = ?, " +
                      "price = ?, sale_price = ?, quantity = ?, sku = ?, status = ?, featured = ?, " +
                      "is_deleted = ?, updated_at = GETDATE() WHERE product_id = ?";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, product.getCategoryId(), java.sql.Types.INTEGER);
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getShortDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getSalePrice());
            ps.setInt(7, product.getQuantity());
            ps.setString(8, product.getSku());
            ps.setString(9, product.getStatus());
            ps.setInt(10, product.getFeatured());
            ps.setInt(11, product.getIsDeleted() );
            ps.setInt(12, product.getProductId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int getLastInsertProductId() {
    String query = "SELECT IDENT_CURRENT('Products') AS last_id"; // SQL Server
    // Nếu dùng MySQL: String query = "SELECT LAST_INSERT_ID() AS last_id";
    int id = -1;
    try {
        conn = new Db().getConnection();
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("last_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return id;
}
    
    public Product getProductById(int productId) {
        String query = "SELECT * FROM Products WHERE product_id = ?";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("short_description"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("sale_price"),
                    rs.getInt("quantity"),
                    rs.getString("sku"),
                    rs.getString("status"),
                    rs.getInt("featured"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"),
                    rs.getInt("is_deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}