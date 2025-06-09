package dao.impl;

import model.entity.ProductImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.db.Db;

public class ProductImageDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy tất cả hình ảnh
    public List<ProductImage> getAllProductImages() {
        List<ProductImage> images = new ArrayList<>();
        String query = "SELECT image_id, product_id, image_url, is_main, display_order, is_deleted " +
                      "FROM Product_images ORDER BY product_id, display_order";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ProductImage image = new ProductImage(
                    rs.getInt("image_id"),
                    rs.getInt("product_id"),
                    rs.getString("image_url"),
                    rs.getInt("is_main"),
                    rs.getInt("display_order"),
                    rs.getInt("is_deleted") != 0 ? rs.getInt("is_deleted") : 0 != 0 ? rs.getInt("is_deleted") : 0 != 0 ? rs.getInt("is_deleted") : 0
                );
                images.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        
        return images;
    }

    // Lấy tất cả hình ảnh theo product_id (sắp xếp theo display_order)
    public List<ProductImage> getImagesByProductId(int productId) {
        List<ProductImage> images = new ArrayList<>();
        String query = "SELECT image_id, product_id, image_url, is_main, display_order, is_deleted " +
                      "FROM Product_images WHERE product_id = ? ORDER BY display_order";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ProductImage image = new ProductImage(
                    rs.getInt("image_id"),
                    rs.getInt("product_id"),
                    rs.getString("image_url"),
                    rs.getInt("is_main"),
                    rs.getInt("display_order"),
                    rs.getInt("is_deleted")
                );
                images.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        
        return images;
    }

    // Lấy hình ảnh chính theo product_id
    public ProductImage getMainImageByProductId(int productId) {
        String query = "SELECT image_id, product_id, image_url, is_main, display_order, is_deleted " +
                      "FROM Product_images WHERE product_id = ? AND is_main = 1";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new ProductImage(
                    rs.getInt("image_id"),
                    rs.getInt("product_id"),
                    rs.getString("image_url"),
                    rs.getInt("is_main"),
                    rs.getInt("display_order"),
                    rs.getInt("is_deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        
        return null;
    }

    // Thêm mới hình ảnh
    public boolean insertProductImage(ProductImage image) {
        String query = "INSERT INTO Product_images (product_id, image_url, is_main, display_order, is_deleted) " +
                      "VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, image.getProductId());
            ps.setString(2, image.getImageUrl());
            ps.setInt(3, image.getIsMain());
            ps.setInt(4, image.getDisplayOrder());
            ps.setInt(5, image.getIsDeleted() != null ? image.getIsDeleted() : 0);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Cập nhật hình ảnh
    public boolean updateProductImage(ProductImage image) {
        String query = "UPDATE Product_images SET product_id = ?, image_url = ?, is_main = ?, " +
                      "display_order = ?, is_deleted = ? WHERE image_id = ?";
        
        try {
            conn = new Db().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, image.getProductId());
            ps.setString(2, image.getImageUrl());
            ps.setInt(3, image.getIsMain());
            ps.setInt(4, image.getDisplayOrder());
            ps.setInt(5, image.getIsDeleted() != null ? image.getIsDeleted() : 0);
            ps.setInt(6, image.getImageId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Đóng tài nguyên
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}