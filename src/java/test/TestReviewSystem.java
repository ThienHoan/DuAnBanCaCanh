package test;

import dao.impl.pReview.ReviewDAO;
import dao.impl.pReview.ReviewImageDAO;
import model.entity.pReview.Review;
import model.entity.pReview.ReviewImage;
import service.impl.ReviewServiceImpl;
import service.interfaces.ReviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestReviewSystem {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Review System ===");
        
        // Test ReviewDAO
        testReviewDAO();
        
        // Test ReviewImageDAO
        testReviewImageDAO();
        
        // Test ReviewService
        testReviewService();
        
        System.out.println("=== Review System Test Completed ===");
    }
    
    private static void testReviewDAO() {
        System.out.println("\n--- Testing ReviewDAO ---");
        ReviewDAO reviewDAO = new ReviewDAO();
        
        try {
            // Test get reviews by product ID
            List<Review> reviews = reviewDAO.getReviewsByProductId(1);
            System.out.println("Reviews for product 1: " + reviews.size());
            
            // Test get average rating
            double avgRating = reviewDAO.getAverageRatingByProductId(1);
            System.out.println("Average rating for product 1: " + avgRating);
            
            // Test get review count
            int reviewCount = reviewDAO.getReviewCountByProductId(1);
            System.out.println("Review count for product 1: " + reviewCount);
            
        } catch (Exception e) {
            System.err.println("Error testing ReviewDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testReviewImageDAO() {
        System.out.println("\n--- Testing ReviewImageDAO ---");
        ReviewImageDAO reviewImageDAO = new ReviewImageDAO();
        
        try {
            // Test get images by review ID
            List<ReviewImage> images = reviewImageDAO.getReviewImagesByReviewId(1);
            System.out.println("Images for review 1: " + images.size());
            
        } catch (Exception e) {
            System.err.println("Error testing ReviewImageDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testReviewService() {
        System.out.println("\n--- Testing ReviewService ---");
        ReviewService reviewService = new ReviewServiceImpl();
        
        try {
            // Test get reviews by product ID
            List<Review> reviews = reviewService.getReviewsByProductId(1);
            System.out.println("Reviews for product 1: " + reviews.size());
            
            // Test get average rating
            double avgRating = reviewService.getAverageRatingByProductId(1);
            System.out.println("Average rating for product 1: " + avgRating);
            
            // Test get review count
            int reviewCount = reviewService.getReviewCountByProductId(1);
            System.out.println("Review count for product 1: " + reviewCount);
            
            // Test get review counts by rating
            int[] ratingCounts = reviewService.getReviewCountsByRating(1);
            System.out.println("Rating counts for product 1:");
            for (int i = 0; i < ratingCounts.length; i++) {
                System.out.println("  " + (i+1) + " star: " + ratingCounts[i]);
            }
            
        } catch (Exception e) {
            System.err.println("Error testing ReviewService: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 