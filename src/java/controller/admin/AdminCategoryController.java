package controller.admin;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogCategory;
import model.entity.User;

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
 * Admin Category Management Controller
 * URL patterns: /admin-categories, /admin-category-action
 */
@WebServlet(urlPatterns = {"/admin-categories", "/admin-category-action"})
public class AdminCategoryController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminCategoryController.class.getName());
    private BlogService blogService;

    @Override
    public void init() throws ServletException {
        super.init();
        blogService = new BlogServiceImpl();
        LOGGER.info("AdminCategoryController initialized");
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

        LOGGER.info("AdminCategoryController handling GET request: " + path);

        try {
            if ("/admin-categories".equals(path)) {
                handleCategoriesList(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AdminCategoryController GET", e);
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

        LOGGER.info("AdminCategoryController handling POST request: " + path);

        try {
            if ("/admin-category-action".equals(path)) {
                handleCategoryAction(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AdminCategoryController POST", e);
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
     * Handle categories list page
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
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling admin categories list", e);
            request.setAttribute("errorMessage", "Lỗi tải danh sách danh mục: " + e.getMessage());
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
        }
    }

    /**
     * Handle category actions (create, update, delete, activate, deactivate)
     */
    private void handleCategoryAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            String action = request.getParameter("action");
            String redirectUrl = request.getContextPath() + "/admin-categories";

            switch (action) {
                case "create":
                    handleCreateCategory(request, user);
                    request.getSession().setAttribute("successMessage", "Danh mục đã được tạo thành công!");
                    break;
                    
                case "update":
                    handleUpdateCategory(request, user);
                    request.getSession().setAttribute("successMessage", "Danh mục đã được cập nhật thành công!");
                    break;
                    
                case "delete":
                    handleDeleteCategory(request, user);
                    request.getSession().setAttribute("successMessage", "Danh mục đã được xóa thành công!");
                    break;
                    
                case "activate":
                    handleActivateCategory(request, user);
                    request.getSession().setAttribute("successMessage", "Danh mục đã được kích hoạt!");
                    break;
                    
                case "deactivate":
                    handleDeactivateCategory(request, user);
                    request.getSession().setAttribute("successMessage", "Danh mục đã được ẩn!");
                    break;
                    
                default:
                    request.getSession().setAttribute("errorMessage", "Hành động không hợp lệ.");
                    break;
            }

            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling category action", e);
            request.getSession().setAttribute("errorMessage", "Lỗi thực hiện hành động: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-categories");
        }
    }

    private void handleCreateCategory(HttpServletRequest request, User user) {
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
