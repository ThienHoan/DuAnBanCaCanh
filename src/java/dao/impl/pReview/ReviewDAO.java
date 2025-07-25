package dao.impl.pReview;

import model.entity.pReview.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utils.db.DBContext;

public class ReviewDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    
    
    
    // Get all reviews by product ID (only non-deleted reviews)
public List<Review> getAllReviewsByProductIdForCus(int productId) {
    List<Review> reviews = new ArrayList<>();
    String query = "SELECT * FROM RReviews WHERE product_id = ? AND is_deleted = 0 ORDER BY review_date DESC";
    try {
        conn = DBContext.getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, productId);
        rs = ps.executeQuery();
        while (rs.next()) {
            Review review = createReviewFromResultSet(rs);
            reviews.add(review);
        }
    } catch (SQLException e) {
        // Log the error
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return reviews;
}
    
    
    public int insertReviewAndGetId(Review review) {
        String query = "INSERT INTO Reviews (product_id, user_id, order_id, rating, comment, review_date, status, is_verified_purchase, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setObject(1, review.getProductId());
            ps.setObject(2, review.getUserId());
            ps.setObject(3, review.getOrderId());
            ps.setObject(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setTimestamp(6, review.getReviewDate() != null ? 
                Timestamp.valueOf(review.getReviewDate()) : 
                Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, review.getStatus() != null ? review.getStatus() : "pending");
            ps.setObject(8, review.getIsVerifiedPurchase() != null ? review.getIsVerifiedPurchase() : 0);
            ps.setObject(9, review.getIsDeleted() != null ? review.getIsDeleted() : 0);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return -1; // Indicate failure
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return -1;
        } finally {
            closeResources();
        }
    }
    
    // Get all reviews
    public List<Review> getAllReviewsForCus() {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE is_deleted = 0 ORDER BY review_id DESC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review review = createReviewFromResultSet(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviews;
    }

    // Get review by ID
    public Review getReviewById(int reviewId) {
        Review review = null;
        String query = "SELECT * FROM Reviews WHERE review_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);
            rs = ps.executeQuery();
            if (rs.next()) {
                review = createReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return review;
    }

    // Get reviews by product ID
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE product_id = ? AND is_deleted = 0 ORDER BY review_date DESC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review review = createReviewFromResultSet(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviews;
    }

    // Get reviews by user ID
    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE user_id = ? AND is_deleted = 0 ORDER BY review_date DESC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review review = createReviewFromResultSet(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviews;
    }

    // Get reviews by status
    public List<Review> getReviewsByStatus(String status) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE status = ? AND is_deleted = 0 ORDER BY review_date DESC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review review = createReviewFromResultSet(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviews;
    }

    // Get reviews by rating
    public List<Review> getReviewsByRating(int rating) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE rating = ? AND is_deleted = 0 ORDER BY review_date DESC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, rating);
            rs = ps.executeQuery();
            while (rs.next()) {
                Review review = createReviewFromResultSet(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviews;
    }

    // Insert new review
    public boolean insertReview(Review review) {
        String query = "INSERT INTO Reviews (product_id, user_id, order_id, rating, comment, review_date, status, is_verified_purchase, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, review.getProductId());
            ps.setObject(2, review.getUserId());
            ps.setObject(3, review.getOrderId());
            ps.setObject(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setTimestamp(6, review.getReviewDate() != null ? 
                Timestamp.valueOf(review.getReviewDate()) : 
                Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, review.getStatus() != null ? review.getStatus() : "pending");
            ps.setObject(8, review.getIsVerifiedPurchase() != null ? review.getIsVerifiedPurchase() : 0);
            ps.setObject(9, review.getIsDeleted() != null ? review.getIsDeleted() : 0);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Update existing review
    public boolean updateReview(Review review) {
        String query = "UPDATE Reviews SET product_id = ?, user_id = ?, order_id = ?, rating = ?, comment = ?, review_date = ?, status = ?, is_verified_purchase = ?, is_deleted = ? WHERE review_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, review.getProductId());
            ps.setObject(2, review.getUserId());
            ps.setObject(3, review.getOrderId());
            ps.setObject(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setTimestamp(6, review.getReviewDate() != null ? 
                Timestamp.valueOf(review.getReviewDate()) : null);
            ps.setString(7, review.getStatus());
            ps.setObject(8, review.getIsVerifiedPurchase());
            ps.setObject(9, review.getIsDeleted());
            ps.setInt(10, review.getReviewId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Soft delete review (set is_deleted = 1)
    public boolean softDeleteReview(int reviewId) {
        String query = "UPDATE Reviews SET is_deleted = 1 WHERE review_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Hard delete review
    public boolean deleteReview(int reviewId) {
        String query = "DELETE FROM Reviews WHERE review_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Update review status
    public boolean updateReviewStatus(int reviewId, String status) {
        String query = "UPDATE Reviews SET status = ? WHERE review_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, reviewId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Get average rating for a product
    public double getAverageRatingByProductId(int productId) {
        double averageRating = 0.0;
        String query = "SELECT AVG(CAST(rating AS DECIMAL)) as avg_rating FROM Reviews WHERE product_id = ? AND is_deleted = 0 AND status = 'approved'";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                averageRating = rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return averageRating;
    }

    // Get review count for a product
    public int getReviewCountByProductId(int productId) {
        int count = 0;
        String query = "SELECT COUNT(*) as review_count FROM Reviews WHERE product_id = ? AND is_deleted = 0 AND status = 'approved'";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("review_count");
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    // Helper method to create Review object from ResultSet
   private Review createReviewFromResultSet(ResultSet rs) throws SQLException {
    Review review = new Review();
    review.setReviewId(rs.getInt("review_id"));
    review.setProductId((Integer) rs.getObject("product_id"));
    review.setUserId((Integer) rs.getObject("user_id"));
    review.setOrderId((Integer) rs.getObject("order_id"));
    review.setRating((Integer) rs.getObject("rating"));
    review.setComment(rs.getString("comment"));
    
    Timestamp timestamp = rs.getTimestamp("review_date");
    if (timestamp != null) {
        review.setReviewDate(timestamp.toLocalDateTime());
    }
    
    review.setStatus(rs.getString("status"));
    
    // Xử lý bit fields - phải dùng Boolean từ ResultSet rồi convert sang Integer
    Boolean isVerified = (Boolean) rs.getObject("is_verified_purchase");
    review.setIsVerifiedPurchase(isVerified != null ? (isVerified ? 1 : 0) : null);
    
    Boolean isDeleted = (Boolean) rs.getObject("is_deleted");
    review.setIsDeleted(isDeleted != null ? (isDeleted ? 1 : 0) : null);
    
    return review;
}
    // Helper method to close resources
    private void closeResources() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}