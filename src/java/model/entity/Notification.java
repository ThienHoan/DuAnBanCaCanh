package model.entity;

import java.sql.Timestamp;

/**
 * Entity class cho thông báo
 */
public class Notification {
    private int notificationId;
    private int userId;
    private int referenceId; // ID của đối tượng liên quan (đơn hàng, sản phẩm, ...)
    private String referenceType; // Loại đối tượng liên quan (order, product, ...)
    private String message; // Nội dung thông báo
    private String type; // Loại thông báo (payment_success, order_status, ...)
    private boolean isRead; // Đã đọc hay chưa
    private Timestamp createdAt; // Thời gian tạo
    
    public Notification() {
    }
    
    public Notification(int userId, int referenceId, String referenceType, String message, String type) {
        this.userId = userId;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.message = message;
        this.type = type;
        this.isRead = false;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Notification{" + "notificationId=" + notificationId + ", userId=" + userId + ", message=" + message + ", isRead=" + isRead + '}';
    }
} 