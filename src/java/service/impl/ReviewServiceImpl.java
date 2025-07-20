package service.impl;

import dao.impl.pReview.ReviewDAO;
import dao.impl.pReview.ReviewImageDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.entity.pReview.Review;
import model.entity.pReview.ReviewImage;
import service.interfaces.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewDAO reviewDAO;
    private final ReviewImageDAO reviewImageDAO;
    
    public ReviewServiceImpl() {
        reviewDAO = new ReviewDAO();
        reviewImageDAO = new ReviewImageDAO();
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = reviewDAO.getReviewsByProductId(productId);
        
        // Load images for each review
        for (Review review : reviews) {
            List<ReviewImage> images = reviewImageDAO.getReviewImagesByReviewId(review.getReviewId());
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
        int reviewId = reviewDAO.insertReviewAndGetId(review);
        
        if (reviewId > 0 && imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReviewId(reviewId);
                reviewImage.setImageUrl(imageUrl);
                reviewImage.setIsDeleted(0);
                reviewImageDAO.insertReviewImage(reviewImage);
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
        reviewImageDAO.softDeleteReviewImagesByReviewId(reviewId);
        
        // Then mark the review as deleted
        return reviewDAO.softDeleteReview(reviewId);
    }

    @Override
    public boolean hasUserReviewedProduct(int userId, int productId) {
        List<Review> userReviews = reviewDAO.getReviewsByUserId(userId);
        for (Review review : userReviews) {
            if (review.getProductId() != null && review.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasUserPurchasedProduct(int userId, int productId) {
        // Kiểm tra xem người dùng đã mua và nhận sản phẩm này chưa
        // Truy vấn các đơn hàng đã hoàn thành (delivered) của người dùng
        // và kiểm tra xem sản phẩm có trong đơn hàng không
        
        try {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            try {
                conn = utils.db.DBContext.getConnection();
                String query = "SELECT COUNT(*) FROM Orders o " +
                               "JOIN Order_items oi ON o.order_id = oi.order_id " +
                               "WHERE o.user_id = ? AND oi.product_id = ? " +
                               "AND (o.status = 'delivered' OR o.status = 'completed') " +
                               "AND o.is_deleted = 0";
                
                ps = conn.prepareStatement(query);
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public List<ReviewImage> getImagesByReviewId(int reviewId) {
        return reviewImageDAO.getReviewImagesByReviewId(reviewId);
    }

    @Override
    public int[] getReviewCountsByRating(int productId) {
        List<Review> reviews = reviewDAO.getReviewsByProductId(productId);
        int[] counts = new int[5];
        
        for (Review review : reviews) {
            if (review.getRating() != null && review.getRating() >= 1 && review.getRating() <= 5) {
                counts[review.getRating() - 1]++;
            }
        }
        
        return counts;
    }
} 