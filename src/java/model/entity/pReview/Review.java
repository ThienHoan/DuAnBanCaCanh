
package model.entity.pReview;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private int reviewId;
    private Integer productId; // Using Integer to allow null values
    private Integer userId; // Using Integer to allow null values
    private Integer orderId; // Using Integer to allow null values
    private Integer rating; // Using Integer to allow null values
    private String comment;
    private LocalDateTime reviewDate;
    private String status;
    private Integer isVerifiedPurchase; // Changed from bit to Integer
    private Integer isDeleted; // Changed from bit to Integer
    
    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public Review() {
    }

    // Constructor with all fields
    public Review(int reviewId, Integer productId, Integer userId, Integer orderId, 
                 Integer rating, String comment, String reviewDate, String status, 
                 Integer isVerifiedPurchase, Integer isDeleted) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.rating = rating;
        this.comment = comment;
        setReviewDate(reviewDate);
        this.status = status;
        this.isVerifiedPurchase = isVerifiedPurchase;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getters and Setters for reviewDate with String conversion
    public String getReviewDate() {
        return reviewDate != null ? reviewDate.format(DATE_FORMATTER) : null;
    }

    public void setReviewDate(String reviewDate) {
        if (reviewDate != null && !reviewDate.isEmpty()) {
            this.reviewDate = LocalDateTime.parse(reviewDate, DATE_FORMATTER);
        } else {
            this.reviewDate = null;
        }
    }
    
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Modified to use Integer instead of bit
    public Integer getIsVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public void setIsVerifiedPurchase(Integer isVerifiedPurchase) {
        this.isVerifiedPurchase = isVerifiedPurchase;
    }
    
    // Boolean convenience methods for verified purchase
    public boolean isVerifiedPurchase() {
        return isVerifiedPurchase != null && isVerifiedPurchase == 1;
    }
    
    public void setVerifiedPurchase(boolean isVerifiedPurchase) {
        this.isVerifiedPurchase = isVerifiedPurchase ? 1 : 0;
    }

    // Modified to use Integer instead of bit
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    // Boolean convenience methods for deletion
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
    
    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    public void setActive(boolean active) {
        this.status = active ? "active" : "inactive";
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                ", status='" + status + '\'' +
                ", isVerifiedPurchase=" + isVerifiedPurchase +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
