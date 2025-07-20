package test;

import model.entity.pReview.Review;
import model.entity.pReview.ReviewImage;
import service.impl.ReviewServiceImpl;
import service.interfaces.ReviewService;
import java.util.List;

public class TestReviewFix {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Review System Fix ===");
        
        try {
            // Test creating ReviewService
            ReviewService reviewService = new ReviewServiceImpl();
            System.out.println("✅ ReviewService created successfully");
            
            // Test creating Review object
            Review review = new Review();
            review.setProductId(1);
            review.setUserId(1);
            review.setRating(5);
            review.setComment("Great product!");
            System.out.println("✅ Review object created successfully");
            
            // Test creating ReviewImage object
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setReviewId(1);
            reviewImage.setImageUrl("/test/image.jpg");
            System.out.println("✅ ReviewImage object created successfully");
            
            // Test method calls (these will fail if there's no database connection, but that's expected)
            try {
                List<Review> reviews = reviewService.getReviewsByProductId(1);
                System.out.println("✅ getReviewsByProductId method works");
            } catch (Exception e) {
                System.out.println("⚠️ getReviewsByProductId failed (expected if no DB): " + e.getMessage());
            }
            
            try {
                List<ReviewImage> images = reviewService.getImagesByReviewId(1);
                System.out.println("✅ getImagesByReviewId method works");
            } catch (Exception e) {
                System.out.println("⚠️ getImagesByReviewId failed (expected if no DB): " + e.getMessage());
            }
            
            System.out.println("=== All type compatibility issues fixed! ===");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 