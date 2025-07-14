package dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.Product;
import utils.db.DBContext;

public class ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ProductDAO() {
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

    public boolean toggleIsDeleted(int productId) {
        String query = "UPDATE Products SET is_deleted = CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END, " +
                      "updated_at = GETDATE() WHERE product_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createProduct(Product product) {
        String query = "INSERT INTO Products (category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
            ps.setInt(10, product.getFeatured() != null ? product.getFeatured() : 0);
            ps.setString(11, product.getCreatedAt());
            ps.setString(12, product.getUpdatedAt());
            ps.setInt(13, product.getIsDeleted() != null ? product.getIsDeleted() : 0);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
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
        String query = "SELECT IDENT_CURRENT('Products') AS last_id"; // SQL Server
        int id = -1;
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                id = rs.getInt("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Product getProductById(int productId) {
        String query = "SELECT * FROM Products WHERE product_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            
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
        }
        return null;
    }

    private Connection getConnection() throws SQLException {
        return DBContext.getConnection();
    }

    // Updated method to get products by category ID with pagination, sorting, and search
    public List<Product> getProductsByCategoryId(int categoryId, boolean isParent, int page, int pageSize, String sort, String search) {
        List<Product> products = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
            "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted " +
            "FROM Products p "
        );

        // Handle category filtering
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

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {
            
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
            System.out.println("Parameters: categoryId=" + categoryId + ", isParent=" + isParent + 
                               ", page=" + page + ", pageSize=" + pageSize + ", sort=" + sort + 
                               ", search=" + (search != null ? search : "null"));

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

    public List<Product> getDiscountedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT TOP (?) p.product_id, p.category_id, p.name, p.description, p.short_description, " +
                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted " +
                      "FROM Products p " +
                      "WHERE p.sale_price IS NOT NULL AND p.sale_price < p.price AND p.is_deleted = 0 AND p.featured = 1 " +
                      "ORDER BY p.sale_price ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
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
}