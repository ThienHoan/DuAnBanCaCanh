package model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private String orderNumber;
    private String status;
    private double totalAmount;
    private double discountAmount;
    private double shippingFee;
    private double tax;
    private double finalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private int shippingAddressId;
    private int billingAddressId;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isDeleted;
    private List<OrderItem> items;

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public int getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(int shippingAddressId) { this.shippingAddressId = shippingAddressId; }
    public int getBillingAddressId() { return billingAddressId; }
    public void setBillingAddressId(int billingAddressId) { this.billingAddressId = billingAddressId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
