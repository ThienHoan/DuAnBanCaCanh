/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProductView {
    private int productId;
    private Integer categoryId;
    private String categoryName; // Thêm để hiển thị tên danh mục
    private String name;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int quantity;
    private String sku;
    private String status;
    private Integer featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
    private ProductDetail productDetail; // Thông tin chi tiết sản phẩm
    private List<ProductImage> images; // Danh sách hình ảnh
    private List<ProductAttribute> attributes; // Danh sách thuộc tính (như màu sắc, kích thước)

    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public ProductView() {
    }

    // Constructor with essential fields
    public ProductView(int productId, Integer categoryId, String categoryName, String name, String shortDescription,
                       BigDecimal price, BigDecimal salePrice, int quantity, String sku, String status,
                       Integer featured, LocalDateTime createdAt, LocalDateTime updatedAt, Integer isDeleted,
                       ProductDetail productDetail, List<ProductImage> images, List<ProductAttribute> attributes) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.shortDescription = shortDescription;
        this.price = price;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.sku = sku;
        this.status = status;
        this.featured = featured;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.productDetail = productDetail;
        this.images = images;
        this.attributes = attributes;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }

    public boolean isFeatured() {
        return featured != null && featured == 1;
    }

    public void setFeaturedBoolean(boolean featured) {
        this.featured = featured ? 1 : 0;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtString() {
        return createdAt != null ? createdAt.format(DATE_FORMATTER) : null;
    }

    public void setCreatedAt(String createdAt) {
        if (createdAt != null && !createdAt.isEmpty()) {
            this.createdAt = LocalDateTime.parse(createdAt, DATE_FORMATTER);
        } else {
            this.createdAt = null;
        }
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAtString() {
        return updatedAt != null ? updatedAt.format(DATE_FORMATTER) : null;
    }

    public void setUpdatedAt(String updatedAt) {
        if (updatedAt != null && !updatedAt.isEmpty()) {
            this.updatedAt = LocalDateTime.parse(updatedAt, DATE_FORMATTER);
        } else {
            this.updatedAt = null;
        }
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted != null && isDeleted == 1;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted ? 1 : 0;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<ProductAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ProductView{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", price=" + price +
                ", salePrice=" + salePrice +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", images=" + images.size() +
                ", attributes=" + attributes.size() +
                '}';
    }
}