
package model.entity.pCart;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cart {
    private int cartId;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Cart(int cartId, int userId, String createdAt, String updatedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = LocalDateTime.parse(createdAt, DATE_FORMATTER);
        this.updatedAt = LocalDateTime.parse(updatedAt, DATE_FORMATTER);
    }


    // Getters & Setters
    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt, DATE_FORMATTER);
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = LocalDateTime.parse(updatedAt, DATE_FORMATTER);
    }

    // Nếu cần lấy String format ra ngoài
    public String getCreatedAtString() {
        return createdAt.format(DATE_FORMATTER);
    }
    public String getUpdatedAtString() {
        return updatedAt.format(DATE_FORMATTER);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", createdAt=" + getCreatedAtString() +
                ", updatedAt=" + getUpdatedAtString() +
                '}';
    }
}
