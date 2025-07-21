package dao.impl;

import dao.interfaces.BlogDAO;
import model.entity.BlogPost;
import model.entity.BlogCategory;
import utils.db.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of BlogDAO interface
 */
public class BlogDAOImpl implements BlogDAO {

    private static final Logger LOGGER = Logger.getLogger(BlogDAOImpl.class.getName());    // Blog Posts methods

    @Override
    public List<BlogPost> getAllPublishedPosts() {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                posts.add(mapResultSetToBlogPost(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all published posts", e);
        }
        return posts;
    }

    @Override
    public BlogPost getPostById(int postId) {
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.post_id = ? AND bp.is_deleted = 0
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBlogPost(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting post by ID: " + postId, e);
        }
        return null;
    }

    @Override
    public List<BlogPost> getPostsByCategory(String category) {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE (bc.category_name = ? OR bc.slug = ?) AND bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToBlogPost(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting posts by category: " + category, e);
        }
        return posts;
    }

    @Override
    public List<BlogPost> getPostsByCategoryId(int categoryId) {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.category_id = ? AND bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToBlogPost(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting posts by category ID: " + categoryId, e);
        }
        return posts;
    }

    @Override
    public List<BlogPost> searchPosts(String keyword) {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE (bp.title LIKE ? OR bp.content LIKE ? OR bp.summary LIKE ? OR bp.tags LIKE ?)
                  AND bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToBlogPost(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching posts with keyword: " + keyword, e);
        }
        return posts;
    }

    @Override
    public List<BlogPost> getPostsWithPagination(int offset, int limit) {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, offset);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToBlogPost(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting posts with pagination", e);
        }
        return posts;
    }

