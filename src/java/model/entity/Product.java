package model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Product {
    private int productId;
    private Integer categoryId; // Using Integer to allow null values
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int quantity;
    private String sku;
    private String status;
    private Integer featured;  // Changed from boolean to Integer
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted; // Changed from boolean to Integer
    private Integer soldQuantity; // Số lượng đã bán
    
    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public Product() {
    }    // Constructor with all fields
    public Product(int productId, Integer categoryId, String name, String description, 
                  String shortDescription, BigDecimal price, BigDecimal salePrice, int quantity, 
                  String sku, String status, Integer featured, String createdAt, 
                  String updatedAt, Integer isDeleted, Integer soldQuantity) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.price = price;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.sku = sku;
        this.status = status;
        this.featured = featured;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        this.isDeleted = isDeleted;
        this.soldQuantity = soldQuantity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    // Modified to use Integer instead of boolean
    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }
    
    // Boolean convenience methods
    public boolean isFeatured() {
        return featured != null && featured == 1;
    }
    
    public void setFeaturedBoolean(boolean featured) {
        this.featured = featured ? 1 : 0;
    }

    // Getters and Setters for createdAt and updatedAt with String conversion
    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(DATE_FORMATTER) : null;
    }

    public void setCreatedAt(String createdAt) {
        if (createdAt != null && !createdAt.isEmpty()) {
            this.createdAt = LocalDateTime.parse(createdAt, DATE_FORMATTER);
        } else {
            this.createdAt = null;
        }
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(DATE_FORMATTER) : null;
    }

    public void setUpdatedAt(String updatedAt) {
        if (updatedAt != null && !updatedAt.isEmpty()) {
            this.updatedAt = LocalDateTime.parse(updatedAt, DATE_FORMATTER);
        } else {
            this.updatedAt = null;
        }
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Modified to use Integer instead of boolean
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    // Boolean convenience methods
    public boolean isDeleted() {
        return isDeleted != null && isDeleted == 1;
    }
      public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted ? 1 : 0;
    }
    
    // Boolean convenience methods for status
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    public void setActive(boolean active) {
        this.status = active ? "active" : "inactive";
    }
    
    // Getter and Setter for soldQuantity
    public Integer getSoldQuantity() {
        return soldQuantity;
    }
    
    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", price=" + price +
                ", salePrice=" + salePrice +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", soldQuantity=" + soldQuantity +
                '}';
    }
}