package dao.impl.pAttribute;

import model.entity.pAttribute.ProductAttribute;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.db.Db;

public class ProductAttributeDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Get all product attributes
    public List<ProductAttribute> getAllProductAttributes() {
        List<ProductAttribute> productAttributes = new ArrayList<>();
        String query = "SELECT * FROM Product_attributes ORDER BY attribute_id ASC";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductAttribute productAttribute = createProductAttributeFromResultSet(rs);
                productAttributes.add(productAttribute);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productAttributes;
    }

    // Get product attribute by ID
    public ProductAttribute getProductAttributeById(int attributeId) {
        ProductAttribute productAttribute = null;
        String query = "SELECT * FROM Product_attributes WHERE attribute_id = ?";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, attributeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                productAttribute = createProductAttributeFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Log the error
        } finally {
            closeResources();
        }
        return productAttribute;
    }

    

    // Insert new product attribute
    public boolean insertProductAttribute(ProductAttribute productAttribute) {
        String query = "INSERT INTO Product_attributes (name) VALUES (?)";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, productAttribute.getName());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

    // Update existing product attribute
    public boolean updateProductAttribute(ProductAttribute productAttribute) {
        String query = "UPDATE Product_attributes SET name = ? WHERE attribute_id = ?";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, productAttribute.getName());
            ps.setInt(2, productAttribute.getAttributeId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }

    // Delete product attribute (hard delete - cascades to attribute values)
    public boolean deleteProductAttribute(int attributeId) {
        String query = "DELETE FROM Product_attributes WHERE attribute_id = ?";
        try {
            conn = Db.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, attributeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            return false;
        } finally {
            closeResources();
        }
    }
    
    
    private ProductAttribute createProductAttributeFromResultSet(ResultSet rs) throws SQLException {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeId(rs.getInt("attribute_id"));
        productAttribute.setName(rs.getString("name"));
        return productAttribute;
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