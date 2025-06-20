/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Coupon {
    private int couponId;
    private String code;
    private String discountType;
    private double discountValue;
    private double discountAmount; // Lưu số tiền giảm giá thực tế
    private double minimumOrder;
    private double maximumDiscount;

    // Constructor, getters, and setters
    public Coupon() {}

    public Coupon(int couponId, String code, String discountType, double discountValue, double discountAmount, double minimumOrder, double maximumDiscount) {
        this.couponId = couponId;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.discountAmount = discountAmount;
        this.minimumOrder = minimumOrder;
        this.maximumDiscount = maximumDiscount;
    }

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }
    public double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(double maximumDiscount) { this.maximumDiscount = maximumDiscount; }
}