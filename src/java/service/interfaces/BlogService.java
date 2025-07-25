package service.interfaces;

import model.entity.BlogPost;
import model.entity.BlogCategory;
import java.util.List;

/**
 * Interface for Blog Service Layer
 */
public interface BlogService {
    
    // Public methods (cho mọi người đọc blog)
    /**
     * Lấy tất cả bài viết đã published
     */
    List<BlogPost> getAllPublishedPosts();
    
    /**
     * Lấy tất cả bài viết (bao gồm cả draft và published)
     */
    List<BlogPost> getAllPosts();
    
    /**
     * Lấy bài viết theo ID và tăng view count
     */
    BlogPost getPostByIdAndIncrementView(int postId);
    
    /**
     * Lấy bài viết theo category ID
     */
    List<BlogPost> getPostsByCategoryId(int categoryId);
    
    /**
     * Lấy bài viết theo category
     */
    List<BlogPost> getPostsByCategory(String category);
    
    /**
     * Tìm kiếm bài viết
     */
    List<BlogPost> searchPosts(String keyword);
    
    /**
     * Lấy bài viết với phân trang
     */
    List<BlogPost> getPostsWithPagination(int page, int pageSize);
    
    /**
     * Lấy tổng số trang
     */
    int getTotalPages(int pageSize);
    
    /**
     * Lấy tổng số bài viết đã published
     */
    int getTotalPostsCount();
    
    /**
     * Lấy bài viết mới nhất
     */
    List<BlogPost> getLatestPosts(int limit);
    
    /**
     * Lấy tất cả danh mục
     */
    List<BlogCategory> getAllCategories();
    
    // Admin methods (chỉ admin mới được dùng)
    /**
     * Tạo bài viết mới (admin only)
     */
    boolean createPost(BlogPost post, int adminUserId);
    
    /**
     * Cập nhật bài viết (admin only)
     */
    boolean updatePost(BlogPost post, int adminUserId);
    
    /**
     * Xóa bài viết (admin only)
     */
    boolean deletePost(int postId, int adminUserId);
    
    /**
     * Lấy tất cả bài viết cho admin (bao gồm draft)
     */
    List<BlogPost> getAllPostsForAdmin(int adminUserId);
    
    /**
     * Lấy bài viết theo ID cho admin (không tăng view)
     */
    BlogPost getPostByIdForAdmin(int postId, int adminUserId);
    
    /**
     * Publish bài viết
     */
    boolean publishPost(int postId, int adminUserId);
    
    /**
     * Unpublish bài viết
     */
    boolean unpublishPost(int postId, int adminUserId);
    
    /**
     * Tạo danh mục mới
     */
    boolean createCategory(BlogCategory category, int adminUserId);
    
    /**
     * Cập nhật danh mục
     */
    boolean updateCategory(BlogCategory category, int adminUserId);
      /**
     * Xóa danh mục
     */
    boolean deleteCategory(int categoryId, int adminUserId);
    
    /**
     * Kích hoạt danh mục
     */
    boolean activateCategory(int categoryId, int adminUserId);
    
    /**
     * Ẩn danh mục
     */
    boolean deactivateCategory(int categoryId, int adminUserId);
    
    /**
     * Kiểm tra user có phải admin không
     */
    boolean isUserAdmin(int userId);
}
