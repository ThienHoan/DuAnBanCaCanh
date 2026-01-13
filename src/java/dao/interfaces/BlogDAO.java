package dao.interfaces;

import model.entity.BlogPost;
import model.entity.BlogCategory;
import java.util.List;

/**
 * Interface for Blog Data Access Object
 */
public interface BlogDAO {
    
    // Blog Posts methods
    /**
     * Lấy tất cả bài viết đã published (status = 'published')
     */
    List<BlogPost> getAllPublishedPosts();
    
    /**
     * Lấy bài viết theo ID
     */
    BlogPost getPostById(int postId);
      /**
     * Lấy bài viết theo category ID
     */
    List<BlogPost> getPostsByCategoryId(int categoryId);
    
    /**
     * Lấy bài viết theo category name (for backward compatibility)
     */
    List<BlogPost> getPostsByCategory(String category);
    
    /**
     * Tìm kiếm bài viết theo từ khóa
     */
    List<BlogPost> searchPosts(String keyword);
    
    /**
     * Lấy danh sách bài viết với phân trang
     */
    List<BlogPost> getPostsWithPagination(int offset, int limit);
    
    /**
     * Lấy tổng số bài viết published
     */
    int getTotalPublishedPosts();
    
    /**
     * Tăng lượt xem bài viết
     */
    boolean incrementViewCount(int postId);
    
    /**
     * Lấy bài viết mới nhất
     */
    List<BlogPost> getLatestPosts(int limit);
    
    // Admin methods (chỉ admin mới được dùng)
    /**
     * Tạo bài viết mới
     */
    boolean createPost(BlogPost post);
    
    /**
     * Cập nhật bài viết
     */
    boolean updatePost(BlogPost post);
    
    /**
     * Xóa bài viết (soft delete)
     */
    boolean deletePost(int postId);
    
    /**
     * Lấy tất cả bài viết (bao gồm draft, admin dùng)
     */
    List<BlogPost> getAllPosts();
    
    /**
     * Publish bài viết
     */
    boolean publishPost(int postId);
    
    /**
     * Unpublish bài viết (chuyển về draft)
     */
    boolean unpublishPost(int postId);
    
    // Categories methods
    /**
     * Lấy tất cả danh mục
     */
    List<BlogCategory> getAllCategories();
    
    /**
     * Lấy danh mục theo ID
     */
    BlogCategory getCategoryById(int categoryId);
    
    /**
     * Tạo danh mục mới
     */
    boolean createCategory(BlogCategory category);
    
    /**
     * Cập nhật danh mục
     */
    boolean updateCategory(BlogCategory category);
      /**
     * Xóa danh mục
     */
    boolean deleteCategory(int categoryId);
    
    /**
     * Kích hoạt danh mục
     */
    boolean activateCategory(int categoryId);
    
    /**
     * Ẩn danh mục
     */
    boolean deactivateCategory(int categoryId);
}