    @Override
    public int getTotalPublishedPosts() {
        String sql = "SELECT COUNT(*) FROM blog_posts WHERE status = 'published' AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total published posts count", e);
        }
        return 0;
    }

    @Override
    public boolean incrementViewCount(int postId) {
        String sql = "UPDATE blog_posts SET view_count = view_count + 1 WHERE post_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error incrementing view count for post: " + postId, e);
            return false;
        }
    }

    @Override
    public List<BlogPost> getLatestPosts(int limit) {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT TOP (?) bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.status = 'published' AND bp.is_deleted = 0
            ORDER BY bp.published_at DESC, bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToBlogPost(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting latest posts", e);
        }
        return posts;
    }    // Admin methods

    @Override
    public boolean createPost(BlogPost post) {
        String sql = """
            INSERT INTO blog_posts (title, content, summary, featured_image, author_id, 
                                   category_id, tags, status, published_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getSummary());
            stmt.setString(4, post.getFeaturedImage());
            stmt.setInt(5, post.getAuthorId());
            stmt.setInt(6, post.getCategoryId()); // Changed from category to categoryId
            stmt.setString(7, post.getTags());
            stmt.setString(8, post.getStatus());

            if ("published".equals(post.getStatus())) {
                stmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("BlogDAO createPost - rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating post: " + post.getTitle()
                    + ", categoryId: " + post.getCategoryId(), e);
            return false;
        }
    }

    @Override
    public boolean updatePost(BlogPost post) {
        String sql = """
            UPDATE blog_posts 
            SET title = ?, content = ?, summary = ?, featured_image = ?, 
                category_id = ?, tags = ?, status = ?, updated_at = GETDATE(),
                published_at = CASE 
                    WHEN status != 'published' AND ? = 'published' THEN GETDATE()
                    WHEN ? != 'published' THEN NULL
                    ELSE published_at
                END
            WHERE post_id = ?
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getSummary());
            stmt.setString(4, post.getFeaturedImage());
            stmt.setInt(5, post.getCategoryId()); // Changed from category to categoryId
            stmt.setString(6, post.getTags());
            stmt.setString(7, post.getStatus());
            stmt.setString(8, post.getStatus()); // For CASE condition
            stmt.setString(9, post.getStatus()); // For CASE condition
            stmt.setInt(10, post.getPostId());

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("BlogDAO updatePost - rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating post: " + post.getTitle()
                    + ", categoryId: " + post.getCategoryId(), e);
            return false;
        }
    }

    @Override
    public boolean deletePost(int postId) {
        String sql = "UPDATE blog_posts SET is_deleted = 1 WHERE post_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting post: " + postId, e);
            return false;
        }
    }

    @Override
    public List<BlogPost> getAllPosts() {
        List<BlogPost> posts = new ArrayList<>();
        String sql = """
            SELECT bp.post_id, bp.title, bp.content, bp.summary, bp.featured_image, 
                   bp.author_id, ISNULL(u.full_name, u.username) as author_name, 
                   bp.category_id, bp.tags, bp.status, bp.view_count, 
                   bp.created_at, bp.updated_at, bp.published_at, bp.is_deleted,
                   bc.category_name, bc.slug as category_slug, bc.is_active as category_active
            FROM blog_posts bp
            LEFT JOIN Users u ON bp.author_id = u.user_id
            LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
            WHERE bp.is_deleted = 0
            ORDER BY bp.created_at DESC
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                posts.add(mapResultSetToBlogPost(rs));
            }
            LOGGER.info("getAllPosts returned " + posts.size() + " posts");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all posts", e);
        }
        return posts;
    }

    @Override
    public boolean publishPost(int postId) {
        String sql = "UPDATE blog_posts SET status = 'published', published_at = GETDATE() WHERE post_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error publishing post: " + postId, e);
            return false;
        }
    }

    @Override
    public boolean unpublishPost(int postId) {
        String sql = "UPDATE blog_posts SET status = 'draft', published_at = NULL WHERE post_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error unpublishing post: " + postId, e);
            return false;
        }
    }    // Categories methods

    @Override
    public List<BlogCategory> getAllCategories() {
        List<BlogCategory> categories = new ArrayList<>();

        // First try with is_active column
        String sql = """
            SELECT category_id, category_name, description, slug, created_at, is_deleted, 
                   ISNULL(is_active, 1) as is_active 
            FROM blog_categories WHERE is_deleted = 0 ORDER BY category_name
            """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToBlogCategory(rs));
            }
        } catch (SQLException e) {
            // If is_active column doesn't exist, try without it
            LOGGER.log(Level.WARNING, "Error with is_active column, trying fallback query", e);

            String fallbackSql = """
                SELECT category_id, category_name, description, slug, created_at, is_deleted, 
                       1 as is_active 
                FROM blog_categories WHERE is_deleted = 0 ORDER BY category_name
                """;

            try (Connection conn2 = DBContext.getConnection(); PreparedStatement stmt2 = conn2.prepareStatement(fallbackSql); ResultSet rs2 = stmt2.executeQuery()) {

                while (rs2.next()) {
                    categories.add(mapResultSetToBlogCategory(rs2));
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Error getting all categories", e2);
            }
        }
        return categories;
    }

    @Override
    public BlogCategory getCategoryById(int categoryId) {
        String sql = "SELECT category_id, category_name, description, slug, created_at, is_deleted, "
                + "ISNULL(is_active, 1) as is_active "
                + "FROM blog_categories WHERE category_id = ? AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBlogCategory(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category by ID: " + categoryId, e);
        }
        return null;
    }

    @Override
    public boolean createCategory(BlogCategory category) {
        String sql = "INSERT INTO blog_categories (category_name, description, slug, created_at, is_deleted, is_active) VALUES (?, ?, ?, GETDATE(), 0, ?)";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getSlug());
            stmt.setBoolean(4, category.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCategoryId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating category: " + category.getCategoryName(), e);
        }
        return false;
    }

    @Override
    public boolean updateCategory(BlogCategory category) {
        String sql = "UPDATE blog_categories SET category_name = ?, description = ?, slug = ?, is_active = ? WHERE category_id = ? AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getSlug());
            stmt.setBoolean(4, category.isActive());
            stmt.setInt(5, category.getCategoryId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating category: " + category.getCategoryId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        String sql = "UPDATE blog_categories SET is_deleted = 1 WHERE category_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting category: " + categoryId, e);
            return false;
        }
    }

    @Override
    public boolean activateCategory(int categoryId) {
        String sql = "UPDATE blog_categories SET is_active = 1 WHERE category_id = ? AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error activating category: " + categoryId, e);
            return false;
        }
    }

    @Override
    public boolean deactivateCategory(int categoryId) {
        String sql = "UPDATE blog_categories SET is_active = 0 WHERE category_id = ? AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deactivating category: " + categoryId, e);
            return false;
        }
    }
    // Helper methods

    private BlogPost mapResultSetToBlogPost(ResultSet rs) throws SQLException {
        BlogPost post = new BlogPost();
        post.setPostId(rs.getInt("post_id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setSummary(rs.getString("summary"));
        post.setFeaturedImage(rs.getString("featured_image"));
        post.setAuthorId(rs.getInt("author_id"));
        post.setAuthorName(rs.getString("author_name"));

        // Handle category relationship
        try {
            int categoryId = rs.getInt("category_id");
            if (!rs.wasNull()) {
                post.setCategoryId(categoryId);

                // Create category object if we have the data
                try {
                    String categoryName = rs.getString("category_name");
                    if (categoryName != null) {
                        BlogCategory categoryObj = new BlogCategory();
                        categoryObj.setCategoryId(categoryId);
                        categoryObj.setCategoryName(categoryName);
                        categoryObj.setSlug(rs.getString("category_slug"));
                        categoryObj.setActive(rs.getBoolean("category_active"));
                        post.setCategoryObject(categoryObj);
                    }
                } catch (SQLException e) {
                    // Category columns not available in this query, skip
                }
            }
        } catch (SQLException e) {
            // category_id column not available in this query, skip
        }

        post.setTags(rs.getString("tags"));
        post.setStatus(rs.getString("status"));
        post.setViewCount(rs.getInt("view_count"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setUpdatedAt(rs.getTimestamp("updated_at"));
        post.setPublishedAt(rs.getTimestamp("published_at"));
        post.setDeleted(rs.getBoolean("is_deleted"));
        return post;
    }

    private BlogCategory mapResultSetToBlogCategory(ResultSet rs) throws SQLException {
        BlogCategory category = new BlogCategory();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setSlug(rs.getString("slug"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setDeleted(rs.getBoolean("is_deleted"));
        category.setActive(rs.getBoolean("is_active"));
        return category;
    }
}
