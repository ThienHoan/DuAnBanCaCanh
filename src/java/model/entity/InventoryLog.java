package model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity class representing records in the Inventory_logs table
 */
public class InventoryLog {
    private int logId;
    private int productId;
    private int quantityBefore;
    private int quantityAfter;
    private String changeType;
    private String reason;
    private Integer referenceId;  // nullable
    private String referenceType; // nullable
    private LocalDateTime createdAt;
    
    // Product information (for joining)
    private String productName;
    private String productSku;
    
    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public InventoryLog() {
    }
    
    // Constructor with all fields
    public InventoryLog(int logId, int productId, int quantityBefore, int quantityAfter, 
                        String changeType, String reason, Integer referenceId, 
                        String referenceType, String createdAt) {
        this.logId = logId;
        this.productId = productId;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.changeType = changeType;
        this.reason = reason;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        setCreatedAt(createdAt);
    }
    
    // Constructor for creating a new inventory log (without ID)
    public InventoryLog(int productId, int quantityBefore, int quantityAfter, 
                        String changeType, String reason, Integer referenceId, String referenceType) {
        this.productId = productId;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.changeType = changeType;
        this.reason = reason;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
    }
    
    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityBefore() {
        return quantityBefore;
    }

    public void setQuantityBefore(int quantityBefore) {
        this.quantityBefore = quantityBefore;
    }

    public int getQuantityAfter() {
        return quantityAfter;
    }

    public void setQuantityAfter(int quantityAfter) {
        this.quantityAfter = quantityAfter;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }
    
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
    
    // Derived fields getters and setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }
    
    // Derived field - get quantity change
    public int getQuantityChange() {
        return quantityAfter - quantityBefore;
    }
    
    // Helper methods
    public boolean isIncrease() {
        return "increase".equals(changeType);
    }
    
    public boolean isDecrease() {
        return "decrease".equals(changeType);
    }
} 