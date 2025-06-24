package dao.impl;

import model.entity.ProductView;
import model.entity.ProductDetail;
import model.entity.ProductImage;
import model.entity.ProductAttribute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductViewDAO {
    private Connection connection;

    public ProductViewDAO(Connection connection) {
        this.connection = connection;
    }

    public static class PaginatedResult {
        private List<ProductView> products;
        private int totalCount;
        private int totalPages;

        public PaginatedResult(List<ProductView> products, int totalCount, int pageSize) {
            this.products = products;
            this.totalCount = totalCount;
            this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
        }

        public List<ProductView> getProducts() {
            return products;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }

    public PaginatedResult getProductsByCategoryId(int categoryId, int page, int pageSize) throws SQLException {
        List<ProductView> products = new ArrayList<>();
        List<Integer> processedProductIds = new ArrayList<>();
        int totalCount = 0;

        // Query to get total count
        String countSql = "SELECT COUNT(DISTINCT p.product_id) AS total_count " +
                "FROM Products p " +
                "WHERE p.category_id = ? AND p.status = 'active' AND p.is_deleted = 0";

        // Main query with pagination
        String sql = "SELECT " +
                "p.product_id, p.category_id, c.name AS category_name, p.name, p.short_description, " +
                "p.price, p.sale_price, p.quantity, p.sku, p.status, p.featured, p.created_at, p.updated_at, p.is_deleted, " +
                "pd.product_detail_id, pd.scientific_name, pd.common_name, pd.origin, pd.size, pd.lifespan, " +
                "pd.water_type, pd.water_temperature, pd.water_ph, pd.diet, pd.breeding_difficulty, pd.care_level, pd.compatibility, pd.is_deleted AS pd_is_deleted, " +
                "pi.image_id, pi.image_url, pi.is_main, pi.display_order, pi.is_deleted AS pi_is_deleted, " +
                "pa.attribute_id, pa.name AS attribute_name, pav.value_id, pav.value, pav.is_deleted AS pav_is_deleted " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Product_details pd ON p.product_id = pd.product_id " +
                "LEFT JOIN Product_images pi ON p.product_id = pi.product_id " +
                "LEFT JOIN Product_attribute_values pav ON p.product_id = pav.product_id " +
                "LEFT JOIN Product_attributes pa ON pav.attribute_id = pa.attribute_id " +
                "WHERE p.category_id = ? AND p.status = 'active' AND p.is_deleted = 0 " +
                "ORDER BY p.product_id " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            countStmt.setInt(1, categoryId);
            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                totalCount = countRs.getInt("total_count");
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();

            ProductView currentProduct = null;
            int currentProductId = -1;

            while (rs.next()) {
                int productId = rs.getInt("product_id");

                if (productId != currentProductId) {
                    if (currentProduct != null) {
                        products.add(currentProduct);
                    }
                    currentProductId = productId;
                    processedProductIds.add(productId);

                    currentProduct = new ProductView();
                    currentProduct.setProductId(productId);
                    currentProduct.setCategoryId(rs.getInt("category_id"));
                    currentProduct.setCategoryName(rs.getString("category_name"));
                    currentProduct.setName(rs.getString("name"));
                    currentProduct.setShortDescription(rs.getString("short_description"));
                    currentProduct.setPrice(rs.getBigDecimal("price"));
                    currentProduct.setSalePrice(rs.getBigDecimal("sale_price"));
                    currentProduct.setQuantity(rs.getInt("quantity"));
                    currentProduct.setSku(rs.getString("sku"));
                    currentProduct.setStatus(rs.getString("status"));
                    currentProduct.setFeatured(rs.getInt("featured"));
                    currentProduct.setCreatedAt(rs.getTimestamp("created_at") != null ?
                            rs.getTimestamp("created_at").toLocalDateTime() : null);
                    currentProduct.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                            rs.getTimestamp("updated_at").toLocalDateTime() : null);
                    currentProduct.setIsDeleted(rs.getInt("is_deleted"));
                    currentProduct.setImages(new ArrayList<>());
                    currentProduct.setAttributes(new ArrayList<>());
                }

                if (rs.getInt("product_detail_id") != 0 && currentProduct.getProductDetail() == null) {
                    ProductDetail pd = new ProductDetail();
                    pd.setProductDetailId(rs.getInt("product_detail_id"));
                    pd.setProductId(productId);
                    pd.setScientificName(rs.getString("scientific_name"));
                    pd.setCommonName(rs.getString("common_name"));
                    pd.setOrigin(rs.getString("origin"));
                    pd.setSize(rs.getString("size"));
                    pd.setLifespan(rs.getString("lifespan"));
                    pd.setWaterType(rs.getString("water_type"));
                    pd.setWaterTemperature(rs.getString("water_temperature"));
                    pd.setWaterPh(rs.getString("water_ph"));
                    pd.setDiet(rs.getString("diet"));
                    pd.setBreedingDifficulty(rs.getString("breeding_difficulty"));
                    pd.setCareLevel(rs.getString("care_level"));
                    pd.setCompatibility(rs.getString("compatibility"));
                    pd.setIsDeleted(rs.getInt("pd_is_deleted"));
                    currentProduct.setProductDetail(pd);
                }

                if (rs.getInt("image_id") != 0) {
                    ProductImage img = new ProductImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setProductId(productId);
                    img.setImageUrl(rs.getString("image_url"));
                    img.setMain(rs.getInt("is_main"));
                    img.setDisplayOrder(rs.getInt("display_order"));
                    img.setDeleted(rs.getInt("pi_is_deleted"));
                    if (currentProduct.getImages().stream().noneMatch(i -> i.getImageId() == img.getImageId())) {
                        currentProduct.getImages().add(img);
                    }
                }

                if (rs.getInt("value_id") != 0) {
                    ProductAttribute attr = new ProductAttribute();
                    attr.setValueId(rs.getInt("value_id"));
                    attr.setProductId(productId);
                    attr.setAttributeId(rs.getInt("attribute_id"));
                    attr.setAttributeName(rs.getString("attribute_name"));
                    attr.setValue(rs.getString("value"));
                    attr.setIsDeleted(rs.getInt("pav_is_deleted"));
                    if (currentProduct.getAttributes().stream().noneMatch(a -> a.getValueId() == attr.getValueId())) {
                        currentProduct.getAttributes().add(attr);
                    }
                }
            }

            if (currentProduct != null) {
                products.add(currentProduct);
            }
        }

        return new PaginatedResult(products, totalCount, pageSize);
    }
}