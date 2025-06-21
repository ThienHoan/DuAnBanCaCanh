package model.entity;

import java.sql.Timestamp;

/**
 * Wishlist Entity - Quản lý danh sách yêu thích của người dùng
 */
public class Wishlist {
    private int wishlistId;
    private int userId;
    private int productId;
    private Timestamp createdAt;
      // Thông tin bổ sung từ join với bảng products
    private String productName;
    private String productDescription;
    private double productPrice;
    private double salePrice;
    private String productImageUrl;
    private int stockQuantity;
    private boolean productActive;
    private String productStatus;
    private boolean featured;
    private String categoryName;
    
    // Constructors
    public Wishlist() {}
    
    public Wishlist(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }
    
    public Wishlist(int wishlistId, int userId, int productId, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getWishlistId() {
        return wishlistId;
    }
    
    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Product information getters and setters
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public double getProductPrice() {
        return productPrice;
    }
      public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    
    public double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public boolean isProductActive() {
        return productActive;
    }
      public void setProductActive(boolean productActive) {
        this.productActive = productActive;
    }
    
    public String getProductStatus() {
        return productStatus;
    }
    
    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
    
    public boolean isFeatured() {
        return featured;
    }
    
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
