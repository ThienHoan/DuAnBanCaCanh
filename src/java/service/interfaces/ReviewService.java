package service.interfaces;

import java.util.List;
import model.entity.Review;
import model.entity.ReviewImage;

public interface ReviewService {
    /**
     * Get all reviews for a specific product
     * 
     * @param productId The product ID
     * @return List of reviews for the product
     */
    List<Review> getReviewsByProductId(int productId);
    
    /**
     * Get the average rating for a product
     * 
     * @param productId The product ID
     * @return Average rating value
     */
    double getAverageRatingByProductId(int productId);
    
    /**
     * Get the count of reviews for a product
     * 
     * @param productId The product ID
     * @return Number of reviews
     */
    int getReviewCountByProductId(int productId);
    
    /**
     * Add a new review
     * 
     * @param review Review object to add
     * @param imageUrls List of image URLs to associate with the review (can be null)
     * @return The ID of the newly created review, or -1 if failed
     */
    int addReview(Review review, List<String> imageUrls);
    
    /**
     * Update an existing review
     * 
     * @param review Review object with updated data
     * @return true if update was successful, false otherwise
     */
    boolean updateReview(Review review);
    
    /**
     * Delete a review (mark as deleted)
     * 
     * @param reviewId ID of review to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteReview(int reviewId);
    
    /**
     * Check if a user has already reviewed a product
     * 
     * @param userId User ID
     * @param productId Product ID
     * @return true if user has already reviewed the product, false otherwise
     */
    boolean hasUserReviewedProduct(int userId, int productId);
    
    /**
     * Get all images for a specific review
     * 
     * @param reviewId The review ID
     * @return List of review images
     */
    List<ReviewImage> getImagesByReviewId(int reviewId);
    
    /**
     * Get review counts by rating for a product
     * 
     * @param productId Product ID
     * @return Array of counts for ratings 1-5
     */
    int[] getReviewCountsByRating(int productId);
} 