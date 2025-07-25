package controller.admin;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogPost;
import model.entity.BlogCategory;
import model.entity.User;
import utils.ConfigUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin Blog Management Controller
 * URL patterns: /admin-blogs, /admin-blog-form, /admin-blog-categories, /admin-blog-action
 */
// @WebServlet(urlPatterns = {"/admin-blog", "/admin-blog-form", "/admin-blog-categories", "/admin-blog-action"})
public class AdminBlogController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminBlogController.class.getName());
    private BlogService blogService;

    @Override
    public void init() throws ServletException {
        super.init();
        blogService = new BlogServiceImpl();
        LOGGER.info("AdminBlogController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check admin authentication
        if (!isAdminAuthenticated(request, response)) {
            return;
        }

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        LOGGER.info("AdminBlogController handling GET request: " + path);        try {
            switch (path) {
                case "/admin-blogs":
                    handleBlogList(request, response);
                    break;
                case "/admin-blog-form":
                    handleBlogForm(request, response);
                    break;
                case "/admin-blog-categories":
                    handleCategoriesList(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AdminBlogController GET", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check admin authentication
        if (!isAdminAuthenticated(request, response)) {
            return;
        }

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        LOGGER.info("AdminBlogController handling POST request: " + path);

        try {
            if ("/admin-blog-action".equals(path)) {
                handleBlogAction(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AdminBlogController POST", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if user is admin
     */
    private boolean isAdminAuthenticated(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin role required.");
            return false;
        }

        return true;
    }

    /**
     * Handle blog list page - hiển thị tất cả bài viết cho admin
     */
    private void handleBlogList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            // Get all posts for admin (including draft)
            List<BlogPost> posts = blogService.getAllPostsForAdmin(user.getUserId());
            
            // Get categories
            List<BlogCategory> categories = blogService.getAllCategories();

            // Set attributes
            request.setAttribute("posts", posts);
            request.setAttribute("categories", categories);

            LOGGER.info("Admin blog list loaded: " + posts.size() + " posts");

            // Forward to admin blog JSP
            request.getRequestDispatcher("/admin/blog-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling admin blog list", e);
            request.setAttribute("errorMessage", "Lỗi tải danh sách bài viết: " + e.getMessage());
            request.getRequestDispatcher("/admin/blog-list.jsp").forward(request, response);
        }
    }

    /**
     * Handle blog form - tạo/chỉnh sửa bài viết
     */    private void handleBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            String action = request.getParameter("action");
            String postIdParam = request.getParameter("id");

            BlogPost post = null;
            if ("edit".equals(action) && postIdParam != null) {
                try {
                    int postId = Integer.parseInt(postIdParam);
                    post = blogService.getPostByIdForAdmin(postId, user.getUserId());
                    if (post == null) {
                        request.setAttribute("errorMessage", "Bài viết không tồn tại hoặc bạn không có quyền chỉnh sửa.");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "ID bài viết không hợp lệ.");
                }
            }

            // Get categories for dropdown
            List<BlogCategory> categories = blogService.getAllCategories();

            // Set attributes
            request.setAttribute("post", post);
            request.setAttribute("categories", categories);
            request.setAttribute("action", action);
            
            // Add TinyMCE configuration
            request.setAttribute("tinyMCEApiKey", ConfigUtil.getTinyMCEApiKey());
            request.setAttribute("tinyMCEVersion", ConfigUtil.getTinyMCEVersion());
            request.setAttribute("maxUploadSize", ConfigUtil.getMaxUploadSize());
            request.setAttribute("allowedUploadTypes", ConfigUtil.getAllowedUploadTypes());

            LOGGER.info("Admin blog form loaded for action: " + action);

            // Forward to form JSP
            request.getRequestDispatcher("/admin/blog-form.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling admin blog form", e);
            request.setAttribute("errorMessage", "Lỗi tải form bài viết: " + e.getMessage());
            request.getRequestDispatcher("/admin/blog-form.jsp").forward(request, response);
        }
    }

    /**
     * Handle categories list
     */
    private void handleCategoriesList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get all categories
            List<BlogCategory> categories = blogService.getAllCategories();

            // Set attributes
            request.setAttribute("categories", categories);

            LOGGER.info("Admin categories list loaded: " + categories.size() + " categories");

            // Forward to categories JSP
            request.getRequestDispatcher("/admin/blog-categories.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling admin categories list", e);
            request.setAttribute("errorMessage", "Lỗi tải danh sách danh mục: " + e.getMessage());
            request.getRequestDispatcher("/admin/blog-categories.jsp").forward(request, response);
        }
    }

    /**
     * Handle blog actions (create, update, delete, publish, unpublish)
     */
    private void handleBlogAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            String action = request.getParameter("action");
            String redirectUrl = request.getContextPath() + "/admin-blog";

            switch (action) {
                case "create":
                    handleCreatePost(request, user);
                    request.getSession().setAttribute("successMessage", "Bài viết đã được tạo thành công!");
                    break;
                    
                case "update":
                    handleUpdatePost(request, user);
                    request.getSession().setAttribute("successMessage", "Bài viết đã được cập nhật thành công!");
                    break;
                    
                case "delete":
                    handleDeletePost(request, user);
                    request.getSession().setAttribute("successMessage", "Bài viết đã được xóa thành công!");
                    break;
                    
                case "publish":
                    handlePublishPost(request, user);
                    request.getSession().setAttribute("successMessage", "Bài viết đã được xuất bản!");
                    break;
                    
                case "unpublish":
                    handleUnpublishPost(request, user);
                    request.getSession().setAttribute("successMessage", "Bài viết đã được ẩn!");
                    break;
                      case "create_category":
                    handleCreateCategory(request, user);
                    redirectUrl = request.getContextPath() + "/admin-blog-categories";
                    request.getSession().setAttribute("successMessage", "Danh mục đã được tạo thành công!");
                    break;
                    
                case "update_category":
                    handleUpdateCategory(request, user);
                    redirectUrl = request.getContextPath() + "/admin-blog-categories";
                    request.getSession().setAttribute("successMessage", "Danh mục đã được cập nhật thành công!");
                    break;
                    
                case "delete_category":
                    handleDeleteCategory(request, user);
                    redirectUrl = request.getContextPath() + "/admin-blog-categories";
                    request.getSession().setAttribute("successMessage", "Danh mục đã được xóa thành công!");
                    break;
                    
                case "activate_category":
                    handleActivateCategory(request, user);
                    redirectUrl = request.getContextPath() + "/admin-blog-categories";
                    request.getSession().setAttribute("successMessage", "Danh mục đã được kích hoạt!");
                    break;
                    
                case "deactivate_category":
                    handleDeactivateCategory(request, user);
                    redirectUrl = request.getContextPath() + "/admin-blog-categories";
                    request.getSession().setAttribute("successMessage", "Danh mục đã được ẩn!");
                    break;
                    
                default:
                    request.getSession().setAttribute("errorMessage", "Hành động không hợp lệ.");
                    break;
            }

            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling blog action", e);
            request.getSession().setAttribute("errorMessage", "Lỗi thực hiện hành động: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-blog");
        }
    }    private void handleCreatePost(HttpServletRequest request, User user) {
        LOGGER.info("Creating new post for user: " + user.getUserId());
        
        BlogPost post = new BlogPost();
        populatePostFromRequest(post, request);
        post.setAuthorId(user.getUserId());
        
        LOGGER.info("Post details: title=" + post.getTitle() + ", categoryId=" + post.getCategoryId() + ", status=" + post.getStatus());
        
        boolean success = blogService.createPost(post, user.getUserId());
        LOGGER.info("Create post result: " + success);
        
        if (!success) {
            throw new RuntimeException("Không thể tạo bài viết mới");
        }
    }

    private void handleUpdatePost(HttpServletRequest request, User user) {
        String postIdParam = request.getParameter("id");
        if (postIdParam == null) {
            throw new RuntimeException("ID bài viết không được cung cấp");
        }

        int postId = Integer.parseInt(postIdParam);
        BlogPost post = blogService.getPostByIdForAdmin(postId, user.getUserId());
        if (post == null) {
            throw new RuntimeException("Bài viết không tồn tại");
        }

        populatePostFromRequest(post, request);
        
        if (!blogService.updatePost(post, user.getUserId())) {
            throw new RuntimeException("Không thể cập nhật bài viết");
        }
    }

    private void handleDeletePost(HttpServletRequest request, User user) {
        String postIdParam = request.getParameter("id");
        if (postIdParam == null) {
            throw new RuntimeException("ID bài viết không được cung cấp");
        }

        int postId = Integer.parseInt(postIdParam);
        if (!blogService.deletePost(postId, user.getUserId())) {
            throw new RuntimeException("Không thể xóa bài viết");
        }
    }

    private void handlePublishPost(HttpServletRequest request, User user) {
        String postIdParam = request.getParameter("id");
        if (postIdParam == null) {
            throw new RuntimeException("ID bài viết không được cung cấp");
        }

        int postId = Integer.parseInt(postIdParam);
        if (!blogService.publishPost(postId, user.getUserId())) {
            throw new RuntimeException("Không thể xuất bản bài viết");
        }
    }

    private void handleUnpublishPost(HttpServletRequest request, User user) {
        String postIdParam = request.getParameter("id");
        if (postIdParam == null) {
            throw new RuntimeException("ID bài viết không được cung cấp");
        }

        int postId = Integer.parseInt(postIdParam);
        if (!blogService.unpublishPost(postId, user.getUserId())) {
            throw new RuntimeException("Không thể ẩn bài viết");
        }
    }    private void handleCreateCategory(HttpServletRequest request, User user) {
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        String slug = request.getParameter("slug");
        boolean isActive = "true".equals(request.getParameter("isActive"));

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new RuntimeException("Tên danh mục không được để trống");
        }

        // Auto-generate slug if not provided
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(categoryName.trim());
        }

        BlogCategory category = new BlogCategory(categoryName.trim(), description, slug.trim());
        category.setActive(isActive);
        
        if (!blogService.createCategory(category, user.getUserId())) {
            throw new RuntimeException("Không thể tạo danh mục mới");
        }
    }

    private void handleUpdateCategory(HttpServletRequest request, User user) {
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam == null) {
            throw new RuntimeException("ID danh mục không được cung cấp");
        }

        int categoryId = Integer.parseInt(categoryIdParam);
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        String slug = request.getParameter("slug");
        boolean isActive = "true".equals(request.getParameter("isActive"));

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new RuntimeException("Tên danh mục không được để trống");
        }

        BlogCategory category = new BlogCategory();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName.trim());
        category.setDescription(description);
        category.setSlug(slug);
        category.setActive(isActive);

        if (!blogService.updateCategory(category, user.getUserId())) {
            throw new RuntimeException("Không thể cập nhật danh mục");
        }
    }

    private void handleDeleteCategory(HttpServletRequest request, User user) {
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam == null) {
            throw new RuntimeException("ID danh mục không được cung cấp");
        }

        int categoryId = Integer.parseInt(categoryIdParam);
        if (!blogService.deleteCategory(categoryId, user.getUserId())) {
            throw new RuntimeException("Không thể xóa danh mục");
        }
    }

    private void handleActivateCategory(HttpServletRequest request, User user) {
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam == null) {
            throw new RuntimeException("ID danh mục không được cung cấp");
        }

        int categoryId = Integer.parseInt(categoryIdParam);
        if (!blogService.activateCategory(categoryId, user.getUserId())) {
            throw new RuntimeException("Không thể kích hoạt danh mục");
        }
    }

    private void handleDeactivateCategory(HttpServletRequest request, User user) {
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam == null) {
            throw new RuntimeException("ID danh mục không được cung cấp");
        }

        int categoryId = Integer.parseInt(categoryIdParam);
        if (!blogService.deactivateCategory(categoryId, user.getUserId())) {
            throw new RuntimeException("Không thể ẩn danh mục");
        }
    }    private void populatePostFromRequest(BlogPost post, HttpServletRequest request) {
        post.setTitle(request.getParameter("title"));
        post.setContent(request.getParameter("content"));
        post.setSummary(request.getParameter("summary"));
        post.setFeaturedImage(request.getParameter("featuredImage"));
        
        // Handle categoryId
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam);
                post.setCategoryId(categoryId);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid categoryId: " + categoryIdParam);
            }
        }
        
        post.setTags(request.getParameter("tags"));
        post.setStatus(request.getParameter("status"));
        
        LOGGER.info("Populated post from request: " + post.getTitle() + ", categoryId: " + post.getCategoryId());
    }

    /**
     * Generate slug from text (Vietnamese to ASCII)
     */
    private String generateSlug(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        return text.toLowerCase()
                .trim()
                // Replace Vietnamese characters
                .replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("đ", "d")
                // Remove special characters
                .replaceAll("[^a-z0-9\\s-]", "")
                // Replace spaces with hyphens
                .replaceAll("\\s+", "-")
                // Remove multiple hyphens
                .replaceAll("-+", "-")
                // Remove leading/trailing hyphens
                .replaceAll("^-|-$", "");
    }
}
