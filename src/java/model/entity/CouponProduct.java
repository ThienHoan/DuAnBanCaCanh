package model.entity;

public class CouponProduct {
    private int couponProductId;
    private int couponId;
    private int productId;
    private boolean isDeleted;

    // Constructors
    public CouponProduct() {
    }

    public CouponProduct(int couponProductId, int couponId, int productId, boolean isDeleted) {
        this.couponProductId = couponProductId;
        this.couponId = couponId;
        this.productId = productId;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getCouponProductId() {
        return couponProductId;
    }

    public void setCouponProductId(int couponProductId) {
        this.couponProductId = couponProductId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "CouponProduct{" +
                "couponProductId=" + couponProductId +
                ", couponId=" + couponId +
                ", productId=" + productId +
                ", isDeleted=" + isDeleted +
                '}';
    }
} 