package service.impl;

import dao.interfaces.BlogDAO;
import dao.impl.BlogDAOImpl;
import service.interfaces.BlogService;
import model.entity.BlogPost;
import model.entity.BlogCategory;
import utils.db.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of BlogService interface
 */
public class BlogServiceImpl implements BlogService {
    private static final Logger LOGGER = Logger.getLogger(BlogServiceImpl.class.getName());
    private final BlogDAO blogDAO;

    public BlogServiceImpl() {
        this.blogDAO = new BlogDAOImpl();
    }

    // Public methods
    @Override
    public List<BlogPost> getAllPublishedPosts() {
        try {
            return blogDAO.getAllPublishedPosts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllPublishedPosts service", e);
            return List.of(); // Return empty list instead of null
        }
    }

    @Override
    public List<BlogPost> getAllPosts() {
        try {
            return blogDAO.getAllPosts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllPosts service", e);
            return List.of(); // Return empty list instead of null
        }
    }

    @Override
    public BlogPost getPostByIdAndIncrementView(int postId) {
        try {
            BlogPost post = blogDAO.getPostById(postId);
            if (post != null && "published".equals(post.getStatus())) {
                // Increment view count
                blogDAO.incrementViewCount(postId);
                // Get updated post with new view count
                post = blogDAO.getPostById(postId);
            }
            return post;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getPostByIdAndIncrementView service", e);
            return null;
        }
    }

    @Override
    public List<BlogPost> getPostsByCategory(String category) {
        try {
            if (category == null || category.trim().isEmpty()) {
                return List.of();
            }
            return blogDAO.getPostsByCategory(category.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getPostsByCategory service", e);
            return List.of();
        }
    }

    @Override
    public List<BlogPost> searchPosts(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return List.of();
            }
            return blogDAO.searchPosts(keyword.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in searchPosts service", e);
            return List.of();
        }
    }

    @Override
    public List<BlogPost> getPostsWithPagination(int page, int pageSize) {
        try {
            if (page < 1) page = 1;
            if (pageSize < 1) pageSize = 10;
            
            int offset = (page - 1) * pageSize;
            return blogDAO.getPostsWithPagination(offset, pageSize);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getPostsWithPagination service", e);
            return List.of();
        }
    }

    @Override
    public int getTotalPages(int pageSize) {
        try {
            if (pageSize < 1) pageSize = 10;
            int totalPosts = blogDAO.getTotalPublishedPosts();
            return (int) Math.ceil((double) totalPosts / pageSize);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getTotalPages service", e);
            return 0;
        }
    }

    @Override
    public int getTotalPostsCount() {
        try {
            return blogDAO.getTotalPublishedPosts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getTotalPostsCount service", e);
            return 0;
        }
    }

    @Override
    public List<BlogPost> getLatestPosts(int limit) {
        try {
            if (limit < 1) limit = 5;
            return blogDAO.getLatestPosts(limit);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getLatestPosts service", e);
            return List.of();
        }
    }

    @Override
    public List<BlogCategory> getAllCategories() {
        try {
            return blogDAO.getAllCategories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllCategories service", e);
            return List.of();
        }
    }    // Admin methods
    @Override
    public boolean createPost(BlogPost post, int adminUserId) {
        try {
            LOGGER.info("Creating post for user ID: " + adminUserId);
            
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot create post");
                return false;
            }
            LOGGER.info("User " + adminUserId + " is confirmed as admin");

            if (post == null || post.getTitle() == null || post.getTitle().trim().isEmpty()) {
                LOGGER.warning("Invalid post data for creation: post=" + post + 
                              (post != null ? ", title=" + post.getTitle() : ""));
                return false;
            }
            LOGGER.info("Post data validation passed: " + post.getTitle());

            post.setAuthorId(adminUserId);
            boolean result = blogDAO.createPost(post);
            LOGGER.info("BlogDAO.createPost result: " + result);
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in createPost service", e);
            return false;
        }
    }

    @Override
    public boolean updatePost(BlogPost post, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot update post");
                return false;
            }

            if (post == null || post.getPostId() <= 0) {
                LOGGER.warning("Invalid post data for update");
                return false;
            }

            return blogDAO.updatePost(post);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updatePost service", e);
            return false;
        }
    }

    @Override
    public boolean deletePost(int postId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot delete post");
                return false;
            }

            if (postId <= 0) {
                LOGGER.warning("Invalid post ID for deletion");
                return false;
            }

            return blogDAO.deletePost(postId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in deletePost service", e);
            return false;
        }
    }

    @Override
    public List<BlogPost> getAllPostsForAdmin(int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot access all posts");
                return List.of();
            }
            return blogDAO.getAllPosts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllPostsForAdmin service", e);
            return List.of();
        }
    }

    @Override
    public BlogPost getPostByIdForAdmin(int postId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot access post for admin");
                return null;
            }
            return blogDAO.getPostById(postId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getPostByIdForAdmin service", e);
            return null;
        }
    }

    @Override
    public boolean publishPost(int postId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot publish post");
                return false;
            }
            return blogDAO.publishPost(postId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in publishPost service", e);
            return false;
        }
    }

    @Override
    public boolean unpublishPost(int postId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot unpublish post");
                return false;
            }
            return blogDAO.unpublishPost(postId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in unpublishPost service", e);
            return false;
        }
    }

    @Override
    public boolean createCategory(BlogCategory category, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot create category");
                return false;
            }

            if (category == null || category.getCategoryName() == null || 
                category.getCategoryName().trim().isEmpty()) {
                LOGGER.warning("Invalid category data for creation");
                return false;
            }

            return blogDAO.createCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in createCategory service", e);
            return false;
        }
    }

    @Override
    public boolean updateCategory(BlogCategory category, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot update category");
                return false;
            }

            if (category == null || category.getCategoryId() <= 0) {
                LOGGER.warning("Invalid category data for update");
                return false;
            }

            return blogDAO.updateCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateCategory service", e);
            return false;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("User " + adminUserId + " is not admin, cannot delete category");
                return false;
            }

            if (categoryId <= 0) {
                LOGGER.warning("Invalid category ID for deletion");
                return false;
            }            return blogDAO.deleteCategory(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in deleteCategory service", e);
            return false;
        }
    }

    @Override
    public boolean activateCategory(int categoryId, int adminUserId) {
        try {
            // Check if user is admin
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user " + adminUserId + " attempted to activate category");
                return false;
            }

            return blogDAO.activateCategory(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in activateCategory service", e);
            return false;
        }
    }

    @Override
    public boolean deactivateCategory(int categoryId, int adminUserId) {
        try {
            // Check if user is admin
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user " + adminUserId + " attempted to deactivate category");
                return false;
            }

            return blogDAO.deactivateCategory(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in deactivateCategory service", e);
            return false;
        }
    }

    @Override    public boolean isUserAdmin(int userId) {
        String sql = "SELECT role FROM Users WHERE user_id = ? AND is_deleted = 0";
        
        LOGGER.info("Checking if user ID " + userId + " is admin");
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    boolean isAdmin = "admin".equalsIgnoreCase(role);
                    LOGGER.info("User ID " + userId + " has role: " + role + ", isAdmin: " + isAdmin);
                    return isAdmin;
                } else {
                    LOGGER.warning("User ID " + userId + " not found or is deleted");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user is admin: " + userId, e);
        }
        return false;
    }

    @Override
    public List<BlogPost> getPostsByCategoryId(int categoryId) {
        try {
            if (categoryId <= 0) {
                return List.of();
            }
            return blogDAO.getPostsByCategoryId(categoryId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getPostsByCategoryId service", e);
            return List.of();
        }
    }
}
