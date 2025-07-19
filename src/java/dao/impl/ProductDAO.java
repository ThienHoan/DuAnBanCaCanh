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
      public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, category_id, name, description, short_description, " +
                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                      "FROM Products WHERE category_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, categoryId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setCategoryId(rs.getInt("category_id") != 0 ? rs.getInt("category_id") : null);
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setShortDescription(rs.getString("short_description"));
                    product.setPrice(rs.getBigDecimal("price"));
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
        }
        
        return products;
    }
    public String getProductContext(String fishName) throws SQLException {
    String sql = "SELECT p.name, p.description, pd.care_level, pd.compatibility, pd.diet, pd.water_temperature "
               + "FROM Products p JOIN Product_details pd ON p.product_id=pd.product_id "
               + "WHERE p.name LIKE ?";
    try (Connection c = DBContext.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, "%" + fishName + "%");
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;
        return String.format(
          "Tên: %s. Mô tả: %s. Chăm sóc: %s. Tương thích: %s. Chế độ ăn: %s. Nhiệt độ: %s°C.",
          rs.getString("name"),
          rs.getString("description"),
          rs.getString("care_level"),
          rs.getString("compatibility"),
          rs.getString("diet"),
          rs.getString("water_temperature")
        );
    }
    }
    /**
 * Tìm kiếm và lấy danh sách sản phẩm theo tên.
 * Sử dụng LIKE '%...%' để tìm kiếm gần đúng.
 * @param name Tên hoặc một phần tên của sản phẩm cần tìm.
 * @return Một danh sách các sản phẩm có tên khớp với từ khóa tìm kiếm.
 */
    public List<Product> getProductsByName(String name) {
    List<Product> products = new ArrayList<>();
    
    // Câu SQL sử dụng LIKE để tìm kiếm các sản phẩm có tên chứa từ khóa
    String query = "SELECT * FROM Products WHERE name LIKE ?";
    
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        // Gán giá trị cho tham số trong câu SQL
        // Thêm dấu '%' để thực hiện tìm kiếm gần đúng (contains)
        ps.setString(1, "%" + name + "%");
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Tạo đối tượng Product từ dữ liệu trong ResultSet
                // Logic này được sao chép từ các phương thức khác để đảm bảo tính nhất quán
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setQuantity(rs.getInt("quantity")); // <-- Lấy từ cột 'quantity'
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getInt("featured"));
                // Chuyển đổi từ Timestamp của SQL sang LocalDateTime của Java
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                product.setIsDeleted(rs.getInt("is_deleted"));
                
                products.add(product);
            }
        }
    } catch (Exception e) {
        System.err.println("Lỗi khi tìm sản phẩm theo tên: " + e.getMessage());
        e.printStackTrace();
    }
    
    return products;
}
// Dán đoạn mã này vào bên trong lớp public class ProductDAO { ... }

/**
 * Lấy danh sách tên của TẤT CẢ các sản phẩm trong database.
 * Phương thức này được dùng để AI có thể tự động nhận diện sản phẩm trong câu hỏi.
 * @return Một danh sách (List) các chuỗi (String) chứa tên sản phẩm.
 */
public List<String> getAllProductNames() {
    List<String> productNames = new ArrayList<>();
    
    // Câu SQL này chỉ lấy cột 'name' để tối ưu hiệu suất
    String query = "SELECT name FROM Products";
    
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        
        // Lặp qua từng kết quả và thêm tên sản phẩm vào danh sách
        while (rs.next()) {
            productNames.add(rs.getString("name"));
        }
    } catch (Exception e) {
        System.err.println("Lỗi khi lấy tất cả tên sản phẩm: " + e.getMessage());
        e.printStackTrace();
    }
    
    return productNames;
}



/**
 * Lấy danh sách sản phẩm dựa trên mức độ chăm sóc (care level).
 * Phương thức này thực hiện JOIN giữa bảng Products và Product_details.
 * @param careLevel Mức độ chăm sóc (ví dụ: "Dễ", "Trung bình", "Khó").
 * @return Một danh sách các sản phẩm phù hợp.
 */
// Đoạn mã này sẽ được thêm vào file ProductDAO.java của bạn

public List<Product> getProductsByCareLevel(String careLevel) {
    List<Product> products = new ArrayList<>();
    String query = "SELECT p.* " +
                   "FROM Products p " +
                   "JOIN Product_details pd ON p.product_id = pd.product_id " +
                   "WHERE pd.care_level = ?";
    
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        ps.setString(1, careLevel);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // **TÔI SẼ SAO CHÉP LOGIC TỪ CÁC PHƯƠNG THỨC KHÁC CỦA BẠN VÀO ĐÂY**
                // Ví dụ:
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setShortDescription(rs.getString("short_description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setQuantity(rs.getInt("quantity")); // <-- Sửa lại từ 'stock'
                product.setSku(rs.getString("sku"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getInt("featured"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); // <-- Sửa lại cách set
                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime()); // <-- Sửa lại cách set
                product.setIsDeleted(rs.getInt("is_deleted"));
                
                products.add(product);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return products;
}

    // Private method để lấy connection
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