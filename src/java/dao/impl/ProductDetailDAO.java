package dao.impl;

import model.entity.ProductDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.db.Db;

public class ProductDetailDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Get all product details
    public List<ProductDetail> getAllProductDetails() {
        List<ProductDetail> productDetails = new ArrayList<>();
        String query = "SELECT * FROM Product_details WHERE is_deleted = 0 ORDER BY product_detail_id ASC";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProductDetailId(rs.getInt("product_detail_id"));
                productDetail.setProductId(rs.getInt("product_id"));
                productDetail.setScientificName(rs.getString("scientific_name"));
                productDetail.setCommonName(rs.getString("common_name"));
                productDetail.setOrigin(rs.getString("origin"));
                productDetail.setSize(rs.getString("size"));
                productDetail.setLifespan(rs.getString("lifespan"));
                productDetail.setWaterType(rs.getString("water_type"));
                productDetail.setWaterTemperature(rs.getString("water_temperature"));
                productDetail.setWaterPh(rs.getString("water_ph"));
                productDetail.setDiet(rs.getString("diet"));
                productDetail.setBreedingDifficulty(rs.getString("breeding_difficulty"));
                productDetail.setCareLevel(rs.getString("care_level"));
                productDetail.setCompatibility(rs.getString("compatibility"));
                productDetail.setIsDeleted(rs.getInt("is_deleted"));

                productDetails.add(productDetail);
            }
        } catch (SQLException e) {
            // Log the error (consistent with CategoryDAO pattern)
        } finally {
            closeResources();
        }
        return productDetails;
    }

    // Get product detail by product detail ID
    public ProductDetail getProductDetailById(int productDetailId) {
        ProductDetail productDetail = null;
        String query = "SELECT * FROM Product_details WHERE product_detail_id = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                productDetail = new ProductDetail();
                productDetail.setProductDetailId(rs.getInt("product_detail_id"));
                productDetail.setProductId(rs.getInt("product_id"));
                productDetail.setScientificName(rs.getString("scientific_name"));
                productDetail.setCommonName(rs.getString("common_name"));
                productDetail.setOrigin(rs.getString("origin"));
                productDetail.setSize(rs.getString("size"));
                productDetail.setLifespan(rs.getString("lifespan"));
                productDetail.setWaterType(rs.getString("water_type"));
                productDetail.setWaterTemperature(rs.getString("water_temperature"));
                productDetail.setWaterPh(rs.getString("water_ph"));
                productDetail.setDiet(rs.getString("diet"));
                productDetail.setBreedingDifficulty(rs.getString("breeding_difficulty"));
                productDetail.setCareLevel(rs.getString("care_level"));
                productDetail.setCompatibility(rs.getString("compatibility"));
                productDetail.setIsDeleted(rs.getInt("is_deleted"));
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productDetail;
    }

    // Get product detail by product ID
    public ProductDetail getProductDetailByProductId(int productId) {
        ProductDetail productDetail = null;
        String query = "SELECT * FROM Product_details WHERE product_id = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                productDetail = new ProductDetail();
                productDetail.setProductDetailId(rs.getInt("product_detail_id"));
                productDetail.setProductId(rs.getInt("product_id"));
                productDetail.setScientificName(rs.getString("scientific_name"));
                productDetail.setCommonName(rs.getString("common_name"));
                productDetail.setOrigin(rs.getString("origin"));
                productDetail.setSize(rs.getString("size"));
                productDetail.setLifespan(rs.getString("lifespan"));
                productDetail.setWaterType(rs.getString("water_type"));
                productDetail.setWaterTemperature(rs.getString("water_temperature"));
                productDetail.setWaterPh(rs.getString("water_ph"));
                productDetail.setDiet(rs.getString("diet"));
                productDetail.setBreedingDifficulty(rs.getString("breeding_difficulty"));
                productDetail.setCareLevel(rs.getString("care_level"));
                productDetail.setCompatibility(rs.getString("compatibility"));
                productDetail.setIsDeleted(rs.getInt("is_deleted"));
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productDetail;
    }

    // Insert new product detail
    public boolean insertProductDetail(ProductDetail productDetail) {
        String query = "INSERT INTO Product_details (product_id, scientific_name, common_name, origin, size, " +
                      "lifespan, water_type, water_temperature, water_ph, diet, breeding_difficulty, " +
                      "care_level, compatibility, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productDetail.getProductId());
            ps.setString(2, productDetail.getScientificName());
            ps.setString(3, productDetail.getCommonName());
            ps.setString(4, productDetail.getOrigin());
            ps.setString(5, productDetail.getSize());
            ps.setString(6, productDetail.getLifespan());
            ps.setString(7, productDetail.getWaterType());
            ps.setString(8, productDetail.getWaterTemperature());
            ps.setString(9, productDetail.getWaterPh());
            ps.setString(10, productDetail.getDiet());
            ps.setString(11, productDetail.getBreedingDifficulty());
            ps.setString(12, productDetail.getCareLevel());
            ps.setString(13, productDetail.getCompatibility());
            ps.setInt(14, productDetail.getIsDeleted() != null ? productDetail.getIsDeleted() : 0);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

    // Update existing product detail
    public boolean updateProductDetail(ProductDetail productDetail) {
        String query = "UPDATE Product_details SET product_id = ?, scientific_name = ?, common_name = ?, " +
                      "origin = ?, size = ?, lifespan = ?, water_type = ?, water_temperature = ?, " +
                      "water_ph = ?, diet = ?, breeding_difficulty = ?, care_level = ?, compatibility = ? " +
                      "WHERE product_detail_id = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productDetail.getProductId());
            ps.setString(2, productDetail.getScientificName());
            ps.setString(3, productDetail.getCommonName());
            ps.setString(4, productDetail.getOrigin());
            ps.setString(5, productDetail.getSize());
            ps.setString(6, productDetail.getLifespan());
            ps.setString(7, productDetail.getWaterType());
            ps.setString(8, productDetail.getWaterTemperature());
            ps.setString(9, productDetail.getWaterPh());
            ps.setString(10, productDetail.getDiet());
            ps.setString(11, productDetail.getBreedingDifficulty());
            ps.setString(12, productDetail.getCareLevel());
            ps.setString(13, productDetail.getCompatibility());
            ps.setInt(14, productDetail.getProductDetailId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

    // Soft delete product detail
    public boolean deleteProductDetail(int productDetailId) {
        String query = "UPDATE Product_details SET is_deleted = 1 WHERE product_detail_id = ?";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productDetailId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

    // Get product details by water type
    public List<ProductDetail> getProductDetailsByWaterType(String waterType) {
        List<ProductDetail> productDetails = new ArrayList<>();
        String query = "SELECT * FROM Product_details WHERE water_type = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, waterType);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductDetail productDetail = createProductDetailFromResultSet(rs);
                productDetails.add(productDetail);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productDetails;
    }

    // Get product details by care level
    public List<ProductDetail> getProductDetailsByCareLevel(String careLevel) {
        List<ProductDetail> productDetails = new ArrayList<>();
        String query = "SELECT * FROM Product_details WHERE care_level = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, careLevel);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductDetail productDetail = createProductDetailFromResultSet(rs);
                productDetails.add(productDetail);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productDetails;
    }

    // Helper method to create ProductDetail object from ResultSet
    private ProductDetail createProductDetailFromResultSet(ResultSet rs) throws SQLException {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductDetailId(rs.getInt("product_detail_id"));
        productDetail.setProductId(rs.getInt("product_id"));
        productDetail.setScientificName(rs.getString("scientific_name"));
        productDetail.setCommonName(rs.getString("common_name"));
        productDetail.setOrigin(rs.getString("origin"));
        productDetail.setSize(rs.getString("size"));
        productDetail.setLifespan(rs.getString("lifespan"));
        productDetail.setWaterType(rs.getString("water_type"));
        productDetail.setWaterTemperature(rs.getString("water_temperature"));
        productDetail.setWaterPh(rs.getString("water_ph"));
        productDetail.setDiet(rs.getString("diet"));
        productDetail.setBreedingDifficulty(rs.getString("breeding_difficulty"));
        productDetail.setCareLevel(rs.getString("care_level"));
        productDetail.setCompatibility(rs.getString("compatibility"));
        productDetail.setIsDeleted(rs.getInt("is_deleted"));
        return productDetail;
    }

    // Helper method to close resources
    private void closeResources() {
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
}