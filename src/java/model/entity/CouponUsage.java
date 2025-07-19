package model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CouponUsage {
    private int usageId;
    private int couponId;
    private int userId;
    private int orderId;
    private BigDecimal discountAmount;
    private Timestamp usedAt;
    private boolean isDeleted;

    // Constructors
    public CouponUsage() {
    }

    public CouponUsage(int usageId, int couponId, int userId, int orderId, BigDecimal discountAmount, Timestamp usedAt, boolean isDeleted) {
        this.usageId = usageId;
        this.couponId = couponId;
        this.userId = userId;
        this.orderId = orderId;
        this.discountAmount = discountAmount;
        this.usedAt = usedAt;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "CouponUsage{" +
                "usageId=" + usageId +
                ", couponId=" + couponId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", discountAmount=" + discountAmount +
                ", usedAt=" + usedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
} 