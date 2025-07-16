package dao.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import utils.db.DBContext;

public class ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;    public ProductDAO() {
        // Connection sẽ được tạo trong mỗi method để tránh timeout
    }    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, " +
                       "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                       "FROM Products";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id") != 0 ? rs.getInt("category_id") : null);
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price") != null ? rs.getBigDecimal("price") : BigDecimal.ZERO);
                product.setSalePrice(rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO);
                product.setQuantity(rs.getInt("quantity"));
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
            System.out.println("Số sản phẩm lấy được: " + products.size()); // Log ra console
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL: " + e.getMessage());
        }

        return products;
    }
        public boolean toggleIsDeleted(int productId) {
        String query = "UPDATE Products SET is_deleted = CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END, " +
                      "updated_at = GETDATE() WHERE product_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }    
    
    public boolean createProduct(Product product) {
        String query = "INSERT INTO Products (category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            // Safely set the category ID (handle null case)
            if (product.getCategoryId() != null) {
                ps.setInt(1, product.getCategoryId());
            } else {
                ps.setInt(1, 1); // Default category if null
            }
            
            // Set name with null check
            ps.setString(2, product.getName() != null ? product.getName() : "");
            
            // Set description with null check
            ps.setString(3, product.getDescription() != null ? product.getDescription() : "");
            
            // Set short description with null check
            ps.setString(4, product.getShortDescription() != null ? product.getShortDescription() : "");
            
            // Set price with null check
            ps.setBigDecimal(5, product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
            
            // Set sale price with null check
            ps.setBigDecimal(6, product.getSalePrice() != null ? product.getSalePrice() : null);
            
            // Set quantity
            ps.setInt(7, product.getQuantity());
            
            // Set SKU with null check
            ps.setString(8, product.getSku() != null ? product.getSku() : "");
            
            // Set status with null check
            ps.setString(9, product.getStatus() != null ? product.getStatus() : "active");
            
            // Set featured with null check
            ps.setInt(10, product.getFeatured() != null ? product.getFeatured() : 0);
            
            // We're using GETDATE() for created_at and updated_at in the SQL query
            
            // Set is_deleted with null check
            ps.setInt(11, product.getIsDeleted() != null ? product.getIsDeleted() : 0);
            
            System.out.println("Executing SQL: " + query);
            System.out.println("With SKU: " + product.getSku());
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error creating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
      public boolean updateProduct(Product product) {
        String query = "UPDATE Products SET category_id = ?, name = ?, description = ?, short_description = ?, " +
                      "price = ?, sale_price = ?, quantity = ?, sku = ?, status = ?, featured = ?, " +
                      "is_deleted = ?, updated_at = GETDATE() WHERE product_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
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
        }
    }
      public int getLastInsertProductId() {
        String query = "SELECT TOP 1 product_id FROM Products ORDER BY product_id DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        } catch (SQLException e) {
            System.out.println("Error getting last insert product ID: " + e.getMessage());
        }
        
        return -1;
    }
    
    public Product getProductById(int productId) {
        String query = "SELECT * FROM Products WHERE product_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
              if (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));
                
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getActiveProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                      "FROM Products WHERE status = 'active' AND is_deleted = 0 ORDER BY created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
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
        }
          return products;
    }

    // Private method để lấy connection
    private Connection getConnection() throws SQLException {
        return DBContext.getConnection();
    }
    
    //search
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
        public List<Product> searchProductsByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, " +
                       "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                       "FROM Products WHERE name LIKE ? OR short_description LIKE ? OR sku LIKE ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern); 
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern); 

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm sản phẩm với từ khóa: " + keyword, e);
        }

        return products;
    }

    public List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
                       "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted " +
                       "FROM Products p " +
                       "JOIN Categories c ON p.category_id = c.category_id " +
                       "WHERE c.name = ? AND p.is_deleted = 0";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setCategoryId(rs.getInt("category_id") != 0 ? rs.getInt("category_id") : null);
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setShortDescription(rs.getString("short_description"));
                    product.setPrice(rs.getBigDecimal("price") != null ? rs.getBigDecimal("price") : BigDecimal.ZERO);
                    product.setSalePrice(rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO);
                    product.setQuantity(rs.getInt("quantity"));
                    product.setSku(rs.getString("sku"));
                    product.setStatus(rs.getString("status"));
                    product.setFeatured(rs.getInt("featured"));
                    product.setCreatedAt(rs.getString("created_at"));
                    product.setUpdatedAt(rs.getString("updated_at"));
                    product.setIsDeleted(rs.getInt("is_deleted"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL khi lấy sản phẩm theo danh mục: " + e.getMessage());
        }
        return products;
    }
    
    public List<Product> searchProductsByNameInCategory(String keyword, String categoryName) {
    List<Product> products = new ArrayList<>();
    String query = "SELECT p.* " +
                  "FROM Products p " +
                  "JOIN Categories c ON p.category_id = c.category_id " +
                  "WHERE p.is_deleted = 0 " +
                  "  AND LOWER(c.name) = LOWER(?) " +
                  "  AND (p.name LIKE ? OR p.short_description LIKE ? OR p.sku LIKE ?)";
    
    System.out.println("Searching with categoryName: " + categoryName + ", keyword: " + keyword); // Log để debug
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, categoryName);
        String searchPattern = "%" + keyword.toLowerCase() + "%"; // Chuyển từ khóa thành lowercase để khớp với LOWER trong SQL
        ps.setString(2, searchPattern);
        ps.setString(3, searchPattern);
        ps.setString(4, searchPattern);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                products.add(product);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("SQL Error: " + e.getMessage()); // Log lỗi SQL
    }
    System.out.println("Found " + products.size() + " products."); // Log số lượng kết quả
    return products;
}
    
    /**
     * Check if a SKU already exists in the database (including deleted products)
     * @param sku The SKU to check
     * @return true if the SKU exists, false otherwise
     */
    public boolean skuExists(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return false;
        }
        
        String query = "SELECT COUNT(*) FROM Products WHERE sku = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if SKU exists: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Generate a unique SKU if the provided SKU already exists
     * @param sku The original SKU
     * @return A unique SKU (either the original one if it's unique, or a modified version)
     */
    public String getUniqueSku(String sku) {
        if (!skuExists(sku)) {
            return sku; // SKU is unique, return as is
        }
        
        // SKU exists, generate a unique one by appending a number
        String baseSku = sku;
        int counter = 1;
        
        while (skuExists(baseSku + counter)) {
            counter++;
        }
        
        return baseSku + counter;
    }
    
    /**
     * Get product by SKU
     */
    public Product getProductBySku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return null;
        }
        
        Product product = null;
        String sql = "SELECT * FROM products WHERE sku = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return product;
    }
}
