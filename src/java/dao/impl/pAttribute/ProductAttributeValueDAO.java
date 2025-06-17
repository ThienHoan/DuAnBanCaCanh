package dao.impl.pAttribute;

import model.entity.pAttribute.ProductAttributeValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.db.Db;

public class ProductAttributeValueDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Get all product attribute values (non-deleted)
    public List<ProductAttributeValue> getAllProductAttributeValues() {
        List<ProductAttributeValue> attributeValues = new ArrayList<>();
        String query = "SELECT * FROM Product_attribute_values WHERE is_deleted = 0 ORDER BY value_id ASC";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductAttributeValue attributeValue = createProductAttributeValueFromResultSet(rs);
                attributeValues.add(attributeValue);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return attributeValues;
    }

    

    // Get product attribute values by product ID
    public List<ProductAttributeValue> getProductAttributeValuesByProductId(int productId) {
        List<ProductAttributeValue> attributeValues = new ArrayList<>();
        String query = "SELECT * FROM Product_attribute_values WHERE product_id = ? AND is_deleted = 0 ";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductAttributeValue attributeValue = createProductAttributeValueFromResultSet(rs);
                attributeValues.add(attributeValue);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return attributeValues;
    }

    

    

    // Insert new product attribute value
public boolean insertProductAttributeValue(ProductAttributeValue attributeValue) {
    String query = "INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted) VALUES (?, ?, ?, ?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, attributeValue.getProductId());
        ps.setInt(2, attributeValue.getAttributeId());
        ps.setString(3, attributeValue.getValue());
        ps.setInt(4, attributeValue.getIsDeleted() != null ? attributeValue.getIsDeleted() : 0);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean updateProductAttributeValue(ProductAttributeValue attributeValue) {
    String query = "UPDATE Product_attribute_values SET value = ?, is_deleted = 0 WHERE product_id = ? AND attribute_id = ?";
    try {
        conn = Db.getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, attributeValue.getValue());
        ps.setInt(2, attributeValue.getProductId());
        ps.setInt(3, attributeValue.getAttributeId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        // Log the error
        return false;
    } finally {
        closeResources();
    }
}

    // Soft delete a specific attribute value for a product
public boolean deleteProductAttributeValue(int productId, int attributeId) {
    String query = "UPDATE Product_attribute_values SET is_deleted = 1 WHERE product_id = ? AND attribute_id = ?";
    try {
        conn = Db.getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, productId);
        ps.setInt(2, attributeId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        // Log the error
        return false;
    } finally {
    }
}


    // Delete all attribute values for a product (soft delete)
    public boolean deleteProductAttributeValuesByProductId(int productId) {
        String query = "UPDATE Product_attribute_values SET is_deleted = 1 WHERE product_id = ?";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

 public boolean restoreOrInsertProductAttributeValue(ProductAttributeValue attributeValue) {
    String checkQuery = "SELECT value_id FROM Product_attribute_values WHERE product_id = ? AND attribute_id = ?";
    try (Connection conn = Db.getConnection();
         PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {

        psCheck.setInt(1, attributeValue.getProductId());
        psCheck.setInt(2, attributeValue.getAttributeId());
        try (ResultSet rs = psCheck.executeQuery()) {
            if (rs.next()) {
                int existingValueId = rs.getInt("value_id");

                String updateQuery = "UPDATE Product_attribute_values SET value = ?, is_deleted = 0 WHERE value_id = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                    psUpdate.setString(1, attributeValue.getValue());
                    psUpdate.setInt(2, existingValueId);
                    return psUpdate.executeUpdate() > 0;
                }

            } else {
                return insertProductAttributeValue(attributeValue); // sửa luôn insert bên dưới
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // Check if attribute value exists for product and attribute
    public boolean isAttributeValueExists(int productId, int attributeId) {
        String query = "SELECT COUNT(*) FROM Product_attribute_values WHERE product_id = ? AND attribute_id = ? AND is_deleted = 0";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            ps.setInt(2, attributeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
        }
        return false;
    }

    

    

    // Helper method to create ProductAttributeValue object from ResultSet
    private ProductAttributeValue createProductAttributeValueFromResultSet(ResultSet rs) throws SQLException {
        ProductAttributeValue attributeValue = new ProductAttributeValue();
        attributeValue.setValueId(rs.getInt("value_id"));
        attributeValue.setProductId(rs.getInt("product_id"));
        attributeValue.setAttributeId(rs.getInt("attribute_id"));
        attributeValue.setValue(rs.getString("value"));
        attributeValue.setIsDeleted(rs.getInt("is_deleted"));
        return attributeValue;
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