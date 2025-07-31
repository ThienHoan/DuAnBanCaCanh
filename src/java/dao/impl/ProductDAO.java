//package dao.impl;
//
//import java.math.BigDecimal;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import model.entity.Product;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import model.entity.Product;
//import utils.db.DBContext;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class ProductDAO {
//
//    Connection conn = null;
//    PreparedStatement ps = null;
//    ResultSet rs = null;
//
//    public ProductDAO() {
//        // Connection sẽ được tạo trong mỗi method để tránh timeout
//    }
//
//    public List<Product> getAllProducts() {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
//                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
//                      "ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
//                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
//                      "FROM Products p";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//            
//            while (rs.next()) {
//                Product product = new Product(
//                    rs.getInt("product_id"),
//                    rs.getInt("category_id") != 0 ? rs.getInt("category_id") : 0,
//                    rs.getString("name"),
//                    rs.getString("description"),
//                    rs.getString("short_description"),
//                    rs.getBigDecimal("price"),
//                    rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO,
//                    rs.getInt("quantity"),
//                    rs.getString("sku"),
//                    rs.getString("status"),
//                    rs.getInt("featured"),
//                    rs.getString("created_at"),
//                    rs.getString("updated_at"),
//                    rs.getInt("is_deleted"),
//                    rs.getInt("sold_quantity")
//                );
//                products.add(product);
//            }
//            System.out.println("Số sản phẩm lấy được: " + products.size()); // Log ra console
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Lỗi SQL: " + e.getMessage());
//        }
//
//        return products;
//    }
//
//    public boolean toggleIsDeleted(int productId) {
//        String query = "UPDATE Products SET is_deleted = CASE WHEN is_deleted = 0 THEN 1 ELSE 0 END, "
//                + "updated_at = GETDATE() WHERE product_id = ?";
//
//        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setInt(1, productId);
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            return false;
//        }
//    }
//
//    public boolean createProduct(Product product) {
//        String query = "INSERT INTO Products (category_id, name, description, short_description, "
//                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted) "
//                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?)";
//
//        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
//
//            // Safely set the category ID (handle null case)
//            if (product.getCategoryId() != null) {
//                ps.setInt(1, product.getCategoryId());
//            } else {
//                ps.setInt(1, 1); // Default category if null
//            }
//
//            // Set name with null check
//            ps.setString(2, product.getName() != null ? product.getName() : "");
//
//            // Set description with null check
//            ps.setString(3, product.getDescription() != null ? product.getDescription() : "");
//
//            // Set short description with null check
//            ps.setString(4, product.getShortDescription() != null ? product.getShortDescription() : "");
//
//            // Set price with null check
//            ps.setBigDecimal(5, product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
//
//            // Set sale price with null check
//            ps.setBigDecimal(6, product.getSalePrice() != null ? product.getSalePrice() : null);
//
//            // Set quantity
//            ps.setInt(7, product.getQuantity());
//
//            // Set SKU with null check
//            ps.setString(8, product.getSku() != null ? product.getSku() : "");
//
//            // Set status with null check
//            ps.setString(9, product.getStatus() != null ? product.getStatus() : "active");
//
//            // Set featured with null check
//            ps.setInt(10, product.getFeatured() != null ? product.getFeatured() : 0);
//
//            // We're using GETDATE() for created_at and updated_at in the SQL query
//            // Set is_deleted with null check
//            ps.setInt(11, product.getIsDeleted() != null ? product.getIsDeleted() : 0);
//
//            System.out.println("Executing SQL: " + query);
//            System.out.println("With SKU: " + product.getSku());
//
//            int rowsAffected = ps.executeUpdate();
//            System.out.println("Rows affected: " + rowsAffected);
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("SQL Error creating product: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean updateProduct(Product product) {
//        String query = "UPDATE Products SET category_id = ?, name = ?, description = ?, short_description = ?, "
//                + "price = ?, sale_price = ?, quantity = ?, sku = ?, status = ?, featured = ?, "
//                + "is_deleted = ?, updated_at = GETDATE() WHERE product_id = ?";
//
//        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setObject(1, product.getCategoryId(), java.sql.Types.INTEGER);
//            ps.setString(2, product.getName());
//            ps.setString(3, product.getDescription());
//            ps.setString(4, product.getShortDescription());
//            ps.setBigDecimal(5, product.getPrice());
//            ps.setBigDecimal(6, product.getSalePrice());
//            ps.setInt(7, product.getQuantity());
//            ps.setString(8, product.getSku());
//            ps.setString(9, product.getStatus());
//            ps.setInt(10, product.getFeatured());
//            ps.setInt(11, product.getIsDeleted());
//            ps.setInt(12, product.getProductId());
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public int getLastInsertProductId() {
//        String query = "SELECT TOP 1 product_id FROM Products ORDER BY product_id DESC";
//
//        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
//
//            if (rs.next()) {
//                return rs.getInt("product_id");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting last insert product ID: " + e.getMessage());
//        }
//
//        return -1;
//    }
//
//    public Product getProductById(int productId) {
//        String query = "SELECT p.*, ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
//                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
//                      "FROM Products p WHERE p.product_id = ?";
//        
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setInt(1, productId);
//            ResultSet rs = ps.executeQuery();
//              if (rs.next()) {
//                Product product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity"));
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                product.setCreatedAt(rs.getString("created_at"));
//                product.setUpdatedAt(rs.getString("updated_at"));
//                product.setIsDeleted(rs.getInt("is_deleted"));
//                product.setSoldQuantity(rs.getInt("sold_quantity"));
//                
//                return product;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<Product> getActiveProducts() {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
//                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
//                      "ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
//                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
//                      "FROM Products p WHERE p.status = 'active' AND p.is_deleted = 0 ORDER BY p.created_at DESC";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//            
//            while (rs.next()) {
//                Product product = new Product(
//                    rs.getInt("product_id"),
//                    rs.getInt("category_id") != 0 ? rs.getInt("category_id") : 0,
//                    rs.getString("name"),
//                    rs.getString("description"),
//                    rs.getString("short_description"),
//                    rs.getBigDecimal("price"),
//                    rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO,
//                    rs.getInt("quantity"),
//                    rs.getString("sku"),
//                    rs.getString("status"),
//                    rs.getInt("featured"),
//                    rs.getString("created_at"),
//                    rs.getString("updated_at"),
//                    rs.getInt("is_deleted"),
//                    rs.getInt("sold_quantity")
//                );
//                products.add(product);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//          return products;
//    }
//      public List<Product> getProductsByCategory(int categoryId) {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT product_id, category_id, name, description, short_description, " +
//                      "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
//                      "FROM Products WHERE category_id = ?";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setInt(1, categoryId);
//            
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Product product = new Product();
//                    product.setProductId(rs.getInt("product_id"));
//                    product.setCategoryId(rs.getInt("category_id") != 0 ? rs.getInt("category_id") : null);
//                    product.setName(rs.getString("name"));
//                    product.setDescription(rs.getString("description"));
//                    product.setShortDescription(rs.getString("short_description"));
//                    product.setPrice(rs.getBigDecimal("price"));
//                    product.setSalePrice(rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO);
//                    product.setQuantity(rs.getInt("quantity"));
//                    product.setSku(rs.getString("sku"));
//                    product.setStatus(rs.getString("status"));
//                    product.setFeatured(rs.getInt("featured"));
//                    product.setCreatedAt(rs.getString("created_at"));
//                    product.setUpdatedAt(rs.getString("updated_at"));
//                    product.setIsDeleted(rs.getInt("is_deleted"));
//                    
//                    products.add(product);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return products;
//    }
//    public String getProductContext(String fishName) throws SQLException {
//    String sql = "SELECT p.name, p.description, pd.care_level, pd.compatibility, pd.diet, pd.water_temperature "
//               + "FROM Products p JOIN Product_details pd ON p.product_id=pd.product_id "
//               + "WHERE p.name LIKE ?";
//    try (Connection c = DBContext.getConnection();
//         PreparedStatement ps = c.prepareStatement(sql)) {
//        ps.setString(1, "%" + fishName + "%");
//        ResultSet rs = ps.executeQuery();
//        if (!rs.next()) return null;
//        return String.format(
//          "Tên: %s. Mô tả: %s. Chăm sóc: %s. Tương thích: %s. Chế độ ăn: %s. Nhiệt độ: %s°C.",
//          rs.getString("name"),
//          rs.getString("description"),
//          rs.getString("care_level"),
//          rs.getString("compatibility"),
//          rs.getString("diet"),
//          rs.getString("water_temperature")
//        );
//    }
//    }
//    /**
// * Tìm kiếm và lấy danh sách sản phẩm theo tên.
// * Sử dụng LIKE '%...%' để tìm kiếm gần đúng.
// * @param name Tên hoặc một phần tên của sản phẩm cần tìm.
// * @return Một danh sách các sản phẩm có tên khớp với từ khóa tìm kiếm.
// */
//    public List<Product> getProductsByName(String name) {
//    List<Product> products = new ArrayList<>();
//    
//    // Câu SQL sử dụng LIKE để tìm kiếm các sản phẩm có tên chứa từ khóa
//    String query = "SELECT * FROM Products WHERE name LIKE ?";
//    
//    try (Connection conn = DBContext.getConnection();
//         PreparedStatement ps = conn.prepareStatement(query)) {
//        
//        // Gán giá trị cho tham số trong câu SQL
//        // Thêm dấu '%' để thực hiện tìm kiếm gần đúng (contains)
//        ps.setString(1, "%" + name + "%");
//        
//        try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                // Tạo đối tượng Product từ dữ liệu trong ResultSet
//                // Logic này được sao chép từ các phương thức khác để đảm bảo tính nhất quán
//                Product product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity")); // <-- Lấy từ cột 'quantity'
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                // Chuyển đổi từ Timestamp của SQL sang LocalDateTime của Java
//                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
//                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
//                product.setIsDeleted(rs.getInt("is_deleted"));
//                
//                products.add(product);
//            }
//        }
//    } catch (Exception e) {
//        System.err.println("Lỗi khi tìm sản phẩm theo tên: " + e.getMessage());
//        e.printStackTrace();
//    }
//    
//    return products;
//}
//// Dán đoạn mã này vào bên trong lớp public class ProductDAO { ... }
//
///**
// * Lấy danh sách tên của TẤT CẢ các sản phẩm trong database.
// * Phương thức này được dùng để AI có thể tự động nhận diện sản phẩm trong câu hỏi.
// * @return Một danh sách (List) các chuỗi (String) chứa tên sản phẩm.
// */
//public List<String> getAllProductNames() {
//    List<String> productNames = new ArrayList<>();
//    
//    // Câu SQL này chỉ lấy cột 'name' để tối ưu hiệu suất
//    String query = "SELECT name FROM Products";
//    
//    try (Connection conn = DBContext.getConnection();
//         PreparedStatement ps = conn.prepareStatement(query);
//         ResultSet rs = ps.executeQuery()) {
//        
//        // Lặp qua từng kết quả và thêm tên sản phẩm vào danh sách
//        while (rs.next()) {
//            productNames.add(rs.getString("name"));
//        }
//    } catch (Exception e) {
//        System.err.println("Lỗi khi lấy tất cả tên sản phẩm: " + e.getMessage());
//        e.printStackTrace();
//    }
//    
//    return productNames;
//}
//
//
//
///**
// * Lấy danh sách sản phẩm dựa trên mức độ chăm sóc (care level).
// * Phương thức này thực hiện JOIN giữa bảng Products và Product_details.
// * @param careLevel Mức độ chăm sóc (ví dụ: "Dễ", "Trung bình", "Khó").
// * @return Một danh sách các sản phẩm phù hợp.
// */
//// Đoạn mã này sẽ được thêm vào file ProductDAO.java của bạn
//
//public List<Product> getProductsByCareLevel(String careLevel) {
//    List<Product> products = new ArrayList<>();
//    String query = "SELECT p.* " +
//                   "FROM Products p " +
//                   "JOIN Product_details pd ON p.product_id = pd.product_id " +
//                   "WHERE pd.care_level = ?";
//    
//    try (Connection conn = DBContext.getConnection();
//         PreparedStatement ps = conn.prepareStatement(query)) {
//        
//        ps.setString(1, careLevel);
//        
//        try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                // **TÔI SẼ SAO CHÉP LOGIC TỪ CÁC PHƯƠNG THỨC KHÁC CỦA BẠN VÀO ĐÂY**
//                // Ví dụ:
//                Product product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity")); // <-- Sửa lại từ 'stock'
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); // <-- Sửa lại cách set
//                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime()); // <-- Sửa lại cách set
//                product.setIsDeleted(rs.getInt("is_deleted"));
//                
//                products.add(product);
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return products;
//}
//
//    // Private method để lấy connection
//    private Connection getConnection() throws SQLException {
//        return DBContext.getConnection();
//    }
//    
//    /**
//     * Get products that have the same name as the specified product
//     * Used for product variants with different attributes (color, size, etc.)
//     */
//    public List<Product> getProductsWithSameName(String productName) {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT * FROM Products WHERE name = ? AND status = 'active' AND is_deleted = 0";
//        
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setString(1, productName);
//            ResultSet rs = ps.executeQuery();
//            
//            while (rs.next()) {
//                Product product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity"));
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                product.setCreatedAt(rs.getString("created_at"));
//                product.setUpdatedAt(rs.getString("updated_at"));
//                product.setIsDeleted(rs.getInt("is_deleted"));
//                
//                products.add(product);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return products;
//    }
//    
//
//public List<Product> getProductsByCategoryId(Integer categoryId) {
//    List<Product> products = new ArrayList<>();
//    
//    // Handle null categoryId case
//    if (categoryId == null) {
//        return products; // Return empty list if categoryId is null
//    }
//    
//    String query = "SELECT product_id, category_id, name, description, short_description, " +
//                  "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
//                  "FROM Products WHERE category_id = ? AND status = 'active' AND is_deleted = 0";
//    
//    try (Connection conn = getConnection();
//         PreparedStatement ps = conn.prepareStatement(query)) {
//        
//        ps.setInt(1, categoryId); // Auto-unboxing from Integer to int
//        ResultSet rs = ps.executeQuery();
//        
//        while (rs.next()) {
//            Product product = new Product();
//            product.setProductId(rs.getInt("product_id"));
//            product.setCategoryId(rs.getInt("category_id"));
//            product.setName(rs.getString("name"));
//            product.setDescription(rs.getString("description"));
//            product.setShortDescription(rs.getString("short_description"));
//            product.setPrice(rs.getBigDecimal("price"));
//            product.setSalePrice(rs.getBigDecimal("sale_price"));
//            product.setQuantity(rs.getInt("quantity"));
//            product.setSku(rs.getString("sku"));
//            product.setStatus(rs.getString("status"));
//            product.setFeatured(rs.getInt("featured"));
//            product.setCreatedAt(rs.getString("created_at"));
//            product.setUpdatedAt(rs.getString("updated_at"));
//            product.setIsDeleted(rs.getInt("is_deleted"));
//            
//            products.add(product);
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return products;
//}
//
//    // Updated method to get products by category ID with pagination, sorting, and search
//    public List<Product> getProductsByCategoryId(int categoryId, boolean isParent, int page, int pageSize, String sort, String search) {
//        List<Product> products = new ArrayList<>();
//        StringBuilder query = new StringBuilder(
//            "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
//            "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
//            "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
//            "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
//            "FROM Products p "
//        );
//
//        // Handle category filtering
//        List<Integer> categoryIds = new ArrayList<>();
//        if (categoryId == 0) {
//            query.append("WHERE p.is_deleted = 0 ");
//        } else if (isParent) {
//            query.append("JOIN Categories c ON p.category_id = c.category_id "
//                    + "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
//            categoryIds.add(categoryId);
//            categoryIds.add(categoryId);
//        } else {
//            query.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
//            categoryIds.add(categoryId);
//        }
//
//        // Handle search
//        if (search != null && !search.trim().isEmpty()) {
//            query.append("AND p.name LIKE ? ");
//        }
//
//        // Handle sorting
//        if (sort != null && !sort.isEmpty()) {
//            switch (sort) {
//                case "price_asc":
//                    query.append("ORDER BY COALESCE(p.sale_price, p.price) ASC ");
//                    break;
//                case "price_desc":
//                    query.append("ORDER BY COALESCE(p.sale_price, p.price) DESC ");
//                    break;
//                case "name_asc":
//                    query.append("ORDER BY p.name ASC ");
//                    break;
//                case "name_desc":
//                    query.append("ORDER BY p.name DESC ");
//                    break;
//                default:
//                    query.append("ORDER BY p.product_id ASC ");
//                    break;
//            }
//        } else {
//            query.append("ORDER BY p.product_id ASC ");
//        }
//
//        // Add pagination
//        query.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
//
//        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query.toString())) {
//
//            int paramIndex = 1;
//            for (Integer id : categoryIds) {
//                ps.setInt(paramIndex++, id);
//            }
//            if (search != null && !search.trim().isEmpty()) {
//                ps.setString(paramIndex++, "%" + search.trim() + "%");
//            }
//            ps.setInt(paramIndex++, (page - 1) * pageSize);
//            ps.setInt(paramIndex, pageSize);
//
//            // Log the query and parameters for debugging
//            System.out.println("Executing query: " + query.toString());
//            System.out.println("Parameters: categoryId=" + categoryId + ", isParent=" + isParent
//                    + ", page=" + page + ", pageSize=" + pageSize + ", sort=" + sort
//                    + ", search=" + (search != null ? search : "null"));
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Product product = new Product(
//                        rs.getInt("product_id"),
//                        rs.getInt("category_id"),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getString("short_description"),
//                        rs.getBigDecimal("price"),
//                        rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO,
//                        rs.getInt("quantity"),
//                        rs.getString("sku"),
//                        rs.getString("status"),
//                        rs.getInt("featured"),
//                        rs.getString("created_at"),
//                        rs.getString("updated_at"),
//                        rs.getInt("is_deleted"),
//                        rs.getInt("sold_quantity")
//                    );
//                    products.add(product);
//                }
//                System.out.println("Products found: " + products.size());
//            }
//        } catch (SQLException e) {
//            System.err.println("SQL Error in getProductsByCategoryId: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return products;
//    }
//
//    
//   public int getTotalProductsByCategoryId(int categoryId, boolean isParent, String search) {
//        // Get all products first (same logic as getProductsByCategoryId)
//        StringBuilder baseQuery = new StringBuilder(
//            "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
//            "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
//            "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
//            "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
//            "FROM Products p "
//        );
//
//        List<Integer> categoryIds = new ArrayList<>();
//        if (categoryId == 0) {
//            baseQuery.append("WHERE p.is_deleted = 0 ");
//        } else if (isParent) {
//            baseQuery.append("JOIN Categories c ON p.category_id = c.category_id "
//                    + "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
//            categoryIds.add(categoryId);
//            categoryIds.add(categoryId);
//        } else {
//            baseQuery.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
//            categoryIds.add(categoryId);
//        }
//
//        if (search != null && !search.trim().isEmpty()) {
//            baseQuery.append("AND p.name LIKE ? ");
//        }
//
//        baseQuery.append("ORDER BY p.name ASC, p.product_id ASC");
//
//        List<Product> allProducts = new ArrayList<>();
//        try (Connection conn = DBContext.getConnection(); 
//             PreparedStatement ps = conn.prepareStatement(baseQuery.toString())) {
//
//            int paramIndex = 1;
//            for (Integer id : categoryIds) {
//                ps.setInt(paramIndex++, id);
//            }
//            if (search != null && !search.trim().isEmpty()) {
//                ps.setString(paramIndex++, "%" + search.trim() + "%");
//            }
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Product product = new Product(
//                        rs.getInt("product_id"),
//                        rs.getInt("category_id"),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getString("short_description"),
//                        rs.getBigDecimal("price"),
//                        rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price") : BigDecimal.ZERO,
//                        rs.getInt("quantity"),
//                        rs.getString("sku"),
//                        rs.getString("status"),
//                        rs.getInt("featured"),
//                        rs.getString("created_at"),
//                        rs.getString("updated_at"),
//                        rs.getInt("is_deleted"),
//                        rs.getInt("sold_quantity")
//                    );
//allProducts.add(product);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("SQL Error in getTotalProductsByCategoryId: " + e.getMessage());
//            e.printStackTrace();
//            return 0;
//        }
//
//        // Group products by name and count total groups
//        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
//        for (Product product : allProducts) {
//            String name = product.getName();
//            if (!groupedProducts.containsKey(name)) {
//                groupedProducts.put(name, new ArrayList<>());
//            }
//            groupedProducts.get(name).add(product);
//        }
//
//        // Return total count of groups (not individual products)
//        return groupedProducts.size();
//    }
//
//    public List<Product> getDiscountedProducts(int limit) {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT TOP (?) p.product_id, p.category_id, p.name, p.description, p.short_description, " +
//                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
//                      "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
//                      "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
//                      "FROM Products p " +
//                      "WHERE p.sale_price IS NOT NULL AND p.sale_price < p.price AND p.is_deleted = 0 AND p.featured = 1 " +
//                      "ORDER BY p.sale_price ASC";
//
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setInt(1, limit);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Product product = new Product(
//                        rs.getInt("product_id"),
//                        rs.getInt("category_id"),
//                        rs.getString("name"),
//                        rs.getString("description"),
//                        rs.getString("short_description"),
//                        rs.getBigDecimal("price"),
//                        rs.getBigDecimal("sale_price"),
//                        rs.getInt("quantity"),
//                        rs.getString("sku"),
//                        rs.getString("status"),
//                        rs.getInt("featured"),
//                        rs.getString("created_at"),
//                        rs.getString("updated_at"),
//                        rs.getInt("is_deleted"),
//                        rs.getInt("sold_quantity")
//                    );
//                    products.add(product);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return products;
//    }
//
//    public int getProductQuantity(int productId) {
//        String query = "SELECT quantity FROM Products WHERE product_id = ?";
//        
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setInt(1, productId);
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                return rs.getInt("quantity");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//    
//    /**
//     * Check if a SKU already exists in the database (including deleted products)
//     * @param sku The SKU to check
//     * @return true if the SKU exists, false otherwise
//     */
//    public boolean skuExists(String sku) {
//        if (sku == null || sku.trim().isEmpty()) {
//            return false;
//        }
//        
//        String query = "SELECT COUNT(*) FROM Products WHERE sku = ?";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            ps.setString(1, sku);
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                return count > 0;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error checking if SKU exists: " + e.getMessage());
//        }
//        
//        return false;
//    }
//    
//    /**
//     * Generate a unique SKU if the provided SKU already exists
//     * @param sku The original SKU
//     * @return A unique SKU (either the original one if it's unique, or a modified version)
//     */
//    public String getUniqueSku(String sku) {
//        if (!skuExists(sku)) {
//            return sku; // SKU is unique, return as is
//        }
//        
//        // SKU exists, generate a unique one by appending a number
//        String baseSku = sku;
//        int counter = 1;
//        
//        while (skuExists(baseSku + counter)) {
//            counter++;
//        }
//        
//        return baseSku + counter;
//    }
//    
//    /**
//     * Get product by SKU
//     */
//    public Product getProductBySku(String sku) {
//        if (sku == null || sku.trim().isEmpty()) {
//            return null;
//        }
//        
//        Product product = null;
//        String sql = "SELECT * FROM Products WHERE sku = ?";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            
//            ps.setString(1, sku);
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity"));
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                product.setCreatedAt(rs.getString("created_at"));
//                product.setUpdatedAt(rs.getString("updated_at"));
//                product.setIsDeleted(rs.getInt("is_deleted"));
//            }
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return product;
//    }
//
//    
//    /**
//     * Get products with stock level below or equal to the specified threshold
//     * @param threshold The stock level threshold
//     * @return List of products with low stock
//     */
//    public List<Product> getProductsByStockLevel(int threshold) {
//        List<Product> products = new ArrayList<>();
//        String query = "SELECT product_id, category_id, name, description, short_description, "
//                + "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted "
//                + "FROM Products "
//                + "WHERE quantity <= ? AND is_deleted = 0 "
//                + "ORDER BY quantity ASC, name ASC";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            ps.setInt(1, threshold);
//            ResultSet rs = ps.executeQuery();
//            
//            while (rs.next()) {
//                Product product = new Product();
//                product.setProductId(rs.getInt("product_id"));
//                product.setCategoryId(rs.getInt("category_id"));
//                product.setName(rs.getString("name"));
//                product.setDescription(rs.getString("description"));
//                product.setShortDescription(rs.getString("short_description"));
//                product.setPrice(rs.getBigDecimal("price"));
//                product.setSalePrice(rs.getBigDecimal("sale_price"));
//                product.setQuantity(rs.getInt("quantity"));
//                product.setSku(rs.getString("sku"));
//                product.setStatus(rs.getString("status"));
//                product.setFeatured(rs.getInt("featured"));
//                product.setCreatedAt(rs.getString("created_at"));
//                product.setUpdatedAt(rs.getString("updated_at"));
//                product.setIsDeleted(rs.getInt("is_deleted"));
//                
//                products.add(product);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error getting products by stock level: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        return products;
//    }
//
//    /**
//     * Get multiple products by IDs (tối ưu performance)
//     * @param productIds List of product IDs
//     * @return Map of product ID to Product object
//     */
//    public Map<Integer, Product> getProductsByIds(List<Integer> productIds) {
//        Map<Integer, Product> productsMap = new HashMap<>();
//        if (productIds == null || productIds.isEmpty()) {
//            return productsMap;
//        }
//        
//        // Tạo placeholders cho IN clause
//        StringBuilder placeholders = new StringBuilder();
//        for (int i = 0; i < productIds.size(); i++) {
//            if (i > 0) placeholders.append(",");
//            placeholders.append("?");
//        }
//        
//        String query = "SELECT * FROM Products WHERE product_id IN (" + placeholders.toString() + ")";
//        
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            
//            // Set parameters
//            for (int i = 0; i < productIds.size(); i++) {
//                ps.setInt(i + 1, productIds.get(i));
//            }
//            
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Product product = new Product();
//                    product.setProductId(rs.getInt("product_id"));
//                    product.setCategoryId(rs.getInt("category_id"));
//                    product.setName(rs.getString("name"));
//                    product.setDescription(rs.getString("description"));
//                    product.setShortDescription(rs.getString("short_description"));
//                    product.setPrice(rs.getBigDecimal("price"));
//                    product.setSalePrice(rs.getBigDecimal("sale_price"));
//                    product.setQuantity(rs.getInt("quantity"));
//                    product.setSku(rs.getString("sku"));
//                    product.setStatus(rs.getString("status"));
//                    product.setFeatured(rs.getInt("featured"));
//                    product.setCreatedAt(rs.getString("created_at"));
//                    product.setUpdatedAt(rs.getString("updated_at"));
//                    product.setIsDeleted(rs.getInt("is_deleted"));
//                    
//                    productsMap.put(product.getProductId(), product);
//                }
//            }
//            
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy nhiều sản phẩm: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        return productsMap;
//    }
//}
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
import java.util.LinkedHashMap;

public class ProductDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public ProductDAO() {
        // Connection sẽ được tạo trong mỗi method để tránh timeout
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
                      "ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
                      "FROM Products p";
        
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
                    rs.getInt("is_deleted"),
                    rs.getInt("sold_quantity")
                );
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
        String query = "SELECT p.*, ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
                      "FROM Products p WHERE p.product_id = ?";
        
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
                product.setSoldQuantity(rs.getInt("sold_quantity"));
                
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getActiveProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
                      "ISNULL((SELECT SUM(oi.quantity) FROM Order_items oi JOIN Orders o ON oi.order_id = o.order_id " +
                      "WHERE oi.product_id = p.product_id AND o.status IN ('completed', 'delivered')), 0) AS sold_quantity " +
                      "FROM Products p WHERE p.status = 'active' AND p.is_deleted = 0 ORDER BY p.created_at DESC";
        
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
                    rs.getInt("is_deleted"),
                    rs.getInt("sold_quantity")
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
    
    /**
     * Get products that have the same name as the specified product
     * Used for product variants with different attributes (color, size, etc.)
     */
    public List<Product> getProductsWithSameName(String productName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE name = ? AND status = 'active' AND is_deleted = 0";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, productName);
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
    

public List<Product> getProductsByCategoryId(Integer categoryId) {
    List<Product> products = new ArrayList<>();
    
    // Handle null categoryId case
    if (categoryId == null) {
        return products; // Return empty list if categoryId is null
    }
    
    String query = "SELECT product_id, category_id, name, description, short_description, " +
                  "price, sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted " +
                  "FROM Products WHERE category_id = ? AND status = 'active' AND is_deleted = 0";
    
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        ps.setInt(1, categoryId); // Auto-unboxing from Integer to int
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
        
        // First, get all products without pagination to group by name
        StringBuilder baseQuery = new StringBuilder(
            "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
            "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
            "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
            "FROM Products p "
        );

        // Handle category filtering
        List<Integer> categoryIds = new ArrayList<>();
        if (categoryId == 0) {
            baseQuery.append("WHERE p.is_deleted = 0 ");
        } else if (isParent) {
            baseQuery.append("JOIN Categories c ON p.category_id = c.category_id "
                    + "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
            categoryIds.add(categoryId);
            categoryIds.add(categoryId);
        } else {
            baseQuery.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
            categoryIds.add(categoryId);
        }

        // Handle search
        if (search != null && !search.trim().isEmpty()) {
            baseQuery.append("AND p.name LIKE ? ");
        }

        // Always order by name first to group similar products together
        baseQuery.append("ORDER BY p.name ASC, p.product_id ASC");

        // Get all products first
        List<Product> allProducts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(baseQuery.toString())) {

            int paramIndex = 1;
            for (Integer id : categoryIds) {
                ps.setInt(paramIndex++, id);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }

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
                        rs.getInt("is_deleted"),
                        rs.getInt("sold_quantity")
                    );
                    allProducts.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getProductsByCategoryId: " + e.getMessage());
            e.printStackTrace();
            return products;
        }

        // Group products by name
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
        for (Product product : allProducts) {
            String name = product.getName();
            if (!groupedProducts.containsKey(name)) {
                groupedProducts.put(name, new ArrayList<>());
            }
            groupedProducts.get(name).add(product);
        }

        // Convert grouped products to list of groups
        List<List<Product>> productGroups = new ArrayList<>(groupedProducts.values());

        // Apply pagination to groups (not individual products)
        int startGroupIndex = (page - 1) * pageSize;
        int endGroupIndex = Math.min(startGroupIndex + pageSize, productGroups.size());
        
        if (startGroupIndex < productGroups.size()) {
            // Get the groups for current page
            List<List<Product>> currentPageGroups = productGroups.subList(startGroupIndex, endGroupIndex);
            
            // Flatten the groups to get all products for current page
            for (List<Product> group : currentPageGroups) {
                products.addAll(group);
            }
        }

        // Log the results for debugging
        System.out.println("Total products found: " + allProducts.size());
        System.out.println("Grouped into: " + groupedProducts.size() + " groups");
        System.out.println("Products returned: " + products.size());
        System.out.println("Parameters: categoryId=" + categoryId + ", isParent=" + isParent
                + ", page=" + page + ", pageSize=" + pageSize + ", sort=" + sort
                + ", search=" + (search != null ? search : "null"));
        return products;
    }

    
    public int getTotalProductsByCategoryId(int categoryId, boolean isParent, String search) {
        // Get all products first (same logic as getProductsByCategoryId)
        StringBuilder baseQuery = new StringBuilder(
            "SELECT p.product_id, p.category_id, p.name, p.description, p.short_description, " +
            "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
            "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
            "FROM Products p "
        );

        List<Integer> categoryIds = new ArrayList<>();
        if (categoryId == 0) {
            baseQuery.append("WHERE p.is_deleted = 0 ");
        } else if (isParent) {
            baseQuery.append("JOIN Categories c ON p.category_id = c.category_id "
                    + "WHERE (c.category_id = ? OR c.parent_id = ?) AND p.is_deleted = 0 AND c.is_deleted = 0 ");
            categoryIds.add(categoryId);
            categoryIds.add(categoryId);
        } else {
            baseQuery.append("WHERE p.category_id = ? AND p.is_deleted = 0 ");
            categoryIds.add(categoryId);
        }

        if (search != null && !search.trim().isEmpty()) {
            baseQuery.append("AND p.name LIKE ? ");
        }

        baseQuery.append("ORDER BY p.name ASC, p.product_id ASC");

        List<Product> allProducts = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(baseQuery.toString())) {

            int paramIndex = 1;
            for (Integer id : categoryIds) {
                ps.setInt(paramIndex++, id);
            }
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search.trim() + "%");
            }

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
                        rs.getInt("is_deleted"),
                        rs.getInt("sold_quantity")
                    );
                    allProducts.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTotalProductsByCategoryId: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }

        // Group products by name and count total groups
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
        for (Product product : allProducts) {
            String name = product.getName();
            if (!groupedProducts.containsKey(name)) {
                groupedProducts.put(name, new ArrayList<>());
            }
            groupedProducts.get(name).add(product);
        }

        // Return total count of groups (not individual products)
        return groupedProducts.size();
    }

    public List<Product> getDiscountedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT TOP (?) p.product_id, p.category_id, p.name, p.description, p.short_description, " +
                      "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
                      "(SELECT ISNULL(SUM(oi.quantity), 0) FROM Order_items oi INNER JOIN Orders o ON oi.order_id = o.order_id " +
                      "WHERE oi.product_id = p.product_id AND o.status IN ('delivered', 'completed')) AS sold_quantity " +
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
                        rs.getInt("is_deleted"),
                        rs.getInt("sold_quantity")
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
