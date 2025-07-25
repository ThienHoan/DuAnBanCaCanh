package model.entity.pReview;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewImage {
    private int imageId;
    private Integer reviewId; // Using Integer to allow null values
    private String imageUrl;
    private Integer isDeleted; // Changed from bit to Integer
    
    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public ReviewImage() {
    }

    // Constructor with all fields
    public ReviewImage(int imageId, Integer reviewId, String imageUrl, Integer isDeleted) {
        this.imageId = imageId;
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Modified to use Integer instead of bit
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

    @Override
    public String toString() {
        return "ReviewImage{" +
                "imageId=" + imageId +
                ", reviewId=" + reviewId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}