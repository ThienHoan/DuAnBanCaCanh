package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.ProductImage;
import utils.db.DBContext;

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
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
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

    // Lấy tất cả hình ảnh theo product_id (sắp xếp theo display_order)
    public List<ProductImage> getImagesByProductId(int productId) {
        List<ProductImage> images = new ArrayList<>();
        String query = "SELECT image_id, product_id, image_url, is_main, display_order, is_deleted " +
               "FROM Product_images WHERE product_id = ? AND is_deleted = 0 ORDER BY display_order";


        try {
            conn = new DBContext().getConnection();
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
    }    // Lấy hình ảnh chính theo product_id
    public ProductImage getMainImageByProductId(int productId) {
        String query = "SELECT image_id, product_id, image_url, is_main, display_order, is_deleted " +
                       "FROM Product_images WHERE product_id = ? AND is_main = 1 AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

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
        }

        return null;
    }

    // Lấy URL của hình ảnh chính theo product_id
    public String getMainImageByProductId1(int productId) {
        String query = "SELECT image_url FROM Product_images WHERE product_id = ? AND is_main = 1 AND is_deleted = 0";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("image_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return null;
    }

    // Thêm hình ảnh (ảnh phụ hoặc ảnh chính mới)
    public boolean insertProductImage(ProductImage image) {
        if (image.getIsMain() != 1) {
            image.setDisplayOrder(getNextDisplayOrder(image.getProductId()));
        }

        String query = "INSERT INTO Product_images (product_id, image_url, is_main, display_order, is_deleted) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = new DBContext().getConnection();
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
            conn = new DBContext().getConnection();
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

    // 🔄 Thay ảnh chính nếu đã tồn tại
    public boolean replaceMainImage(ProductImage newImage) {
        ProductImage existing = getMainImageByProductId(newImage.getProductId());

        newImage.setMain(1);
        newImage.setDisplayOrder(1);
        newImage.setDeleted(0);

        if (existing != null) {
            newImage.setImageId(existing.getImageId());
            return updateProductImage(newImage);
        } else {
            return insertProductImage(newImage);
        }
    }

    // 👉 Lấy display_order tiếp theo cho ảnh phụ
   public int getNextDisplayOrder(int productId) {
    String query = "SELECT COALESCE(MAX(display_order), 0) + 1 FROM Product_images WHERE product_id = ? AND is_deleted = 0";
    int nextOrder = 1;
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, productId);
        rs = ps.executeQuery();
        if (rs.next()) {
            nextOrder = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return nextOrder;
}

    
    
    public boolean softDeleteImage(int imageId) {
    String query = "UPDATE Product_images SET is_deleted = 1 WHERE image_id = ?";
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, imageId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return false;
}

    
    public boolean updateDisplayOrderAndMain(ProductImage image) {
    String query = "UPDATE Product_images SET display_order = ?, is_main = ? WHERE image_id = ?";
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, image.getDisplayOrder());
        ps.setInt(2, image.getIsMain());
        ps.setInt(3, image.getImageId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return false;
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
