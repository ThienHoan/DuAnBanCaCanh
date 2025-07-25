package dao.impl.pReview;

import model.entity.pReview.ReviewImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.pReview.Review;
import utils.db.DBContext;

public class ReviewImageDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    // Get review images by list of reviews
public List<ReviewImage> getReviewImagesByReviewList(List<Review> reviews) {
    List<ReviewImage> reviewImages = new ArrayList<>();
    
    if (reviews == null || reviews.isEmpty()) {
        return reviewImages;
    }
    
    // Tạo chuỗi placeholder cho IN clause
    StringBuilder placeholders = new StringBuilder();
    for (int i = 0; i < reviews.size(); i++) {
        if (i > 0) {
            placeholders.append(",");
        }
        placeholders.append("?");
    }
    
    String query = "SELECT * FROM Review_images WHERE review_id IN (" + placeholders.toString() + ") AND is_deleted = 0 ORDER BY review_id ASC, image_id ASC";
    
    try {
        conn = DBContext.getConnection();
        ps = conn.prepareStatement(query);
        
        // Set parameters
        for (int i = 0; i < reviews.size(); i++) {
            ps.setInt(i + 1, reviews.get(i).getReviewId());
        }
        
        rs = ps.executeQuery();
        while (rs.next()) {
            ReviewImage reviewImage = createReviewImageFromResultSet(rs);
            reviewImages.add(reviewImage);
        }
    } catch (SQLException e) {
        // Log the error
        e.printStackTrace();
    } finally {
        closeResources();
    }
    
    return reviewImages;
}

    // Get all review images
    public List<ReviewImage> getAllReviewImages() {
        List<ReviewImage> reviewImages = new ArrayList<>();
        String query = "SELECT * FROM Review_images ORDER BY image_id ASC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReviewImage reviewImage = createReviewImageFromResultSet(rs);
                reviewImages.add(reviewImage);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImages;
    }

    // Get review image by ID
    public ReviewImage getReviewImageById(int imageId) {
        ReviewImage reviewImage = null;
        String query = "SELECT * FROM Review_images WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, imageId);
            rs = ps.executeQuery();
            if (rs.next()) {
                reviewImage = createReviewImageFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImage;
    }

    // Get review images by review ID
    public List<ReviewImage> getReviewImagesByReviewId(int reviewId) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        String query = "SELECT * FROM Review_images WHERE review_id = ? AND is_deleted = 0 ORDER BY image_id ASC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReviewImage reviewImage = createReviewImageFromResultSet(rs);
                reviewImages.add(reviewImage);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImages;
    }

    // Get active review images by review ID
    public List<ReviewImage> getActiveReviewImagesByReviewId(int reviewId) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        String query = "SELECT * FROM Review_images WHERE review_id = ? AND is_deleted = 0 ORDER BY image_id ASC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReviewImage reviewImage = createReviewImageFromResultSet(rs);
                reviewImages.add(reviewImage);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImages;
    }

    // Get deleted review images by review ID
    public List<ReviewImage> getDeletedReviewImagesByReviewId(int reviewId) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        String query = "SELECT * FROM Review_images WHERE review_id = ? AND is_deleted = 1 ORDER BY image_id ASC";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReviewImage reviewImage = createReviewImageFromResultSet(rs);
                reviewImages.add(reviewImage);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImages;
    }

    // Get review image by URL
    public ReviewImage getReviewImageByUrl(String imageUrl) {
        ReviewImage reviewImage = null;
        String query = "SELECT * FROM Review_images WHERE image_url = ? AND is_deleted = 0";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, imageUrl);
            rs = ps.executeQuery();
            if (rs.next()) {
                reviewImage = createReviewImageFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reviewImage;
    }

    // Insert new review image
    public boolean insertReviewImage(ReviewImage reviewImage) {
        String query = "INSERT INTO Review_images (review_id, image_url, is_deleted) VALUES (?, ?, ?)";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, reviewImage.getReviewId());
            ps.setString(2, reviewImage.getImageUrl());
            ps.setObject(3, reviewImage.getIsDeleted() != null ? reviewImage.getIsDeleted() : 0);

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

    // Insert multiple review images
    public boolean insertReviewImages(List<ReviewImage> reviewImages) {
        String query = "INSERT INTO Review_images (review_id, image_url, is_deleted) VALUES (?, ?, ?)";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            
            for (ReviewImage reviewImage : reviewImages) {
                ps.setObject(1, reviewImage.getReviewId());
                ps.setString(2, reviewImage.getImageUrl());
                ps.setObject(3, reviewImage.getIsDeleted() != null ? reviewImage.getIsDeleted() : 0);
                ps.addBatch();
            }
            
            int[] rowsAffected = ps.executeBatch();
            return rowsAffected.length > 0;
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Update existing review image
    public boolean updateReviewImage(ReviewImage reviewImage) {
        String query = "UPDATE Review_images SET review_id = ?, image_url = ?, is_deleted = ? WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setObject(1, reviewImage.getReviewId());
            ps.setString(2, reviewImage.getImageUrl());
            ps.setObject(3, reviewImage.getIsDeleted());
            ps.setInt(4, reviewImage.getImageId());

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

    // Update review image URL
    public boolean updateReviewImageUrl(int imageId, String newImageUrl) {
        String query = "UPDATE Review_images SET image_url = ? WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newImageUrl);
            ps.setInt(2, imageId);

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

    // Soft delete review image (set is_deleted = 1)
    public boolean softDeleteReviewImage(int imageId) {
        String query = "UPDATE Review_images SET is_deleted = 1 WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, imageId);

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

    // Soft delete all review images by review ID
    public boolean softDeleteReviewImagesByReviewId(int reviewId) {
        String query = "UPDATE Review_images SET is_deleted = 1 WHERE review_id = ?";
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

    // Restore review image (set is_deleted = 0)
    public boolean restoreReviewImage(int imageId) {
        String query = "UPDATE Review_images SET is_deleted = 0 WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, imageId);

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

    // Hard delete review image
    public boolean deleteReviewImage(int imageId) {
        String query = "DELETE FROM Review_images WHERE image_id = ?";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, imageId);

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

    // Hard delete all review images by review ID
    public boolean deleteReviewImagesByReviewId(int reviewId) {
        String query = "DELETE FROM Review_images WHERE review_id = ?";
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

    // Get count of images for a review
    public int getImageCountByReviewId(int reviewId) {
        int count = 0;
        String query = "SELECT COUNT(*) as image_count FROM Review_images WHERE review_id = ? AND is_deleted = 0";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, reviewId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("image_count");
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    // Check if image exists by URL
    public boolean isImageUrlExists(String imageUrl) {
        boolean exists = false;
        String query = "SELECT COUNT(*) as count FROM Review_images WHERE image_url = ? AND is_deleted = 0";
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, imageUrl);
            rs = ps.executeQuery();
            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            // Log the error
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exists;
    }

    // Helper method to create ReviewImage object from ResultSet
    private ReviewImage createReviewImageFromResultSet(ResultSet rs) throws SQLException {
    ReviewImage reviewImage = new ReviewImage();
    reviewImage.setImageId(rs.getInt("image_id"));
    reviewImage.setReviewId((Integer) rs.getObject("review_id"));
    reviewImage.setImageUrl(rs.getString("image_url"));
    
    // Xử lý bit field - phải dùng Boolean từ ResultSet rồi convert sang Integer
    Boolean isDeleted = (Boolean) rs.getObject("is_deleted");
    reviewImage.setIsDeleted(isDeleted != null ? (isDeleted ? 1 : 0) : null);
    
    return reviewImage;
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