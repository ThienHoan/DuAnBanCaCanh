package dao.impl;

import java.math.BigDecimal;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.Product;
import utils.db.DBContext;
import java.util.HashMap;
import java.util.Map;

public class ProductDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ProductDAO() {
        // Connection sẽ được tạo trong mỗi method để tránh timeout
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
                + "FROM Products";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
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
        String query = "UPDATE Products SET is_deleted = CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END, "
                + "updated_at = GETDATE() WHERE product_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean createProduct(Product product) {
        String query = "INSERT INTO Products (category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?)";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

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
        String query = "UPDATE Products SET category_id = ?, name = ?, description = ?, short_description = ?, "
                + "price = ?, sale_price = ?, quantity = ?, sku = ?, status = ?, featured = ?, "
                + "is_deleted = ?, updated_at = GETDATE() WHERE product_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

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
            ps.setInt(11, product.getIsDeleted());
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

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

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
        String query = "SELECT product_id, category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
                + "FROM Products WHERE status = 'active' AND is_deleted = 0 ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT TOP(?) product_id, category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
                + "FROM Products WHERE featured = 1 AND status = 'active' AND is_deleted = 0 ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, "
                + "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted "
                + "FROM Products p "
                + "JOIN Categories c ON p.category_id = c.category_id "
                + "WHERE c.category_id = ? AND p.status = 'active' AND p.is_deleted = 0 "
                + "ORDER BY p.created_at DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, categoryId);
            ResultSet rs = ps.executeQuery();

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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> searchProductsByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
                + "FROM Products "
                + "WHERE (name LIKE ? OR description LIKE ?) AND status = 'active' AND is_deleted = 0 "
                + "ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> searchProductsByNameInCategory(String keyword, String categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, "
                + "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted "
                + "FROM Products p "
                + "JOIN Categories c ON p.category_id = c.category_id "
                + "WHERE c.category_id = ? AND (p.name LIKE ? OR p.description LIKE ?) "
                + "AND p.status = 'active' AND p.is_deleted = 0 "
                + "ORDER BY p.created_at DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, categoryId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Updated method to get products by category ID with pagination, sorting, and search
    public List<Product> getProductsByCategoryId(int categoryId, boolean isParent, int page, int pageSize, String sort, String search) {
        List<Product> products = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, "
                + "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted "
                + "FROM Products p "
        );

        // Handle category filtering
        List<Integer> categoryIds = new ArrayList<>();
        if (categoryId == 0) {
            query.append("WHERE p.is_deleted = 0 ");
        } else if (isParent) {
            query.append("JOIN Categories c ON p.category_id = c.category_id "
                    + "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
            categoryIds.add(categoryId);
            categoryIds.add(categoryId);
        } else {
            query.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
            categoryIds.add(categoryId);
        }

        // Handle search
        if (search != null && !search.trim().isEmpty()) {
            query.append("AND p.name LIKE ? ");
        }

        // Handle sorting
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_asc":
                    query.append("ORDER BY COALESCE(p.sale_price, p.price) ASC ");
                    break;
                case "price_desc":
                    query.append("ORDER BY COALESCE(p.sale_price, p.price) DESC ");
                    break;
                case "name_asc":
                    query.append("ORDER BY p.name ASC ");
                    break;
                case "name_desc":
                    query.append("ORDER BY p.name DESC ");
                    break;
                default:
                    query.append("ORDER BY p.product_id ASC ");
                    break;
            }
        } else {
            query.append("ORDER BY p.product_id ASC ");
        }

        // Add pagination
        query.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            for (Integer id : categoryIds) {
                ps.setInt(paramIndex++, id);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            // Log the query and parameters for debugging
            System.out.println("Executing query: " + query.toString());
            System.out.println("Parameters: categoryId=" + categoryId + ", isParent=" + isParent
                    + ", page=" + page + ", pageSize=" + pageSize + ", sort=" + sort
                    + ", search=" + (search != null ? search : "null"));

            try (ResultSet rs = ps.executeQuery()) {
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
                System.out.println("Products found: " + products.size());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getProductsByCategoryId: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getDiscountedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT TOP (?) p.product_id, p.category_id, p.name, p.description, p.short_description, "
                + "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted "
                + "FROM Products p "
                + "WHERE p.sale_price IS NOT NULL AND p.sale_price < p.price AND p.is_deleted = 0 AND p.featured = 1 "
                + "ORDER BY p.sale_price ASC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
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
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public int getTotalProductsByCategoryId(int categoryId, boolean isParent, String search) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM Products p ");
        List<Integer> categoryIds = new ArrayList<>();

        if (categoryId == 0) {
            query.append("WHERE p.is_deleted = 0 ");
        } else if (isParent) {
            query.append("JOIN Categories c ON p.category_id = c.category_id " +
                         "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
            categoryIds.add(categoryId);
            categoryIds.add(categoryId);
        } else {
            query.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
            categoryIds.add(categoryId);
        }

        if (search != null && !search.trim().isEmpty()) {
            query.append("AND p.name LIKE ? ");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {
            
            int paramIndex = 1;
            for (Integer id : categoryIds) {
                ps.setInt(paramIndex++, id);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex, "%" + search.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getProductQuantity(int productId) {
        String query = "SELECT quantity FROM Products WHERE product_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
        String sql = "SELECT * FROM Products WHERE sku = ?";
        
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

    private Connection getConnection() throws SQLException {
        return DBContext.getConnection();
    }
    
    /**
     * Get products with stock level below or equal to the specified threshold
     * @param threshold The stock level threshold
     * @return List of products with low stock
     */
    public List<Product> getProductsByStockLevel(int threshold) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, "
                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
                + "FROM Products "
                + "WHERE quantity <= ? AND is_deleted = 0 "
                + "ORDER BY quantity ASC, name ASC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();
            
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
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setUpdatedAt(rs.getString("updated_at"));
                product.setIsDeleted(rs.getInt("is_deleted"));
                
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by stock level: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }

    /**
     * Get multiple products by IDs (tối ưu performance)
     * @param productIds List of product IDs
     * @return Map of product ID to Product object
     */
    public Map<Integer, Product> getProductsByIds(List<Integer> productIds) {
        Map<Integer, Product> productsMap = new HashMap<>();
        if (productIds == null || productIds.isEmpty()) {
            return productsMap;
        }
        
        // Tạo placeholders cho IN clause
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < productIds.size(); i++) {
            if (i > 0) placeholders.append(",");
            placeholders.append("?");
        }
        
        String query = "SELECT * FROM Products WHERE product_id IN (" + placeholders.toString() + ")";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            // Set parameters
            for (int i = 0; i < productIds.size(); i++) {
                ps.setInt(i + 1, productIds.get(i));
            }
            
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
                    product.setFeatured(rs.getInt("featured"));
                    product.setCreatedAt(rs.getString("created_at"));
                    product.setUpdatedAt(rs.getString("updated_at"));
                    product.setIsDeleted(rs.getInt("is_deleted"));
                    
                    productsMap.put(product.getProductId(), product);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy nhiều sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productsMap;
    }
}
