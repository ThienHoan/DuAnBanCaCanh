package service.impl;

import dao.impl.ReviewDAOImpl;
import dao.impl.ReviewImageDAOImpl;
import dao.interfaces.ReviewDAO;
import dao.interfaces.ReviewImageDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entity.Review;
import model.entity.ReviewImage;
import service.interfaces.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewDAO reviewDAO;
    private final ReviewImageDAO reviewImageDAO;
    
    public ReviewServiceImpl() {
        reviewDAO = new ReviewDAOImpl();
        reviewImageDAO = new ReviewImageDAOImpl();
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = reviewDAO.getReviewsByProductId(productId);
        
        // Load images for each review
        for (Review review : reviews) {
            List<ReviewImage> images = reviewImageDAO.getImagesByReviewId(review.getReviewId());
            // You could store these images in the Review object if needed
        }
        
        return reviews;
    }

    @Override
    public double getAverageRatingByProductId(int productId) {
        return reviewDAO.getAverageRatingByProductId(productId);
    }

    @Override
    public int getReviewCountByProductId(int productId) {
        return reviewDAO.getReviewCountByProductId(productId);
    }

    @Override
    public int addReview(Review review, List<String> imageUrls) {
        int reviewId = reviewDAO.addReview(review);
        
        if (reviewId > 0 && imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReviewId(reviewId);
                reviewImage.setImageUrl(imageUrl);
                reviewImage.setDeleted(false);
                reviewImageDAO.addReviewImage(reviewImage);
            }
        }
        
        return reviewId;
    }

    @Override
    public boolean updateReview(Review review) {
        return reviewDAO.updateReview(review);
    }

    @Override
    public boolean deleteReview(int reviewId) {
        // First mark all images as deleted
        reviewImageDAO.deleteReviewImagesByReviewId(reviewId);
        
        // Then mark the review as deleted
        return reviewDAO.deleteReview(reviewId);
    }

    @Override
    public boolean hasUserReviewedProduct(int userId, int productId) {
        return reviewDAO.hasUserReviewedProduct(userId, productId);
    }

    @Override
    public List<ReviewImage> getImagesByReviewId(int reviewId) {
        return reviewImageDAO.getImagesByReviewId(reviewId);
    }

    @Override
    public int[] getReviewCountsByRating(int productId) {
        return reviewDAO.getReviewCountsByRating(productId);
    }
} 